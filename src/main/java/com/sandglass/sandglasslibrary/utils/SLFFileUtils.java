package com.sandglass.sandglasslibrary.utils;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.commonapi.SLFApi;
//import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class SLFFileUtils {
    private static final String TAG = "PUTFileUtils";
    private static long mBlockSize = 0;
    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    private SLFFileUtils() {
    }

    /**
     * 删除文件，若为文件夹，则删除所有子文件夹和子文件
     *
     * @param filePath 文件/文件夹路径
     */
    public static void delete(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            delete(file);
        }
    }

    /**
     * 删除文件，若为文件夹，则删除所有子文件夹和子文件
     *
     * @param file 文件/文件夹
     */
    public static void delete(File file) {

        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                if (fileList != null) {
                    for (File subFile : fileList) {
                        File to = new File(subFile.getAbsolutePath() + System.currentTimeMillis());
                        boolean isRename = subFile.renameTo(to);
                        boolean isDelete = to.delete();
                        Log.e("isRename", isRename + "");
                        Log.e("delete-isDelete", isDelete + "");
                    }
                }
            } else if (file.isFile()) {
                boolean isDelete = file.delete();
                Log.e("delete", isDelete + "");
            }
        }
    }


    /**
     * 按文件类型删除
     *
     * @param filePath  文件/文件夹路径
     * @param extension 目标文件类型后缀名
     * @param isInclude 是否删除包含后缀名为extension的文件
     */
    public static void delete(String filePath, String extension, boolean isInclude) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            delete(file, extension, isInclude);
        }
    }

    /**
     * 按文件类型删除
     *
     * @param file      文件/文件夹
     * @param extension 目标文件类型后缀名
     * @param isInclude 是否删除包含后缀名为extension的文件
     */
    public static void delete(File file, String extension, boolean isInclude) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                if (fileList != null) {
                    for (File subFile : fileList) {
                        delete(subFile, extension, isInclude);
                    }
                }
            } else if (file.isFile()) {
                boolean isSameExtension = file.getName().endsWith(extension);
                if (isSameExtension == isInclude) {
                    boolean isDelete = file.delete();
                    Log.e("isDelete", isDelete + "");
                }
            }
        }
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            Log.e("getFileOrFilesSize", e.getMessage());
        }
        return formetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static long getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            Log.e("getAutoFileOrFilesSize", e.getMessage());
        }
        return blockSize;
    }

    /**
     *
     * @param filePath      文件/文件夹
     * @param extension 目标文件类型后缀名
     * @param isInclude 是否删除包含后缀名为extension的文件
     */
    public static long getAutoFileOrFilesSize(String filePath, String extension, boolean isInclude) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            mBlockSize = 0;
            getAutoFileOrFilesSize(file, extension, isInclude);
        }
        return mBlockSize;
    }

    /**
     * 获取指定结尾的文件大小
     */
    @SuppressWarnings("UnusedReturnValue")
    public static long getAutoFileOrFilesSize(File file, String extension, boolean isInclude) {
        if (file == null || !file.exists()) {
            return 0;
        }

        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File subFile : fileList) {
                    getAutoFileOrFilesSize(subFile, extension, isInclude);
                }
            }
        } else if (file.isFile()) {
            boolean isSameExtension = file.getName().endsWith(extension);
            if (isSameExtension == isInclude) {
                try {
                    mBlockSize = mBlockSize + getFileSize(file);
                    Log.d("filetools", "=========mBlockSize====" + mBlockSize);
                } catch (Exception e) {
                    Log.d("getAutoFileOrFilesSize", e.getMessage());
                }
            }
        }
        return mBlockSize;
    }

    /**
     * 获取指定文件大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                size = fis.available();
            } catch (IOException e) {
                //empty code
            }
        }
        return size;
    }

    /**
     * 获取指定文件夹
     */
    private static long getFileSizes(File f) {
        long size = 0;
        File[] flist = f.listFiles();
        if (flist == null) {
            return size;
        }
        for (File file : flist) {
            if (file.isDirectory()) {
                size = size + getFileSizes(file);
            } else {
                size = size + getFileSize(file);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     */
    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        String fileSizeString;
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     */
    private static double formetFileSize(long fileS, int sizeType) {

        double fileSizeLong = 0;
        try {
            Locale currentLocale = Locale.getDefault();
            Locale.setDefault(Locale.US);
            DecimalFormat df = new DecimalFormat("#.00");
            switch (sizeType) {
                case SIZETYPE_B:
                    fileSizeLong = Double.parseDouble(df.format((double) fileS));
                    break;
                case SIZETYPE_KB:
                    fileSizeLong = Double.parseDouble(df.format((double) fileS / 1024));
                    break;
                case SIZETYPE_MB:
                    fileSizeLong = Double.parseDouble(df.format((double) fileS / 1048576));
                    break;
                case SIZETYPE_GB:
                    fileSizeLong = Double.parseDouble(df
                            .format((double) fileS / 1073741824));
                    break;
                default:
                    break;
            }
            Locale.setDefault(currentLocale);
        } catch (Exception e) {
            //empty code
        }

        return fileSizeLong;
    }


    /**
     * 删除路径下的所有文件
     *
     * @param path 文件路径
     * @return true 成功 false 失败
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean deleteFiles(String path) {

        if (null == path || path.equals("")) {
            Log.d(TAG, " file: path=" + path);
            return false;
        }

        File file = new File(path);
        if (!file.exists()) {
            return false;
        }

        if (file.isFile()) {
            boolean isDelete = file.delete();
            Log.d(TAG,isDelete+"");
            return true;
        } else {
            File[] files = file.listFiles();
            if(files == null) {
                return false;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    boolean dele = files[i].delete();
                    Log.d(TAG , "dele=" + dele);
                } else {
                    Log.d(TAG, "getPath: files[" + i + "]" + files[i].getPath());
                    deleteFiles(files[i].getPath());
                }
            }
            boolean isDelete = file.delete();
            Log.d(TAG,isDelete+"");
            return true;

        }
    }

    public static List<String> getFilesAllName(String path) {
        List<String> s = new ArrayList<>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("error", "空目录");
            return s;
        }

        for (File value : files) {
            s.add(value.getAbsolutePath());
        }
        return s;
    }

    public static List<File> getFiles(String path) {
        File[] files = new File(path).listFiles();
        List<File> list;
        if (files == null) {
            list = new ArrayList<>();
        } else {
            list = Arrays.asList(files);
        }
        return list;
    }

    /**
     * 获取目录下所有文件(按时间排序最新-最老)
     *
     * @param path 路径
     * @return list
     */
    public static List<File> listFileSortByModifyTime(String path) {
        List<File> list = getFiles(path);
        if (!list.isEmpty()) {
            Collections.sort(list, (file, newFile) -> {
                if (file.lastModified() <= newFile.lastModified()) {
                    if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return -1;
                }
            });
        }
        return list;
    }


    //按照文件名称从大到小排序（文件名称必须按照一定规律）
    public static List<File> listFileSortByName(String path) {
        List<File> list = getFiles(path);
        if (!list.isEmpty()) {
            Collections.sort(list, (file, newFile) -> newFile.getName().compareTo(file.getName()));
        }
        return list;
    }


    public static String getPluginDataPath(String SLF) {
        String filePath = SLFConstants.documentPath + SLF;
        File file = new File(filePath);
        if (!file.exists()) {
            boolean isMkdirs = file.mkdirs();
            Log.e("getPluginDataPath",isMkdirs+"");
        }
        return filePath;
    }

    public static String getPluginCachePath(String SLF) {
        String filePath = SLFConstants.cachePath + SLF;
        File file = new File(filePath);
        if (!file.exists()) {
            boolean isMkdirs = file.mkdirs();
            Log.e("getPluginCachePath",isMkdirs+"");
        }
        return filePath;
    }

    public static long getCacheSize() {
        return SLFFileUtils.getAutoFileOrFilesSize(SLFConstants.cachePath);
    }

    public static long getPluginCacheSize(String SLF) {
        return SLFFileUtils.getAutoFileOrFilesSize(getPluginCachePath(SLF));
    }

    public static void deleteCache() {
        SLFFileUtils.delete(SLFConstants.cachePath);
    }

    public static void deleteCache(String SLF) {
        SLFFileUtils.delete(getPluginCachePath(SLF));
    }

    public static String getExternalPluginDataPath(String SLF){
        String path = SLFConstants.externalDocumentPath + SLF;
        File file = new File(path);
        if (!file.exists()) {
            boolean isMkdirs = file.mkdirs();
            //SLFLogUtil.sdke("getExternalPluginDataPath",isMkdirs+"");
        }
        return path;
    }

    public static String getExternalPluginCachePath(String appId){
        String path = SLFConstants.externalCachePath + appId;
        File file = new File(path);
        if (!file.exists()) {
            boolean isMkdirs = file.mkdirs();
            //SLFLogUtil.sdke("getExternalPluginDataPath",isMkdirs+"");
        }
        return path;
    }

    public static String getExternalPluginGalleyPath(String SLF){
        return SLFConstants.externalGallery + SLF;
    }

    public static void insertVideoToGalley(File file,String relativePath){
        String path = SLFConstants.externalGallery+relativePath;
        File sourceFile = new File(path);
        if(!sourceFile.exists()){
            sourceFile.mkdirs();
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.DISPLAY_NAME,file.getName());
        values.put(MediaStore.Video.Media.MIME_TYPE,"video/mp4");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            values.put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM+"/Psync/"+relativePath);
        }else{
            values.put(MediaStore.Images.Media.DATA,SLFConstants.externalGallery+relativePath+"/"+file.getName());
        }
        Uri uri = SLFApi.getSLFContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,values);

        int byteRead = 0;
        try (InputStream inputStream = new FileInputStream(file.getAbsolutePath())){
            try (OutputStream outputStream = SLFApi.getSLFContext().getContentResolver().openOutputStream(uri)) {
                byte[] buffer = new byte[4096];
                while ((byteRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, byteRead);
                }
                outputStream.flush();
            }
        } catch (IOException e) {
            Log.e("VideoCapture error",e.getMessage());
        }
    }

    public static void insertImageToGalley(File file,String relativePath){
        String path = SLFConstants.externalGallery+relativePath;
        File sourceFile = new File(path);
        if(!sourceFile.exists()){
            sourceFile.mkdirs();
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME,file.getName());
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM+"/Psync/"+relativePath);
        }else{
            values.put(MediaStore.Images.Media.DATA, SLFConstants.externalGallery+relativePath+"/"+file.getName());
        }
        Uri uri = SLFApi.getSLFContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Bitmap bp = BitmapFactory.decodeFile(file.getAbsolutePath());
        Matrix mat = new Matrix();
        try {
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int rotate = 0;
            switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                default:
                    rotate = ExifInterface.ORIENTATION_UNDEFINED;
                    break;
            }
            mat.postRotate(rotate);
            bp = Bitmap.createBitmap(bp,0,0,bp.getWidth(),bp.getHeight(),mat,true);
        } catch (IOException e) {
            //
        }

        try(OutputStream outputStream = SLFApi.getSLFContext().getContentResolver().openOutputStream(uri)) {
            bp.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
            outputStream.flush();
            bp.recycle();
        } catch (IOException e) {
            Log.e("ImageCapture error",e.getMessage());
        }
    }

    /*
     *slf文件迁移
     */

    //创建文件
    public static File newFile(String filePath, String fileName) {
        if (filePath == null || filePath.length() == 0
                || fileName == null || fileName.length() == 0) {
            return null;
        }
        try {
            //判断目录是否存在，如果不存在，递归创建目录
            File dir = new File(filePath);
            if (!dir.exists()) {
                boolean isMkdirs = dir.mkdirs();
                Log.e("newFile",isMkdirs+"");
            }

            //组织文件路径
            StringBuilder sbFile = new StringBuilder(filePath);
            if (!filePath.endsWith("/")) {
                sbFile.append("/");
            }
            sbFile.append(fileName);

            //创建文件并返回文件对象
            File file = new File(sbFile.toString());
            if (!file.exists()) {
                boolean isCreateNewFile = file.createNewFile();
                Log.e("isCreateNewFile",isCreateNewFile+"");
            }
            return file;
        } catch (Exception ex) {
            //empty code
        }
        return null;
    }


    //拷贝文件
    public static void copyFile(String filePath, String newDirPath) {
        if (filePath == null || filePath.length() == 0) {
            return;
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            //判断目录是否存在，如果不存在，则创建
            File newDir = new File(newDirPath);
            if (!newDir.exists()) {
                boolean isMkdirs = newDir.mkdirs();
                Log.e("copyFile",isMkdirs+"");
            }
            //创建目标文件
            File newFile = newFile(newDirPath, file.getName());
            try (InputStream is = new FileInputStream(file)) {
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    byte[] buffer = new byte[4096];
                    int byteCount;
                    while ((byteCount = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, byteCount);
                    }
                    fos.flush();
                }
            }
        } catch (Exception e) {
            //empty code
        }
    }

    //拷贝文件
    public static boolean copyFileWithCheck(String filePath, String newDirPath) {
        if (filePath == null || filePath.length() == 0) {
            return false;
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }
            //判断目录是否存在，如果不存在，则创建
            File newDir = new File(newDirPath);
            if (!newDir.exists()) {
                boolean isMkdirs = newDir.mkdirs();
                Log.e("copyFileWithCheck",isMkdirs+"");
            }
            //创建目标文件
            File newFile = newFile(newDirPath, file.getName());
            try (InputStream is = new FileInputStream(file)) {
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    byte[] buffer = new byte[4096];
                    int byteCount;
                    while ((byteCount = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, byteCount);
                    }
                    fos.flush();
                }
            }
            String md5Old = SLFMD5Util.getFileMD5(file);
            String md5New = SLFMD5Util.getFileMD5(newFile);
            Log.e("old", md5Old);
            Log.e("new", md5New);
            if (TextUtils.equals(md5Old, md5New)) {
                return true;
            } else {
                delete(newFile);
                return false;
            }
        } catch (Exception e) {
            //empty code
        }

        return false;
    }

    //拷贝目录
    public static void copyDir(String dirPath, String newDirPath) {
        if (dirPath == null || dirPath.length() == 0
                || newDirPath == null || newDirPath.length() == 0) {
            return;
        }
        try {
            File file = new File(dirPath);
            if (!file.exists() && !file.isDirectory()) {
                return;
            }
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return;
            }
            File newFile = new File(newDirPath);
            boolean isMkdirs = newFile.mkdirs();
            Log.e("copyDir","isMkdirs"+isMkdirs);
            for (File fileTemp : childFile) {
                if (fileTemp.isDirectory()) {
                    copyDir(fileTemp.getPath(), newDirPath + "/" + fileTemp.getName());
                } else {
                    copyFile(fileTemp.getPath(), newDirPath);
                }
            }

        } catch (Exception e) {
            //empty code
        }
    }

    //拷贝目录
    public static boolean copyDirWithCheck(String dirPath, String newDirPath) {
        if (dirPath == null || dirPath.length() == 0
                || newDirPath == null || newDirPath.length() == 0) {
            return false;
        }
        try {
            File file = new File(dirPath);
            if (!file.exists() && !file.isDirectory()) {
                return false;
            }
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return false;
            }
            File newFile = new File(newDirPath);
            boolean isMkdirs = newFile.mkdirs();
            Log.e("copyDirWithCheck","isMkdirs"+isMkdirs);
            boolean isSuccess = true;
            for (File fileTemp : childFile) {
                if (fileTemp.isDirectory()) {
                    isSuccess = isSuccess && copyDirWithCheck(fileTemp.getPath(), newDirPath + "/" + fileTemp.getName());
                } else {
                    isSuccess = isSuccess && copyFileWithCheck(fileTemp.getPath(), newDirPath);
                }
            }
            if (!isSuccess) {
                delete(newDirPath);
            }
            return isSuccess;
        } catch (Exception e) {
            //empty code
        }

        return false;
    }

    //剪切文件
    public static void moveFile(String filePath, String newDirPath) {
        if (filePath == null || filePath.length() == 0
                || newDirPath == null || newDirPath.length() == 0) {
            return;
        }
        try {
            //拷贝文件
            copyFile(filePath, newDirPath);
            //删除原文件
            delete(filePath);
        } catch (Exception e) {
            //empty code
        }
    }

    //剪切文件
    public static boolean moveFileWithCheck(String filePath, String newDirPath) {
        if (filePath == null || filePath.length() == 0
                || newDirPath == null || newDirPath.length() == 0) {
            return false;
        }
        try {
            //拷贝文件
            boolean isSuccess = copyFileWithCheck(filePath, newDirPath);
            if (isSuccess) {
                //删除原文件
                delete(filePath);
            }
            return isSuccess;
        } catch (Exception e) {
            //empty code
        }

        return false;
    }

    //剪切目录
    public static void moveDir(String dirPath, String newDirPath) {
        if (dirPath == null || dirPath.length() == 0
                || newDirPath == null || newDirPath.length() == 0) {
            return;
        }
        try {
            //拷贝目录
            copyDir(dirPath, newDirPath);
            //删除目录
            delete(dirPath);
        } catch (Exception e) {
            //empty code
        }
    }

    //剪切目录
    public static boolean moveDirWithCheck(String dirPath, String newDirPath) {
        if (dirPath == null || dirPath.length() == 0
                || newDirPath == null || newDirPath.length() == 0) {
            return false;
        }
        try {
            //拷贝目录
            boolean isSuccess = copyDirWithCheck(dirPath, newDirPath);
            if (isSuccess) {
                //删除目录
                delete(dirPath);
            }
            return isSuccess;

        } catch (Exception e) {
            //empty code
        }

        return false;
    }
}
