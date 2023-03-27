package com.sandglass.sandglasslibrary.utils.manager;

import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/**
 * 保存序列化对象到文件
 * Created by wangjian on 2023/3/24
 */
public class SLFCacheToFileManager<T> {
    private T mType;

    public SLFCacheToFileManager (T mType) {
        this.mType = mType;
    }
    //    private volatile static SLFCacheToFileManager instance;
//
//    private SLFCacheToFileManager() {
//
//    }
//
//    public SLFCacheToFileManager getInstance () {
//        if (instance == null) {
//            synchronized (SLFCacheToFileManager.class) {
//                if (instance == null) {
//                    instance = new SLFCacheToFileManager<T>();
//                }
//            }
//        }
//        return instance;
//    }
//

    /**
     * 保存序列化对象数据到本地
     */
    public void saveObj(String path,T obj){
        FileOutputStream fos=null;
        OutputStreamWriter osw = null;
        try {
            //如果文件不存在就创建文件
            File file=new File(path);
            if (!file.exists()){
                createFile(path);
            }
            String json = new Gson().toJson(obj);
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos, "utf-8");
            fos.write(json.getBytes());
            fos.close();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (fos!=null) {
                    fos.close();
                }
                if (osw!=null) {
                    osw.close();
                }
            } catch (IOException e) {
            }

        }

    }

    //读取数据
    public T readObj(String path) {
        StringBuilder sb = new StringBuilder();
        InputStreamReader  ois = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(path));
            ois = new InputStreamReader(fis, "utf-8");
            int tempbyte;
            while ((tempbyte = ois.read()) != -1) {
                sb.append((char) tempbyte);
            }
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Gson().fromJson(sb.toString(),(Type)mType);
    }



    /**
     * 创建多级目录
     * @param path 文件地址
     * @throws IOException
     */

    private void createFile(String path) throws IOException {

        if (!TextUtils.isEmpty(path)) {

            File file = new File(path);

            if (!file.getParentFile().exists()) {

                file.getParentFile().mkdirs();

            }

            file.createNewFile();

        }
    }

    /** 删除文件，可以是文件或文件夹
     * @param delFile 要删除的文件夹或文件名
     * @return 删除成功返回true，否则返回false
     */
    public boolean delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile())
                return deleteSingleFile(delFile);
            else
                return deleteDirectory(delFile);
        }
    }

    /** 删除单个文件
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public  boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /** 删除目录及目录下的文件
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public  boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }



}
