package com.wyze.sandglasslibrary.utils;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.wyze.sandglasslibrary.base.SLFBaseApplication;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * created by yangjie
 * describe:安卓转换工具类
 * time: 2022/12/7
 */
public class SLFConvertUtil {

    private SLFConvertUtil(){}
    /**
     * Bytes to bits.
     *
     * @param bytes The bytes.
     * @return bits
     */
    public static String bytes2Bits(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {return "";}
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            for (int j = 7; j >= 0; --j) {
                sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }

    /**
     * Bits to bytes.
     *
     * @param bits The bits.
     * @return bytes
     */
    public static byte[] bits2Bytes(String bits) {
        int lenMod = bits.length() % 8;
        int byteLen = bits.length() / 8;
        // add "0" until length to 8 times
        if (lenMod != 0) {
            StringBuilder bitsBuilder = new StringBuilder(bits);
            for (int i = lenMod; i < 8; i++) {
                bitsBuilder.insert(0, "0");
            }
            bits = bitsBuilder.toString();
            byteLen++;
        }
        byte[] bytes = new byte[byteLen];
        for (int i = 0; i < byteLen; ++i) {
            for (int j = 0; j < 8; ++j) {
                bytes[i] <<= 1;
                bytes[i] |= bits.charAt(i * 8 + j) - '0';
            }
        }
        return bytes;
    }

    /**
     * Bytes to chars.
     *
     * @param bytes The bytes.
     * @return chars
     */
    public static char[] bytes2Chars(final byte[] bytes) {
        if (bytes == null) {return new char[0];}
        int len = bytes.length;
        if (len <= 0) {return new char[0];}
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (bytes[i] & 0xff);
        }
        return chars;
    }

    /**
     * Chars to bytes.
     *
     * @param chars The chars.
     * @return bytes
     */
    public static byte[] chars2Bytes(final char[] chars) {
        if (chars == null || chars.length <= 0) {return new byte[0];}
        int len = chars.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (chars[i]);
        }
        return bytes;
    }

    /**
     * 将文件转换成byte数组
     *
     * @param tradeFile tradeFile
     * @return byte[]
     */
    public static byte[] file2Bytes(File tradeFile) {
        byte[] buffer = null;
        try {
            ByteArrayOutputStream bos;
            try (FileInputStream fis = new FileInputStream(tradeFile)) {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
            }
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            Log.e("file2Bytes",e.getMessage());
        }
        return buffer;
    }

    /**
     * 将文件转换成byte数组
     *
     * @param filePath path
     * @return byte[]
     */
    public static byte[] file2Bytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            ByteArrayOutputStream bos;
            try (FileInputStream fis = new FileInputStream(file)) {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
            }
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            //empty code
        }
        return buffer;
    }

    /**
     * 将byte数组转换成文件
     *
     * @param filePath filePath
     */
    public static void bytes2File(byte[] buf, String filePath, String fileName) {
        File dir = new File(filePath);
        if (!dir.exists() && dir.isDirectory()) {
            boolean isMkdirs = dir.mkdirs();
            Log.e("isMkdirs", isMkdirs + "");
        }
        File file = new File(filePath + File.separator + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (IOException e) {
            //empty code
        }

    }

    /**
     * Bytes to hex string.
     * <p>e.g. bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns "00A8"</p>
     *
     * @param bytes The bytes.
     * @return hex string
     */
    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) {return "";}
        StringBuilder result = new StringBuilder();
        for (int index = 0, len = bytes.length; index <= len - 1; index += 1) {
            int char1 = ((bytes[index] >> 4) & 0xF);
            char chara1 = Character.forDigit(char1, 16);
            int char2 = ((bytes[index]) & 0xF);
            char chara2 = Character.forDigit(char2, 16);
            result.append(chara1);
            result.append(chara2);
        }
        return result.toString();
    }

    /**
     * Hex string to bytes.
     * <p>e.g. hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }</p>
     *
     * @param hexString The hex string.
     * @return the bytes
     */
    public static byte[] hexString2Bytes(String hexString) {
        if (isSpace(hexString)) {return new byte[0];}
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Int(hexBytes[i]) << 4 | hex2Int(hexBytes[i + 1]));
        }
        return ret;
    }

    private static int hex2Int(final char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Milliseconds to fit time span.
     *
     * @param millis    The milliseconds.
     *                  <p>millis &lt;= 0, return null</p>
     * @param precision The precision of time span.
     *                  <ul>
     *                  <li>precision = 0, return null</li>
     *                  <li>precision = 1, return 天</li>
     *                  <li>precision = 2, return 天, 小时</li>
     *                  <li>precision = 3, return 天, 小时, 分钟</li>
     *                  <li>precision = 4, return 天, 小时, 分钟, 秒</li>
     *                  <li>precision &gt;= 5，return 天, 小时, 分钟, 秒, 毫秒</li>
     *                  </ul>
     * @return fit time span
     */
    @SuppressLint("DefaultLocale")
    public static String millis2FitTimeSpan(long millis, int precision) {
        if (millis <= 0 || precision <= 0) {return null;}
        StringBuilder sb = new StringBuilder();
        String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
        int[] unitLen = {86400000, 3600000, 60000, 1000, 1};
        precision = Math.min(precision, 5);
        for (int i = 0; i < precision; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= mode * unitLen[i];
                sb.append(mode).append(units[i]);
            }
        }
        return sb.toString();
    }


    /**
     * Bytes to input stream.
     *
     * @param bytes The bytes.
     * @return input stream
     */
    public static InputStream bytes2InputStream(final byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {return null;}
        return new ByteArrayInputStream(bytes);
    }

    /**
     * Output stream to bytes.
     *
     * @param out The output stream.
     * @return bytes
     */
    public static byte[] outputStream2Bytes(final OutputStream out) {
        if (out == null) {return new byte[0];}
        return ((ByteArrayOutputStream) out).toByteArray();
    }

    /**
     * Bytes to output stream.
     *
     * @param bytes The bytes.
     * @return output stream
     */
    public static OutputStream bytes2OutputStream(final byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {return null;}
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            os.write(bytes);
            return os;
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * Bitmap to bytes.
     *
     * @param bitmap The bitmap.
     * @param format The format of bitmap.
     * @return bytes
     */
    public static byte[] bitmap2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format) {
        if (bitmap == null){ return new byte[0];}
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bytes to bitmap.
     *
     * @param bytes The bytes.
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length == 0)
                ? null
                : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Drawable to bitmap.
     *
     * @param drawable The drawable.
     * @return bitmap
     */
    @SuppressWarnings("deprecation")
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE
                            ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap to drawable.
     *
     * @param bitmap The bitmap.
     * @return drawable
     */
    public static Drawable bitmap2Drawable(final Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(SLFBaseApplication.getAppContext().getResources(), bitmap);
    }

    /**
     * Drawable to bytes.
     *
     * @param drawable The drawable.
     * @param format   The format of bitmap.
     * @return bytes
     */
    public static byte[] drawable2Bytes(final Drawable drawable,
                                        final Bitmap.CompressFormat format) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    /**
     * Bytes to drawable.
     *
     * @param bytes The bytes.
     * @return drawable
     */
    public static Drawable bytes2Drawable(final byte[] bytes) {
        return bytes == null ? null : bitmap2Drawable(bytes2Bitmap(bytes));
    }

    /**
     * View to bitmap.
     *
     * @param view The view.
     * @return bitmap
     */
    public static Bitmap view2Bitmap(final View view) {
        if (view == null) {return null;}
        Bitmap ret =
                Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }

    /**
     * Value of dp to value of px.
     *
     * @param dpValue The value of dp.
     * @return value of px
     */
    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Value of px to value of dp.
     *
     * @param pxValue The value of px.
     * @return value of dp
     */
    public static int px2dp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Value of sp to value of px.
     *
     * @param spValue The value of sp.
     * @return value of px
     */
    public static int sp2px(final float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * Value of px to value of sp.
     *
     * @param pxValue The value of px.
     * @return value of sp
     */
    public static int px2sp(final float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    private static boolean isSpace(final String s) {
        if (s == null) {return true;}
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 开尔文温度转摄氏度 K=℃+273.15
     * @param kelvin 开尔文温度
     * @return 摄氏度
     */
    public static double kelvinToCelsius(double kelvin){
        return kelvin-273.15;
    }

    /**
     * 摄氏度转开尔文温度 K=℃+273.15
     * @param celsius 摄氏度
     * @return 开尔文温度
     */
    public static double celsiusToKelvin(double celsius){
        return celsius+273.15;
    }

    /**
     * 开尔文温度转华氏度
     * @param kelvin 开尔文温度
     * @return 华氏度
     */
    public static int kelvinToFahrenheit(double kelvin){
        return (int)(((kelvin-273.15)*9/5)+32);
    }

    /**
     * 华氏度转开尔文温度
     * @param fahrenheit 华氏度
     * @return 开尔文温度
     */
    public static double fahrenheitToKelvin(int fahrenheit){
        return ((double)fahrenheit-32)*5/9.0+273.15;
    }

    /*
     * 摄氏度转华氏度 F=(℃×9/5)+32
     * @param celsius 摄氏度
     * @return 华氏度
     */
    public static int celsiusToFahrenheit(double celsius){
        return (int)((celsius*9/5)+32);
    }

    /**
     * 华氏度转摄氏度  ℃=(F-32)×5/9
     * @param fahrenheit 华氏度
     * @return 摄氏度
     */
    public static double fahrenheitToCelsius(double fahrenheit){
        return (fahrenheit-32)*(5/9.0);
    }

    /**
     * 千克转磅 kilogram to pound
     * @param kilogram 千克
     * @return 磅
     */
    public static double kilogramToPound(double kilogram){
        return 2.2046226*kilogram;
    }

    /**
     * 磅转千克 pound to kilogram
     * @param pound 千克
     * @return 千克
     */
    public static double poundToKilogram(double pound){
        return pound/2.2046226;
    }

    /**
     * 千克转盎司 kilogram to ounce
     * @param kilogram 千克
     * @return 盎司
     */
    public static double kilogramToOunce(double kilogram){
        return 35.2739619*kilogram;
    }

    /**
     * 盎司转千克 pound to kilogram
     * @param ounce 盎司
     * @return 千克
     */
    public static double ounceToKilogram(double ounce){
        return ounce/35.2739619;
    }

    /**
     * 磅转盎司 pound to ounce
     * @param pound 磅
     * @return 盎司
     */
    public static double poundToOunce(double pound){
        return 16*pound;
    }

    /**
     * 盎司转磅 ounce to pound
     * @param ounce 盎司
     * @return 磅
     */
    public static double ounceToPound(double ounce){
        return ounce/16.0;
    }


    /**
     * 英尺转英寸 foot To Inch
     * @param foot 英寸
     * @return 英寸
     */
    public static double footToInch(double foot){
        return 12*foot;
    }

    /**
     * 英寸转英尺 inch to foot
     * @param inch 英寸
     * @return 英尺
     */
    public static double inchToFoot(double inch){
        return inch/12.0;
    }

    /**
     * 英寸转厘米 inch to cm
     * @param inch 英尺
     * @return 厘米
     */
    public static double inchToCentimeter(double inch){
        return 2.54*inch;
    }

    /**
     * 厘米转英寸 cm to inch
     * @param centimeter 厘米
     * @return 英寸
     */
    public static double centimeterToInch(double centimeter){
        return centimeter/2.54;
    }

    /**
     * 英尺转厘米 feet to cm
     * @param feet 英尺
     * @return 厘米
     */
    public static double feetToCentimeter(double feet){
        return 304.8*feet;
    }

    /**
     * 厘米转英尺 cm to inch
     * @param centimeter 厘米
     * @return 英尺
     */
    public static double centimeterToFeet(double centimeter){
        return centimeter/304.8;
    }

    /**
     * 米/秒 转 千米/时  MeterSecond To KilometerHour
     * @param meterSecond 米/秒
     * @return 千米/时
     */
    public static double meterSecondToKilometerHour(double meterSecond){
        return 3.6*meterSecond;
    }

    /**
     * 千米/时 转 米/秒 KilometerHourToMeterSecon
     * @param kilometerHour 千米/时
     * @return 米/秒
     */
    public static double kilometerHourToMeterSecond(double kilometerHour){
        return kilometerHour/3.6;
    }

    /**
     * 千米/时 转 英里/时 KilometerHour To MileHour
     * @param kilometerHour 千米/时
     * @return 英里/时
     */
    public static double kilometerHourToMileHour(double kilometerHour){
        return kilometerHour/1.609344;
    }

    /**
     * 英里/时 转 千米/时
     * @param mileHour 英里/时
     * @return 千米/时
     */
    public static double mileHourToKilometerHour(double mileHour) {
        return mileHour*1.609344;
    }

    /**
     * 英里/时 转 英寸/秒
     * @param mileHour 英里/时
     * @return 英寸/秒
     */
    public static double mileHourToInchSecond(double mileHour){
        return mileHour*17.6000001;
    }

    /**
     * 英寸/秒 转 英里/时
     * @param inchSecond 英寸/秒
     * @return 英里/时
     */
    public static double inchSecondToMileHour(double inchSecond) {
        return inchSecond/17.6000001;
    }


    /**
     * 角度转换弧度
     * @param degrees 角度
     * @return 弧度
     */
    public static double degreesToRadian(double degrees){
        return (Math.PI * (degrees) / 180.0);
    }

    /**
     * 弧度转换角度
     * @param radian 弧度
     * @return 角度
     */
    public static double radianToDegrees(double radian){
        return (radian*180.0)/(Math.PI);
    }

    /**
     * 米转英里 m to mile
     * @param meter 米
     * @return 英里
     */
    public static double meterToMile(double meter){
        return (meter/1000.0) * (1.0/1.609344);
    }

    /**
     * 英里转米 mile to m
     * @param mile 米
     * @return 英里
     */
    public static double mileToMeter(double mile){
        return mile * 1609.344D;
    }

    /**
     * 米转英尺 m to mile
     * @param meter 米
     * @return 英里
     */
    public static double meterToFt(double meter){
        return meter * 3.2808399D;
    }

    /**
     * 英尺转米 m to mile
     * @param ft 米
     * @return 英里
     */
    public static double ftToMeter(double ft){
        return ft * 0.3048D;
    }

    /**
     * 英尺转英里 m to mile
     * @param ft 米
     * @return 英里
     */
    public static double ftToMile(double ft){
        return ft / 5280D;
    }

    /**
     * 英里转英尺  mile to ft
     * @param mile 英里
     * @return ft
     */
    public static double mileToFt(double mile){
        return mile * 5280D;
    }
}
