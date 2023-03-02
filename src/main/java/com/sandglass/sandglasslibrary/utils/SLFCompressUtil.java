package com.sandglass.sandglasslibrary.utils;

import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * created by yangjie
 * describe:压缩工具类
 * time: 2022/12/21
 */
public class SLFCompressUtil {

    private static final String TAG = "SLFCompressUtil:";

    /**
     * 压缩文件或者目录
     *
     * @param baseDirName    压缩的根目录
     * @param fileName       根目录下待压缩的文件或文件夹名，星号*表示压缩根目录下的全部文件。
     * @param targetFileName 目标ZIP文件
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean zipFile(String baseDirName, String fileName, String targetFileName) {
        //检测根目录是否存在
        if (baseDirName == null) {
            return false;
        }
        File baseDir = new File(baseDirName);
        if (!baseDir.exists() || (!baseDir.isDirectory())) {
            SLFLogUtil.e(TAG, "file is not have：" + baseDirName);
            return false;
        }
        String baseDirPath = baseDir.getAbsolutePath();
        //目标文件
        File targetFile = new File(targetFileName);
        try {
            //创建一个zip输出流来压缩数据并写入到zip文件
            try (ZipOutputStream out = new ZipOutputStream(
                    new FileOutputStream(targetFile))) {
                if (fileName.equals("*")) {
                    //将baseDir目录下的所有文件压缩到ZIP
                    SLFCompressUtil.dirToZip(baseDirPath, baseDir, out);
                } else {
                    File file = new File(baseDir, fileName);
                    if (file.isFile()) {
                        SLFCompressUtil.fileToZip(baseDirPath, file, out);
                        SLFLogUtil.i(TAG, "fileToZip=" + targetFileName);
                    } else {
                        SLFCompressUtil.dirToZip(baseDirPath, file, out);
                    }
                }
            }
        } catch (IOException e) {
            SLFLogUtil.i(TAG, "zipFile e = " + e.getMessage());
            return false;
        }
        return true;
    }

    //带回调的压缩方法
    public static void zipFile(String baseDirName, String fileName, String targetFileName,OnCompressSuccessListener listener) {

        if(listener==null){
            return;
        }

        //检测根目录是否存在
        if (baseDirName == null) {
            SLFLogUtil.e(TAG, "baseDirName is null：");
            listener.onFailure();
            return;
        }
        File baseDir = new File(baseDirName);
        if (!baseDir.exists() || (!baseDir.isDirectory())) {
            SLFLogUtil.e(TAG, "file is not have：" + baseDirName);
            listener.onFailure();
            return;
        }
        String baseDirPath = baseDir.getAbsolutePath();
        //目标文件
        File targetFile = new File(targetFileName);
        try {
            //创建一个zip输出流来压缩数据并写入到zip文件
            try (ZipOutputStream out = new ZipOutputStream(
                    new FileOutputStream(targetFile))) {
                if (fileName.equals("*")) {
                    //将baseDir目录下的所有文件压缩到ZIP
                    SLFCompressUtil.dirToZip(baseDirPath, baseDir, out);
                } else {
                    File file = new File(baseDir, fileName);
                    if (file.isFile()) {
                        SLFCompressUtil.fileToZip(baseDirPath, file, out);
                        SLFLogUtil.i(TAG, "fileToZip=" + targetFileName);
                    } else {
                        SLFCompressUtil.dirToZip(baseDirPath, file, out);
                    }
                }
            }
        } catch (IOException e) {
            SLFLogUtil.i(TAG, "zipFile e = " + e.getMessage());
            listener.onFailure();
            return;
        }
        listener.onSuccess();
    }

    /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到targetDIR目录下
     *
     * @param zipFileName       待解压缩的ZIP文件名
     * @param targetBaseDirName 目标目录
     */
    public static void upzipFile(String zipFileName, String targetBaseDirName) {
        if (!targetBaseDirName.endsWith(File.separator)) {
            targetBaseDirName += File.separator;
        }
        try {
            //根据ZIP文件创建ZipFile对象
            try (ZipFile zipFile = new ZipFile(zipFileName)) {
                ZipEntry entry;
                String entryName;
                String targetFileName;
                byte[] buffer = new byte[4096];
                int bytesRead;
                //获取ZIP文件里所有的entry
                Enumeration<? extends ZipEntry> entrys = zipFile.entries();
                //遍历所有entry
                while (entrys.hasMoreElements()) {
                    entry = entrys.nextElement();
                    //获得entry的名字
                    entryName = entry.getName();
                    targetFileName = targetBaseDirName + entryName;
                    if (entry.isDirectory()) {
                        //  如果entry是一个目录，则创建目录
                        boolean isMkdirs = new File(targetFileName).mkdirs();
                        SLFLogUtil.e("isMkdirs",isMkdirs+"");
                        continue;
                    } else {
                        // 如果entry是一个文件，则创建父目录
                        File parentFile = new File(targetFileName).getParentFile();
                        if(parentFile!=null){
                            boolean isMkdirsParent = parentFile.mkdirs();
                            SLFLogUtil.e("isMkdirsParent",isMkdirsParent+"");
                        }

                    }

                    //否则创建文件
                    File targetFile = new File(targetFileName);
                    SLFLogUtil.d(TAG, "创建文件：" + targetFile.getAbsolutePath());
                    //打开文件输出流
                    InputStream is;
                    try (FileOutputStream os = new FileOutputStream(targetFile)) {
                        //从ZipFile对象中打开entry的输入流
                        is = zipFile.getInputStream(entry);
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                    is.close();
                }
            }
            SLFLogUtil.d(TAG, "upzipFile 解压缩文件成功！");
        } catch (IOException err) {
            SLFLogUtil.e(TAG, "upzipFile failed,error:" + err.getMessage());
        }
    }

    /**
     * 将目录压缩到ZIP输出流。
     */
    private static void dirToZip(String baseDirPath, File dir,
                                 ZipOutputStream out) {
        if (dir.isDirectory()) {
            //列出dir目录下所有文件
            File[] files = dir.listFiles();
            // 如果是空文件夹
            if (files==null || files.length == 0) {
                ZipEntry entry = new ZipEntry(getEntryName(baseDirPath, dir));
                // 存储目录信息
                try {
                    out.putNextEntry(entry);
                    out.closeEntry();
                } catch (IOException e) {
                    SLFLogUtil.i(TAG, "dirToZip e = " + e.getMessage());
                }
                return;
            }
            for (File file : files) {
                if (file.isFile()) {
                    //如果是文件，调用fileToZip方法
                    SLFLogUtil.i(TAG, "dirToZip: is file");
                    SLFCompressUtil.fileToZip(baseDirPath, file, out);
                } else {
                    //如果是目录，递归调用
                    SLFLogUtil.i(TAG, "dirToZip: is file path");
                    SLFCompressUtil.dirToZip(baseDirPath, file, out);
                }
            }
        }
    }

    /**
     * 将文件压缩到ZIP输出流
     */
    private static void fileToZip(String baseDirPath, File file,
                                  ZipOutputStream out) {
        // 创建复制缓冲区
        byte[] buffer = new byte[4096];
        int bytesRead;
        if (file.isFile()) {
                // 创建一个文件输入流
                try (FileInputStream in = new FileInputStream(file)) {
                    // 做一个ZipEntry
                    ZipEntry entry = new ZipEntry(getEntryName(baseDirPath, file));
                    // 存储项信息到压缩文件
                    out.putNextEntry(entry);
                    // 复制字节到压缩文件
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    out.closeEntry();
                }catch (IOException e){
                    SLFLogUtil.e(TAG, "fileToZip e = " + e.getMessage());
                }
            SLFLogUtil.d(TAG, "fileToZip: 添加文件"
                        + file.getAbsolutePath() + "被到ZIP文件中！");
        }
    }

    /**
     * 获取待压缩文件在ZIP文件中entry的名字。即相对于跟目录的相对路径名
     *
     * @param baseDirPath 路径
     * @param file 文件
     * @return string
     */
    private static String getEntryName(String baseDirPath, File file) {
        if (!baseDirPath.endsWith(File.separator)) {
            baseDirPath += File.separator;
        }
        String filePath = file.getAbsolutePath();
        // 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储。
        if (file.isDirectory()) {
            filePath += "/";
        }
        int index = filePath.indexOf(baseDirPath);
        return filePath.substring(index + baseDirPath.length());
    }

    /**
     * 复制文件或文件夹
     *
     * @param oldPath 资源路径
     * @param newPath 目的地路径
     * @return boolean
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean copyFolder(String oldPath, String newPath) {
        boolean isok = true;
        SLFLogUtil.i(TAG, "copyFolder: oldPath=" + oldPath);
        SLFLogUtil.i(TAG, "copyFolder: newPath=" + newPath);
        try {
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                boolean isMkdirs = newFile.mkdirs();
                SLFLogUtil.e(TAG,isMkdirs+"");
            }
            File oldFile = new File(oldPath);
            if (!oldFile.exists()) {
                return false;
            }
            String[] file = oldFile.list();
            if(file == null){
                return false;
            }
            File temp;
            for (String s : file) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + s);
                } else {
                    temp = new File(oldPath + File.separator + s);
                }

                if (temp.isFile()) {
                    try (FileInputStream input = new FileInputStream(temp)) {
                        try (FileOutputStream output = new FileOutputStream(newPath + "/" +
                                (temp.getName()))) {
                            byte[] b = new byte[1024 * 5];
                            int len;
                            while ((len = input.read(b)) != -1) {
                                output.write(b, 0, len);
                            }
                            output.flush();
                        }
                    }
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + s, newPath + "/" + s);
                }
            }
        } catch (Exception e) {
            SLFLogUtil.e(TAG, "copyFolder: Exception e = " + e.getMessage());
            isok = false;
        }
        return isok;
    }


    public interface OnCompressSuccessListener {
        void onSuccess();
        void onFailure();
    }

}