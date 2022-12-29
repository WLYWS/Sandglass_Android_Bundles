package com.wyze.sandglasslibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.LocaleList;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SLFCommonUtils {
    private static final String TAG = "SLFCommonUtil  ";
    public static final int FILE_SAVE_FINISHED = 30000;
    public static final int FILE_SAVE_ERROR = 30001;

    public static final long SIZE_GB = 1073741824; // 1024*1024*1024
    public static final long SIZE_MB = 1048576; // 1024*1024
    public static final long SIZE_KB = 1024; // 1024

    private SLFCommonUtils() {
    }

    /**
     * @param versionA 版本A
     * @param versionB 版本
     * @return 0: versionA 等于 versionB 1: versionA 小于 versionB 2: versionA 大于 versionB
     */
    public static int compareOldVersion(String versionA, String versionB) {
        int result = 0;
        try {
            if (TextUtils.isEmpty(versionA) || TextUtils.isEmpty(versionB)) {
                return 2;
            } else {
                versionA = versionA.replaceAll("[a-zA-Z]", "");
                versionB = versionB.replaceAll("[a-zA-Z]", "");
                String[] ver = versionA.split("\\.");
                String[] ver1 = versionB.split("\\.");
                int length = Math.min(ver.length, ver1.length);
                for (int i = 0; i < length; i++) {
                    if (Integer.parseInt(ver[i]) == Integer.parseInt((ver1[i]))) {
                        result = 0;
                    } else if (Integer.parseInt(ver[i]) > Integer.parseInt((ver1[i]))) {
                        result = 2;
                        break;
                    } else if (Integer.parseInt(ver[i]) < Integer.parseInt((ver1[i]))) {
                        result = 1;
                        break;
                    }
                }

                if (result == 0) {
                    if (ver.length > ver1.length) {
                        result = 2;
                    } else if (ver.length < ver1.length) {
                        result = 1;
                    }
                }
            }
        } catch (Exception e) {
            SLFLogUtil.e(TAG, "compareVersion error:" + e.getMessage());
            result = 0;
        }
        SLFLogUtil.d(TAG, "compareVersion + versionA=" + versionA + ",  versionB=" + versionB + ",  result=" + result);
        return result;
    }

    /**
     * @param versionA 版本A
     * @param versionB 版本B
     * @return 0: A等于B  1: A小于B  2: A大于B  -1:版本号的格式不对
     */
    public static int compareVersion(String versionA, String versionB) {
        if (TextUtils.isEmpty(versionA) || TextUtils.isEmpty(versionB)) {
            return -1;
        }
        Pattern pattern = Pattern.compile("^([1-9]\\d*|[0-9])(\\.([1-9]\\d*|[0-9])){2}(\\.[a-c]([1-9]\\d*|[0-9])?)?$");
        if (!pattern.matcher(versionA).matches()
                || !pattern.matcher(versionB).matches()) {
            return compareOldVersion(versionA, versionB);
        }
        String[] verA = versionA.split("\\.");
        String[] verB = versionB.split("\\.");
        int len = Math.min(verA.length, verB.length);
        if (len < 4) {
            for (int i = 0; i < len; i++) {
                int a = Integer.parseInt(verA[i]);
                int b = Integer.parseInt(verB[i]);
                if (a < b) {
                    return 1;
                } else if (a > b) {
                    return 2;
                }
            }
        } else {
            for (int i = 0; i < len - 1; i++) {
                int a = Integer.parseInt(verA[i]);
                int b = Integer.parseInt(verB[i]);
                if (a < b) {
                    return 1;
                } else if (a > b) {
                    return 2;
                }
            }
            String verALast1 = verA[len - 1].substring(0, 1);
            String verALast2 = verA[len - 1].substring(1);
            String verBLast1 = verB[len - 1].substring(0, 1);
            String verBLast2 = verB[len - 1].substring(1);
            if (verALast1.compareTo(verBLast1) < 0) {
                return 1;
            } else if (verALast1.compareTo(verBLast1) > 0) {
                return 2;
            } else {
                if (Integer.parseInt(verALast2) < Integer.parseInt(verBLast2)) {
                    return 1;
                } else if (Integer.parseInt(verALast2) > Integer.parseInt(verBLast2)) {
                    return 2;
                } else {
                    return 0;
                }
            }
        }


        if (verA.length > verB.length) {
            return 1;
        } else if (verA.length < verB.length) {
            return 2;
        }

        return 0;
    }

    public static final String DEFAULT_TIMEZONE = "+08:00";

    /**
     * +8格式与+08：00格式的相互转换
     */
    @SuppressWarnings("java:S3776")
    public static String getTimeZone(String timeZone) {
        SLFLogUtil.i(TAG, "getTimeZone in put timeZone=" + timeZone);
        String newTimeZone;
        try {
            int index = timeZone.indexOf(':');
            if (index > 0) {
                String start = timeZone.substring(0, 1);
                String hour = timeZone.substring(1, index);
                if (hour.equals(""))
                    hour = "0";
                int h = Integer.parseInt(hour);
                if (start.equals("+")) {
                    newTimeZone = h + "";
                } else {
                    newTimeZone = start + h;
                }
            } else {
                if (timeZone.length() > 2) {
                    String start = timeZone.substring(0, 1);
                    String hour = timeZone.substring(1);
                    if (hour.equals(""))
                        hour = "0";
                    if (hour.startsWith("0")) {
                        int h = Integer.parseInt(hour);
                        newTimeZone = start + "0" + h + ":00";
                    } else {
                        int h = Integer.parseInt(hour);
                        newTimeZone = start + h + ":00";
                    }
                } else {
                    if (timeZone.startsWith("+") || timeZone.startsWith("-")) {
                        String start = timeZone.substring(0, 1);
                        String hour = timeZone.substring(1);
                        if (hour.equals(""))
                            hour = "0";
                        int h = Integer.parseInt(hour);
                        newTimeZone = start + "0" + h + ":00";
                    } else {
                        if (timeZone.length() == 1) {
                            newTimeZone = "+0" + timeZone + ":00";
                        } else if (timeZone.length() == 2) {
                            newTimeZone = "+" + timeZone + ":00";
                        } else {
                            newTimeZone = DEFAULT_TIMEZONE;
                        }
                    }
                }
            }
        } catch (Exception e) {
            newTimeZone = DEFAULT_TIMEZONE;
            SLFLogUtil.e(TAG, "getTimeZone error: " + e.getMessage());
        }
        return newTimeZone;
    }

    /**
     * android5.0以上获取状态栏的高度
     *
     * @param context context
     * @return int
     */
    @SuppressLint("PrivateApi")
    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Field field;
        int x;
        int sbar = 0;

        try {
            c = Class.forName("com.android.internal.R$dimen");
            field = c.getField("status_bar_height");
            Object obj = field.get(c.newInstance());
            if (obj != null) {
                x = Integer.parseInt(obj.toString());
                sbar = context.getResources().getDimensionPixelSize(x);
            }

        } catch (Exception e1) {
            //empty code
        }

        return sbar;
    }

    public static double getStatusBarHeights(Context context){

        double statusBarHeight = Math.ceil(25 * context.getResources().getDisplayMetrics().density);

        return statusBarHeight;

    }


    /**
     * 在截图或者保存最后一张图片后通知系统媒体库更新
     *
     * @param context context
     *
     */
    @SuppressWarnings("deprecation")
    public static void notifyScanSDCardMedia(Context context, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri fileContentUri = Uri.fromFile(file);
                mediaScannerIntent.setData(fileContentUri);
                context.sendBroadcast(mediaScannerIntent);
            } else {
                SLFLogUtil.e(TAG, "file not exist:path=" + path);
            }
        } catch (Exception e) {
            SLFLogUtil.e(TAG, "error:" + e.getMessage());
        }
    }

    /**
     * 确认手机是否有SD卡
     *
     * @return boolean
     */
    public static boolean isHaveSdcard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }


    @SuppressWarnings("java:S4042")
    public static void saveBitmapToPath(final Handler handler, final Bitmap bitmap, final String path, final String fileName) {
        SLFLogUtil.d(TAG, "saveBitmapToPath path=" + path + ", filaName=" + fileName);
        if (bitmap == null || TextUtils.isEmpty(path) || TextUtils.isEmpty(fileName)) {
            return;
        }
        Runnable saveRunnable = () -> {
            try {
                File dir = new File(path);
                if (!dir.exists()) {
                    boolean isMkdir = dir.mkdirs();
                    Log.e("isMkdir", isMkdir + "");
                }
                File f = new File(path, "new" + fileName);
                boolean isCreateSuccess = f.createNewFile();
                Log.e("isCreateSuccess", isCreateSuccess + "");
                FileOutputStream fOut = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();
                File oldFile = new File(path, fileName);

                if (oldFile.exists()) {
                    boolean isDelete = oldFile.delete();
                    Log.e("isDelete", isDelete + "");
                }
                boolean isRenameSuccess = f.renameTo(new File(path, fileName));
                Log.e("isRenameSuccess", isRenameSuccess + "");
                if (handler != null) {
                    handler.sendEmptyMessage(FILE_SAVE_FINISHED);
                }
            } catch (Exception e) {
                if (handler != null) {
                    handler.sendEmptyMessage(FILE_SAVE_ERROR);
                }
                SLFLogUtil.e(TAG, "saveBitmapToPath exception:" + e.getMessage());
            }

        };
        Thread thread = new Thread(saveRunnable);
        thread.start();
    }

    /**
     * 2022-12-15 获取所有的截屏图片的地址
     *
     * @return ArrayList
     */
    @SuppressWarnings("java:S1319")
    public static ArrayList<String> getAllPicturePath(String filePath) {
        File baseFile = new File(filePath);
        ArrayList<String> paths = new ArrayList<>();
        if (baseFile.exists()) {
            paths = getAllPicturePath(baseFile);
            return paths;
        } else {
            boolean isMkdir = baseFile.mkdir();
            SLFLogUtil.e(TAG, isMkdir + "");
        }
        return paths;
    }

    /**
     * 获取图片地址列表
     *
     * @param file file
     * @return list
     */
    @SuppressWarnings({"java:S4042", "java:S1319"})
    public static ArrayList<String> getAllPicturePath(File file) {
        ArrayList<String> list = new ArrayList<>();

        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.length() > 100) {
                    list.add(f.getAbsolutePath());
                } else {
                    try {
                        if (f.exists()) {
                            boolean isDelete = f.delete();
                            Log.e(TAG, isDelete + "");
                        }
                    } catch (Exception e) {
                        //
                    }
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    /**
     * 获取当前的SSID
     *
     * @param applicationContext 必须传入Application Context，否则会造成Activity不释放，慎用
     */
    public static String getPhoneSSID(Context applicationContext) {
        WifiManager wifiManager = (WifiManager) applicationContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            SLFLogUtil.d(TAG + "getSSID", "wifiInfo" + wifiInfo.toString());
            SLFLogUtil.d(TAG + "getSSID", "SSID" + wifiInfo.getSSID());
            return whetherToRemoveTheDoubleQuotationMarks(wifiInfo.getSSID());
        } else {
            return "";
        }
    }

    // 根据Android的版本判断获取到的SSID是否有双引号
    public static String whetherToRemoveTheDoubleQuotationMarks(String ssid) {
        // 获取Android版本号
        int deviceVersion = Build.VERSION.SDK_INT;
        if (deviceVersion >= 17) {
            if (!ssid.startsWith("\"") || !ssid.endsWith("\"")) {
                return ssid;
            }
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        return ssid;
    }

    public static Calendar date2Calendar(Date date) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        return mCalendar;
    }

    public static String[] floatArray2StringArray(float[] floatArray) {
        int length = floatArray.length;
        String[] strArray = new String[length];
        for (int i = 0; i < length; i++) {
            strArray[i] = String.valueOf((int) floatArray[i]);
        }
        return strArray;
    }


    public static int dip2px(final Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 将listB中的元素插入listA中，并保证listA中元素的唯一性
    @SuppressWarnings("java:S1319")
    public static ArrayList<Object> addToListNonRepeat(ArrayList<Object> listA, ArrayList<Object> listB) {
        if (listA == null) {
            return new ArrayList<>();
        } else if (listB == null) {
            return listA;
        } else {
            for (int i = 0; i < listB.size(); i++) {
                try {
                    Object var = listB.get(i);
                    if (!listA.contains(var)) {
                        listA.add(var);
                    }
                } catch (Exception e) {
                    //
                }
            }
        }
        return listA;
    }

    public static synchronized String getCounterTimeString(long timestampInSec, String format) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestampInSec * 1000);
        SimpleDateFormat sDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return sDateFormat.format(new Date(calendar.getTimeInMillis()));
    }

    public static synchronized String formatDateTimeByLocal(long timestampInSec) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestampInSec * 1000);
        return DateFormat.getInstance().format(new Date(calendar.getTimeInMillis()));
    }

    public static synchronized String formatDateByLocal(long timestampInSec) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestampInSec * 1000);
        String date = DateFormat.getInstance().format(new Date(calendar.getTimeInMillis()));
        try {
            if (date.contains(" ")) {
                date = date.substring(0, date.indexOf(' '));
            }
        } catch (Exception e) {
            //
        }
        try {
            if (date.contains("-")) {
                date = date.replace("-", "/");
            }
        } catch (Exception e) {
            //
        }
        return date;
    }

    public static synchronized String formatTimeByLocal(long timestampInSec) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestampInSec * 1000);
        String time = DateFormat.getInstance().format(new Date(calendar.getTimeInMillis()));
        try {
            if (time.contains(" ")) {
                time = time.substring(time.indexOf(' ') + 1);
            }
        } catch (Exception e) {
            //
        }
        return time;
    }

    public static String getFileSizeStr(long fileLengthInBytes) {
        if (fileLengthInBytes > SIZE_GB) { // GB
            double result = (double) fileLengthInBytes / SIZE_GB;
            String str = String.valueOf(result);
            return str.substring(0, str.indexOf('.') + 2) + " GB";
        } else if (fileLengthInBytes > SIZE_MB) { // GB
            double result = (double) fileLengthInBytes / SIZE_MB;
            String str = String.valueOf(result);
            return str.substring(0, str.indexOf('.') + 2) + " MB";
        } else {
            double result = (double) fileLengthInBytes / SIZE_KB;
            String str = String.valueOf(result);
            return str.substring(0, str.indexOf('.') + 2) + " KB";
        }
    }

    public static int getProgress(long startTimeInSec, long endTimeInSec, long frameTimeStamp, int progressMaxValue) {
        if (frameTimeStamp < startTimeInSec) {
            return 0;
        } else {
            if (frameTimeStamp > endTimeInSec) {
                return progressMaxValue;
            } else {
                double duration = (double) (endTimeInSec - startTimeInSec);
                double passed = (double) (frameTimeStamp - startTimeInSec);
                return (int) ((passed / duration) * progressMaxValue);
            }
        }
    }

    public static long getTimeInSecFromProgress(long startTimeInSec, long endTimeInSec, int progress, int progressMaxValue) {
        if (progress < 0 || progress > progressMaxValue) {
            progress = 0;
        }
        double duration = (double) (endTimeInSec - startTimeInSec);
        double passed = duration * progress / progressMaxValue;
        SLFLogUtil.d("aa", "startTimeInSec = " + startTimeInSec + "   endTimeInSec = " + endTimeInSec + "  progress = " + progress + "  progressMaxValue = " + progressMaxValue + " passed =" + passed);
        return (int) (startTimeInSec + passed);
    }

    public static float kbps2mbps(int kBps) {
        return kBps * 8f / 1000f;
    }

    public static String kbps2mbpsStr(int kBps) {
        float value = kbps2mbps(kBps);
        String str = String.valueOf(value);
        int index = str.indexOf('.');
        return str.substring(0, index + 2);
    }

    public static float getFloatWithLen(float src, int len) {
        BigDecimal b1 = BigDecimal.valueOf(src);
        BigDecimal b2 = new BigDecimal(1);
        // 任何一个数字除以1都是原数字
        // ROUND_HALF_UP是BigDecimal的一个常量,表示进行四舍五入的操作
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).floatValue();
    }


    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @SuppressLint("MissingPermission")
    public static Location getLocation(Context context) {
        //获取显示地理位置信息的TextView
        //获取地理位置管理器
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return null;
        }
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        String locationProviderStr;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProviderStr = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProviderStr = LocationManager.NETWORK_PROVIDER;
        } else {
            return null;
        }
        //获取Location
        int permissionCode = context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
        if (permissionCode == PackageManager.PERMISSION_GRANTED) {
            return locationManager.getLastKnownLocation(locationProviderStr);
        } else {
            return null;
        }
    }

    /**
     * 图像转base64编码
     */
    public static String picPathToBase64(String path, String picturename) {
        String stringBase64 = "";
        // 将Bitmap转换成字符串
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path + "/" + picturename);
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] bytes = bStream.toByteArray();
            stringBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception ex) {
            return stringBase64;
        }
        return stringBase64;
    }

    /**
     * 图像转base64编码
     */
    public static String picPathToBase64(Bitmap imageBitmap) {
        String stringBase64 = "";
        // 将Bitmap转换成字符串
        try {
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] bytes = bStream.toByteArray();
            stringBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception ex) {
            return stringBase64;
        }
        return stringBase64;
    }

    // offsetMillis=timezone.getRawOffset()
    public static String createGmtOffsetString(boolean includeGmt,
                                               boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, offsetMinutes % 60);
        return builder.toString();
    }

    private static void appendNumber(StringBuilder builder, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < 2 - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
    }


    public static String dayDifWithNow(long timestamp) {

        String dayDif;

        SLFLogUtil.d(TAG, "dayDifWithNow = " + timestamp + "  " + (new Date(timestamp * 1000)));
        long difference = System.currentTimeMillis() / 1000 - timestamp;
        if (difference < 0) {
            difference = 0;
        }
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minutes = rightNow.get(Calendar.MINUTE);
        int second = rightNow.get(Calendar.SECOND);
        long todayPast = hour * 3600L + minutes * 60 + second;
        long dif = difference - todayPast;
        if (dif <= 0) {
            dayDif = "今天";
        } else if (dif <= 86400) {
            dayDif = "昨天";
        } else {
            dayDif = "";
        }

        return dayDif;
    }

    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap2(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        //压缩到一个最小长度是edgeLength的bitmap
        int longerEdge = edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg);
        int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
        int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
        Bitmap scaledBitmap;

        try {
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
        } catch (Exception e) {
            return null;
        }

        //从图中截取正中间的正方形部分。
        int xTopLeft = (scaledWidth - edgeLength) / 2;
        int yTopLeft = (scaledHeight - edgeLength) / 2;

        try {
            result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
            scaledBitmap.recycle();
        } catch (Exception e) {
            return bitmap;
        }

        return result;
    }

    public static float getScreenDensity() {
        DisplayMetrics dm = SLFResourceUtils.getResources().getDisplayMetrics();
        return dm.density;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenWidth() {
        DisplayMetrics dm = SLFResourceUtils.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = SLFResourceUtils.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取本地视频的时长
     *
     * @param url 本地视频路径
     * @return String
     */
    public static String getLocalVideoDurationInStr(String url, String defaultTime) {
        if (TextUtils.isEmpty(url)) {
            return "00:00";
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        String duration = "0";
        try {
            retriever.setDataSource(url);
            duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            retriever.release();
        } catch (Exception e) {
            //
        }
        String counterTimeString = defaultTime;
        if (!TextUtils.isEmpty(duration)) {
            int second = (int) (Long.parseLong(duration) / 1000);
            int m = second / 60;
            int s = (second - m * 60) % 60;
            String minutes = m >= 10 ? m + "" : "0" + m;
            String seconds = s >= 10 ? s + "" : "0" + s;
            counterTimeString = minutes + ":" + seconds;
            SLFLogUtil.i(TAG, "==============counterTimeString==============" + counterTimeString);
        }
        return counterTimeString;
    }

    /**
     * 获取本地视频的时长
     *
     * @param url 本地视频路径
     * @return bitmap
     */
    public static long getLocalVideoDurationInLong(String url, long defaultValue) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(url);
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release();
        long res = defaultValue;
        try {
            res = Long.parseLong(duration);
        } catch (Exception e) {
            //
        }
        return res;
    }


    /**
     * 获取本地视频的首帧图 ,获取不到返回默认 play_nonepicture_01
     * 此方法只能对单一视频使用，在视频列表中使用会造成OOM
     *
     * @param url 本地视频路径
     * @return bitmap
     */
    public static Bitmap getVideoFirstFrameBitmap(String url, Bitmap defaultImage) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(url);
            // 得到第一秒时刻的bitmap比如第一秒
            Bitmap bitmap = retriever.getFrameAtTime(1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            retriever.release();
            if (bitmap != null) {
                return bitmap;
            } else {
                return defaultImage;
            }
        } catch (Exception e) {
            return defaultImage;
        }
    }

    public static Bitmap readBitmapFromResource(Resources resources, int resourcesId, int width, int height) {
        InputStream ins = resources.openRawResource(resourcesId);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ins, null, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcHeight > height || srcWidth > width) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / height);
            } else {
                inSampleSize = Math.round(srcWidth / width);
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(ins, null, options);
    }

    public static Bitmap compressImage(Bitmap image) {
        if (image == null) {
            return null;
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] bytes = baos.toByteArray();
            ByteArrayInputStream isBm = new ByteArrayInputStream(bytes);
            return BitmapFactory.decodeStream(isBm);
        } catch (Exception e) {
            //
        }
        return null;
    }

    /**
     * 2017.3.5 星期日  22:41
     */
    public static String asYWT(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //星期 this will for example return 2 for tuesday
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //24小时制小时数
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return year + "." + month + "." + day + " " + getWeek((dayOfWeek - 1) % 7) + " " + hour + ":" + (minute < 10 ? "0" + minute : minute);
    }

    public static String getMonth(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "";
        }
    }

    private static String getWeek(int dayInWeek) {
        switch (dayInWeek) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            default:
                return "";
        }
    }

    public static int dayOfWeek(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        //星期 this will for example return 2 for tuesday
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return ((dayOfWeek - 1) % 7);
    }

    public static int dayOfMonth(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int month(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        int month = calendar.get(Calendar.MONTH);
        return month + 1;
    }

    public static int getHours(int timestampInSec) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampInSec * 1000L);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


    public static String getFormatTimeStr(int time) {
        String timeStr;
        if (time < 10) {
            timeStr = "0" + time;
        } else {
            timeStr = time + "";
        }
        return timeStr;
    }

    public static boolean is24HourFormat(Context context) {
        if (context == null) {
            return false;
        }
        String value = Settings.System.getString(context.getContentResolver(),
                Settings.System.TIME_12_24);

        if (value == null) {
            Locale locale = context.getResources().getConfiguration().locale;

            java.text.DateFormat natural =
                    java.text.DateFormat.getTimeInstance(java.text.DateFormat.LONG, locale);

            if (natural instanceof SimpleDateFormat) {
                SimpleDateFormat sdf = (SimpleDateFormat) natural;
                String pattern = sdf.toPattern();

                if (pattern.indexOf('H') >= 0) {
                    value = "24";
                } else {
                    value = "12";
                }
            } else {
                value = "12";
            }
        }

        return value.equals("24");
    }


    public static String getUniqueId(Context context) {
        @SuppressLint("HardwareIds")
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            return SLFMD5Util.encode(androidID);
        } catch (Exception e) {
            return androidID;
        }
    }

    private static boolean isNavigationBarShow(Display display) {
        Point size = new Point();
        Point realSize = new Point();
        display.getSize(size);
        display.getRealSize(realSize);
        return realSize.y != size.y;
    }

    public static int getNavigationBarHeight(Activity activity, Display display) {
        if (!isNavigationBarShow(display)) {
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = 0;
        try {
            height = resources.getDimensionPixelSize(resourceId);
        } catch (Resources.NotFoundException e) {
            SLFLogUtil.e(TAG, Log.getStackTraceString(e));
        }
        return height;
    }

    public static int getLocalTimeZoneInMinutes() {
        return getLocalTimeZoneInSec() / 60;
    }

    public static int getLocalTimeZoneInHours() {
        return getLocalTimeZoneInSec() / 3600;
    }

    public static int getLocalTimeZoneInSec() {
        int daylightSaving = (TimeZone.getDefault().useDaylightTime() && TimeZone.getDefault().inDaylightTime(new Date())) ? TimeZone.getDefault().getDSTSavings() : 0;
        int timezone = -7 * 3600;
        try {
            timezone = (TimeZone.getDefault().getRawOffset() + daylightSaving) / 1000;
        } catch (Exception e) {
            SLFLogUtil.i(TAG, "getLocalTimeZoneInSec exception=" + e.getMessage());
        }
        return timezone;
    }

    public static void clearNotification(Context applicationContext, Intent intent) {
        if (intent.getBooleanExtra("clear_notification", false)) {
            NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.cancelAll();
            }
        }
    }

    // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
    public static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
        Bitmap dst = null;
        if (src != null) {
            dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        }
        return dst;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 显示虚拟按键
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void visibleBottomUIMenu(Activity activity) {
        //虚拟按键
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.VISIBLE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 通过反射，获取包含虚拟键的整体屏幕高度
     *
     * @return int
     */
    public static int getHasVirtualKeyHeight(Activity activity) {
        int dpi = 0;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class<?> c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            //
        }
        return dpi;
    }

    /**
     * 通过反射，获取包含虚拟键的整体屏幕宽度
     *
     * @return int
     */
    public static int getHasVirtualKeyWidth(Activity activity) {
        int dpi = 0;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class<?> c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.widthPixels;
        } catch (Exception e) {
            //
        }
        return dpi;
    }

    /**
     * 通过反射，获取包含虚拟键的整体屏幕高度
     *
     * @return string
     */
    public static String getLocalLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale.getLanguage().concat("-").concat(locale.getCountry());
    }

    /**
     * 判断邮箱是否正确
     *
     * @param email 邮箱
     * @return boolean
     */
    public static boolean isEmail(String email) {
        String pattern1 = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        return mat.find();
    }

    /**
     * 检查网络是否可用
     *
     * @return 是否
     */
    @SuppressWarnings("deprecation")
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager manager = null;
        try {
            manager = (ConnectivityManager) activity
                    .getApplicationContext().getSystemService(
                            Context.CONNECTIVITY_SERVICE);
        } catch (Exception e) {
            SLFLogUtil.i(TAG, "isNetworkAvailable  error" + e.getMessage());
        }
        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        return networkinfo != null && networkinfo.isAvailable();

    }

    /**
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str str
     * @return Boolean
     */
    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 判断字符串是不是double型
     *
     * @param str String
     * @return boolean
     */
    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("[0-9]+[.]{0,1}[0-9]*[dD]{0,1}");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 去掉字符串中的特殊字符
     */
    public static String deleteSpecialChar(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？_\\-]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 去掉字符串中的特殊字符
     */
    public static String formatRouterPath(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？_\\-]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 判断字符串中是否包含特殊字符
     */

    public static boolean isContainSpecialChar(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？_]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 判断字符串中是否合法(只允许输入数字，字母，下划线和空格)
     */
    public static boolean isLegalChar(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }

        String regEx = "[_0-9a-zA-Z\\s\u4E00-\u9FA5]+";

        return str.matches(regEx);
    }

//    /**
//     * BLE设备固件包文库下载后本地存储目录
//     */
//
//    public static File getBLEDownloadDirectory() {
//        return new File(Config.firmwareDownloadPath);
//    }

    /**
     * 将给定bytes数组转成可打印的Hex字符串
     *
     * @param input     Bytes数组
     * @param start     从哪个位置开始？
     * @param end       到哪个位置结束？
     * @param upperCase 是否使用大写字母？
     * @return 如果input为空或start不小于end，返回""，否则返回转换后的字符串
     */
    public static String toHexString(byte[] input, int start, int end, boolean upperCase) {
        if (input == null || start >= end || input.length == 0 || start >= input.length) {
            return "";
        }
        StringBuilder sb = new StringBuilder(input.length << 1);
        return toHexString(sb, input, start, end, upperCase ? 'A' : 'a').toString();
    }

    /**
     * 等同于toHexString(input, 0, input.length, upperCase)
     *
     * @see #toHexString(byte[], int, int, boolean)
     */
    public static String toHexString(byte[] input, boolean upperCase) {
        if (input == null || input.length == 0) {
            return "";
        }
        return toHexString(input, 0, input.length, upperCase);
    }

    private static StringBuilder toHexString(StringBuilder sb, byte[] input, int start, int end, char a) {
        for (int i = start; i < end; ++i) {
            byte b = input[i];
            sb.append(halfByteToChar((b >> 4) & 0xf, a));
            sb.append(halfByteToChar(b & 0xf, a));
        }
        return sb;
    }

    /**
     * 辅助函数：将一个不超过15的值转成16进制表示
     *
     * @param halfByte 值不大于15的无符号值
     * @param a        字符A的ASCII码（如果要换成小写，就用'a'，要换成大写，就用'A'）
     * @return '0'~'9'或'A'~'F'（视参数a是大写还是小写）
     */
    private static char halfByteToChar(int halfByte, int a) {
        if (halfByte < 10) {
            return (char) ('0' + halfByte);
        } else {
            return (char) (a + (halfByte - 10));
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    @SuppressWarnings("java:S4042")
    public static boolean deleteDir(@NonNull File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            if (children != null && children.length > 0) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * Json字符串转Class
     * @param strJsonData
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T JsonToClass(String strJsonData, Class<T> cls)
    {
        Gson gson = new Gson();
        T t = gson.fromJson(strJsonData, cls);

        // 空字符串转换

        return t;
    }

    /**
     * 生成16位不重复的随机数，含数字+大小写
     */
    public static String getGUID() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type) {
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char) (rd.nextInt(25) + 65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char) (rd.nextInt(25) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    /**
     * 加载icon图片
     *
     * @param context     一般应是activity
     * @param deviceModel 设备的产品类型
     * @param ivIcon      设置图片的imageview"WYZEC1-JZ"
     */
    @SuppressWarnings("java:S1172")
    public static void setRotateIcon(@NonNull Context context, @Nullable String deviceModel, @NonNull ImageView ivIcon) {
        //empty
    }

    /**
     * 加载icon图片
     *
     * @param context     一般应是activity
     * @param deviceModel 设备的产品类型
     * @param ivIcon      设置图片的imageview
     */
    @SuppressWarnings("java:S1172")
    public static void setIcon(@NonNull Context context, @NonNull String deviceModel, @NonNull ImageView ivIcon) {
        //empty
    }

    @SuppressWarnings("java:S1172")
    public static void jumpToWhatsNew(@NonNull Context context, @NonNull String deviceModel) {
        //empty
    }
}
