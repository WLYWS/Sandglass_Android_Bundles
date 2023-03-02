package com.sandglass.sandglasslibrary.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sandglass.sandglasslibrary.functionmoudle.activity.SLFMonitoredActivity;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SLFCropUtil {
    private static final String TAG = "CropUtil";

    private static final String SCHEME_FILE = "file";
    private static final String SCHEME_CONTENT = "content";

    public static void closeSilently(@Nullable Closeable c) {
        if (c == null) {return;}
        try {
            c.close();
        } catch (Throwable t) {
            // Do nothing
        }
    }

    public static int getExifRotation(File imageFile) {
        if (imageFile == null) {return 0;}
        try {
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            // We only recognize a subset of orientation tag values-en
            switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return ExifInterface.ORIENTATION_UNDEFINED;
            }
        } catch (IOException e) {
            Log.e("Error getting Exif data", e.toString());
            return 0;
        }
    }

    public static boolean copyExifRotation(File sourceFile, File destFile) {
        if (sourceFile == null || destFile == null) {return false;}
        try {
            ExifInterface exifSource = new ExifInterface(sourceFile.getAbsolutePath());
            ExifInterface exifDest = new ExifInterface(destFile.getAbsolutePath());
            exifDest.setAttribute(ExifInterface.TAG_ORIENTATION, exifSource.getAttribute(ExifInterface.TAG_ORIENTATION));
            exifDest.saveAttributes();
            return true;
        } catch (IOException e) {
            Log.e("Error copying Exif data", e.toString());
            return false;
        }
    }

    @Nullable
    public static File getFromMediaUri(Context context, ContentResolver resolver, Uri uri) {
        if (uri == null) {return null;}

        if (SCHEME_FILE.equals(uri.getScheme())) {
            return new File(uri.getPath());
        } else if (SCHEME_CONTENT.equals(uri.getScheme())) {
            final String[] filePathColumn = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, filePathColumn, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int columnIndex = (uri.toString().startsWith("content://com.google.android.gallery3d")) ?
                            cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME) :
                            cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                    // Picasa images on API 13+
                    if (columnIndex != -1) {
                        String filePath = cursor.getString(columnIndex);
                        if (!TextUtils.isEmpty(filePath)) {
                            return new File(filePath);
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                // Google Drive images
                return getFromMediaUriPfd(context, resolver, uri);
            } catch (SecurityException ignored) {
                // Nothing we can do
            } finally {
                if (cursor != null) {cursor.close();}
            }
        }
        return null;
    }

    private static String getTempFilename(Context context) throws IOException {
        File outputDir = context.getCacheDir();
        File outputFile = File.createTempFile("image", "tmp", outputDir);
        return outputFile.getAbsolutePath();
    }

    @Nullable
    private static File getFromMediaUriPfd(Context context, ContentResolver resolver, Uri uri) {
        if (uri == null) {return null;}

        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            ParcelFileDescriptor pfd = resolver.openFileDescriptor(uri, "r");
            FileDescriptor fd = pfd.getFileDescriptor();
            input = new FileInputStream(fd);

            String tempFilename = getTempFilename(context);
            output = new FileOutputStream(tempFilename);

            int read;
            byte[] bytes = new byte[4096];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            return new File(tempFilename);
        } catch (IOException ignored) {
            // Nothing we can do
        } finally {
            closeSilently(input);
            closeSilently(output);
        }
        return null;
    }

    public static void startBackgroundJob(SLFMonitoredActivity activity,
                                          String title, String message, Runnable job, Handler handler) {
        // Make the progress dialog uncancelable, so that we can guarantee
        // the thread will be done before the activity getting destroyed
        ProgressDialog dialog = ProgressDialog.show(
                activity, title, message, true, false);
        new Thread(new BackgroundJob(activity, job, dialog, handler)).start();
    }

    private static class BackgroundJob extends SLFMonitoredActivity.LifeCycleAdapter implements Runnable {

        private final SLFMonitoredActivity activity;
        private final ProgressDialog dialog;
        private final Runnable job;
        private final Handler handler;
        private final Runnable cleanupRunner = new Runnable() {
            @Override
            public void run() {
                activity.removeLifeCycleListener(BackgroundJob.this);
                if (dialog.getWindow() != null) {dialog.dismiss();}
            }
        };

        public BackgroundJob(SLFMonitoredActivity activity, Runnable job,
                             ProgressDialog dialog, Handler handler) {
            this.activity = activity;
            this.dialog = dialog;
            this.job = job;
            this.activity.addLifeCycleListener(this);
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                job.run();
            } finally {
                handler.post(cleanupRunner);
            }
        }

        @Override
        public void onActivityDestroyed(SLFMonitoredActivity activity) {
            // We get here only when the onDestroyed being called before
            // the cleanupRunner. So, run it now and remove it from the queue
            cleanupRunner.run();
            handler.removeCallbacks(cleanupRunner);
        }

        @Override
        public void onActivityStopped(SLFMonitoredActivity activity) {
            dialog.hide();
        }

        @Override
        public void onActivityStarted(SLFMonitoredActivity activity) {
            dialog.show();
        }
    }

    public static Bitmap rotateImage(Bitmap img, int rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, false);
        return img;
    }
    public static Bitmap rotateImage(Bitmap img, int rotate,int width,int height) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, false);
        return img;
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            Log.e(TAG, android.util.Log.getStackTraceString(e));
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param path   被旋转图片
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(Bitmap bitmap, String path) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(readPictureDegree(path));
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * 将图片的旋转角度置为0
     *
     * @param path
     * @return void
     * @Title: setPictureDegreeZero
     * @date 2012-12-10 上午10:54:46
     */
    public static void setPictureDegreeZero(String path,int degree) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            //修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
            //例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
            exifInterface.setAttribute(ExifInterface.ORIENTATION_ROTATE_270+"",degree+"");
            exifInterface.saveAttributes();
        } catch (IOException e) {
            Log.e(TAG, android.util.Log.getStackTraceString(e));
        }

    }
}
