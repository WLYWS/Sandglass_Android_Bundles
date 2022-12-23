package com.wyze.sandglasslibrary.utils.logutil;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.wyze.sandglasslibrary.interf.SLFDebugLogReadCallback;
import com.wyze.sandglasslibrary.utils.SLFDateFormatUtils;
import com.wyze.sandglasslibrary.utils.SLFFileUtils;
import com.wyze.sandglasslibrary.utils.SLFSpUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SLFDebugLogUtils {
    private static final String TAG = SLFDebugLogUtils.class.getSimpleName();
    private static volatile SLFDebugLogUtils instance;
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private ExecutorService mExecutorService;
    private static final String ROOT_PATH = SLFDebugConfig.ROOT_PATH;
    private static final String CRASH_PATH = ROOT_PATH + File.separator + SLFDebugConfig.CRASH;
    private static final String NETWORK_PATH = ROOT_PATH + File.separator + SLFDebugConfig.NETWORK;

    private static final String SLF_DEBUG_LOG_PLUGIN_MAP = "SLF_DEBUG_LOG_PLUGIN_MAP";
    private static final String APP_ID = "9319141212m2ik";
    private static final String PREFIX = "SLF_";

    /**
     * 最小堆栈帖数
     */
    private static final int MIN_STACK_OFFSET = 3;
    private HashMap<String, String> pluginMap;//记录插件id和名称

    public static SLFDebugLogUtils getInstance() {
        if (instance == null) {
            synchronized (SLFDebugLogUtils.class) {
                if (instance == null) {
                    instance = new SLFDebugLogUtils();
                }
            }
        }
        return instance;
    }

    private SLFDebugLogUtils() {
        initExecutor();
        createDirs();
    }

    private void createDirs() {
        //创建根目录
        if (ROOT_PATH == null) {
            return;
        }
        File crashFile = new File(ROOT_PATH);
        if (!crashFile.exists()) {
            boolean mkdirs = crashFile.mkdirs();
            SLFLogUtil.i(TAG, "mkdirs: " + mkdirs);
        }
    }

    private void initExecutor() {
        if (mExecutorService == null || mExecutorService.isShutdown()) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
    }

    /**
     * 保存插件信息
     *
     * @param pluginId   插件id
     * @param pluginName 插件名称
     */
    @SuppressWarnings("unchecked")
    private void setPluginInfo(String pluginId, String pluginName) {
        if (TextUtils.isEmpty(pluginId)) {
            pluginId = APP_ID;
        }
        if (pluginMap == null) {
            pluginMap = SLFSpUtils.getHashMapData(SLF_DEBUG_LOG_PLUGIN_MAP);
        }
        if (pluginMap == null) {
            pluginMap = new HashMap<>();
        } else {
            if (pluginMap.containsKey(pluginId) && TextUtils.equals(pluginMap.get(pluginId), pluginName)) {
                return;
            }
        }
        pluginMap.put(pluginId, pluginName);
        SLFSpUtils.putHashMapData(SLF_DEBUG_LOG_PLUGIN_MAP, pluginMap);
    }

    private String getPluginName(String pluginId) {
        @SuppressWarnings("unchecked")
        HashMap<String, String> map = SLFSpUtils.getHashMapData(SLF_DEBUG_LOG_PLUGIN_MAP);
        if (map != null && map.containsKey(pluginId)) {
            return map.get(pluginId);
        }
        return pluginId;
    }

    /**
     * 根据插件ID, 自定义类型, 写log
     *
     * @param pluginId   插件ID
     * @param pluginName 插件名称(用于显示的名称)
     * @param category   自定义类型
     * @param text       文本
     */
    public void writeLog(String pluginId, String pluginName, String category, String text) {
        if (!SLFDebugConfig.isOpenLogEnable()) {
            return;
        }
        //保存插件id和名称
        setPluginInfo(pluginId, pluginName);
        if (TextUtils.isEmpty(category)) {
            category = SLFDebugConfig.INFO;
        }
        String filePath;
        if (SLFDebugConfig.CRASH.equals(category)) {
            filePath = CRASH_PATH;
            //添加日志的header
            text = "[" + getDateString() + "]: " + text;
        } else if (SLFDebugConfig.NETWORK.equals(category)) {
            filePath = NETWORK_PATH;
            text = "[" + getDateString() + "]: " + text;
        } else {
            filePath = ROOT_PATH + File.separator + pluginId + File.separator + category;
            text = "[" + getDateString() + "][" + logHeaderContent() + "]: " + text;
        }
        String fileName = PREFIX + SLFDateFormatUtils.getCurrentTime(SLFDateFormatUtils.ymdhm_cnSDF) + ".txt";
        initExecutor();
        mExecutorService.submit(new SaveTask(filePath, fileName, text));
    }

    /**
     * 获取插件列表
     */
    public List<SLFDebugData> getPluginList() {
        List<SLFDebugData> dirList = new ArrayList<>();
        if (!SLFDebugConfig.isOpenLogEnable()) {
            return dirList;
        }
        File crashFile = null;
        File networkFile = null;
        File rootFile = new File(ROOT_PATH);
        if (rootFile.exists() && rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            if (files != null) {
                //排序
                listFileSortByModifyTime(files);
                for (File file : files) {
                    if (file != null && file.isDirectory()) {
                        if (SLFDebugConfig.CRASH.equals(file.getName())) {
                            crashFile = file;
                        } else if (SLFDebugConfig.NETWORK.equals(file.getName())) {
                            networkFile = file;
                        } else {
                            String pluginName = getPluginName(file.getName());
                            if (TextUtils.isEmpty(pluginName)) {
                                pluginName = file.getName();
                            }
                            dirList.add(getDebugData(SLFDebugData.LEVEL_PLUGIN, file, pluginName));
                        }
                    }
                }
                if (networkFile != null) {
                    dirList.add(0, getDebugData(SLFDebugData.LEVEL_PLUGIN, networkFile, SLFDebugConfig.NETWORK));
                }
                if (crashFile != null) {
                    dirList.add(0, getDebugData(SLFDebugData.LEVEL_PLUGIN, crashFile, SLFDebugConfig.CRASH));
                }
            }
        }
        return dirList;
    }

    /**
     * 根据pluginId或者category获取下级列表
     */
    public List<SLFDebugData> getDebugData(SLFDebugData data) {
        if (!SLFDebugConfig.isOpenLogEnable()) {
            return new ArrayList<>();
        }
        ArrayList<SLFDebugData> list = new ArrayList<>();
        if (data == null || TextUtils.isEmpty(data.getPath())) {
            return list;
        }
        String dirPath = data.getPath();
        File dirFile = new File(dirPath);
        if (dirFile.exists() && dirFile.isDirectory()) {
            File[] files = dirFile.listFiles();
            if (files != null) {
                //排序
                listFileSortByModifyTime(files);
                for (File file : files) {
                    if (file != null) {
                        SLFDebugData debugData = null;
                        if (data.getLevel() == SLFDebugData.LEVEL_PLUGIN) {
                            if ((CRASH_PATH.equals(dirPath) || NETWORK_PATH.equals(dirPath)) && file.isFile()) {
                                debugData = getDebugData(SLFDebugData.LEVEL_CATEGORY, file, getFileDate(getFileNameNoEx(file.getName())));
                            } else if (file.isDirectory()) {
                                debugData = getDebugData(SLFDebugData.LEVEL_CATEGORY, file, file.getName());
                                debugData.setCategory(file.getName());
                            }
                        } else if (data.getLevel() == SLFDebugData.LEVEL_CATEGORY && file.isFile()) {
                            debugData = getDebugData(SLFDebugData.LEVEL_FILE, file, getFileDate(getFileNameNoEx(file.getName())));
                            debugData.setCategory(data.getCategory());
                        }
                        if (debugData != null) {
                            debugData.setPluginId(data.getPluginId());
                            debugData.setPluginName(data.getPluginName());
                            list.add(debugData);
                        }
                    }
                }
            }
        }
        return list;
    }

    private SLFDebugData getDebugData(int level, File file, String name) {
        SLFDebugData data = new SLFDebugData();
        data.setLevel(level);
        data.setDirectory(file.isDirectory());
        data.setFile(file.isFile());
        data.setPath(file.getPath());
        data.setName(name);
        if (level == SLFDebugData.LEVEL_PLUGIN) {
            data.setPluginId(file.getName());
            data.setPluginName(name);
        }
        return data;
    }

    /**
     * 根据log文件路径获取log内容, 异步获取
     *
     * @param logFilePath log文件绝对路径
     * @param callback    回调
     */
    public void readLog(String logFilePath, SLFDebugLogReadCallback callback) {
        if (callback == null) {
            SLFLogUtil.i(TAG, "callback == null");
            return;
        }
        if (!SLFDebugConfig.isOpenLogEnable()) {
            SLFLogUtil.i(TAG, "debug log disable");
            callback.onReadFailed(logFilePath);
            return;
        }
        if (logFilePath == null) {
            SLFLogUtil.i(TAG, "logFilePath == null");
            callback.onReadFailed(null);
            return;
        }
        initExecutor();
        mExecutorService.submit(new ReadTask(logFilePath, callback));
    }

    //写文件操作
    private static class SaveTask implements Runnable {

        private final String content;
        private final String filePath;
        private final String fileName;

        public SaveTask(String filePath, String fileName, String content) {
            this.filePath = filePath;
            this.fileName = fileName;
            this.content = content;
        }

        @Override
        public void run() {
            File logFile = SLFFileUtils.newFile(filePath, fileName);
            if (logFile == null) {
                return;
            }
            FileWriter writer = null;
            try {
                // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
                writer = new FileWriter(logFile.getAbsolutePath(), true);
                writer.write(content);
                writer.write("\r\n");
            } catch (IOException e) {
                SLFLogUtil.e(TAG, e.toString());
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    SLFLogUtil.e(TAG, e.toString());
                }
            }
        }
    }

    //读文件操作
    private static class ReadTask implements Runnable {

        private final String logFilePath;
        private final SLFDebugLogReadCallback callback;

        public ReadTask(String logFilePath, SLFDebugLogReadCallback callback) {
            this.logFilePath = logFilePath;
            this.callback = callback;
        }

        @Override
        public void run() {
            File file = new File(logFilePath);
            if (!file.exists() || file.isDirectory()) {
                SLFLogUtil.i(TAG, "logFile not exist");
                handler.post(() -> callback.onReadFailed(logFilePath));
                return;
            }
            final StringBuilder content = new StringBuilder(); //文件内容字符串
            InputStream is = null;
            InputStreamReader inputReader = null;
            BufferedReader bufferReader = null;
            try {
                is = new FileInputStream(file);
                inputReader = new InputStreamReader(is);
                bufferReader = new BufferedReader(inputReader);
                String line;
                //分行读取
                while ((line = bufferReader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (Exception e) {
                SLFLogUtil.e(TAG, e.toString());
            } finally {
                try {
                    if (bufferReader != null) {
                        bufferReader.close();
                    }
                    if (inputReader != null) {
                        inputReader.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    SLFLogUtil.e(TAG, e.toString());
                }
            }
            SLFLogUtil.i(TAG, "logFile read success");
            handler.post(() -> callback.onReadSuccess(logFilePath, content.toString()));
        }
    }

    private String getDateString() {
        return SLFDateFormatUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
    }

    private String logHeaderContent() {
        try {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            int stackOffset = -1;
            for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
                StackTraceElement e = trace[i];
                String name = e.getClassName();
                if (!name.equals(SLFDebugLogUtils.class.getName())) {
                    stackOffset = i - 1;
                    break;
                }
            }
            StackTraceElement element = trace[stackOffset + 1];
            return element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1).split("\\$")[0] +
                    "." + element.getMethodName() + "(" + element.getLineNumber() + ")";
        } catch (Exception e) {
            SLFLogUtil.e(TAG, e.toString());
        }
        return "";
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     */
    private String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    //得到文件名中的日期
    private String getFileDate(String nameNoEx) {
        try {
            if (nameNoEx != null && nameNoEx.startsWith(PREFIX)) {
                nameNoEx = nameNoEx.split("_")[1];
            }
        } catch (Exception e) {
            SLFLogUtil.e(TAG, e.toString());
        }
        return nameNoEx;
    }

    //时间倒序排序, 最新的在上面
    private void listFileSortByModifyTime(File[] files) {
        Arrays.sort(files, (f1, f2) -> {
            long diff = f1.lastModified() - f2.lastModified();
            if (diff > 0) {
                return -1;
            }
            else if (diff == 0) {
                return 0;
            }
            else {
                return 1;
            }
        });
    }

}