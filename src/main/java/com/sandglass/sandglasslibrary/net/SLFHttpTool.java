package com.sandglass.sandglasslibrary.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import android.util.Base64;

import com.google.gson.GsonBuilder;
import com.sandglass.sandglasslibrary.utils.SLFStringUtil;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by wangjian on 2022/12/5
 */
public class SLFHttpTool {

    private static final String TAG = "SLFHttpTool";
    private static String secret;
    private static final String CIP_ALGORITHM = "AES/ECB/PKCS5Padding";
    public static final String CHARSET_UTF8 = "UTF-8";
    private static String nonce;

    /**
     * 生成签名
     * @param method
     * @param url
     * @param params
     * @param ts
     * @return
     */
    public static String generateSign(String method, String url, Map <String, Object> params, String ts){
        String sha256SignStr = "";
        String signatureStr = "";
        if (method.equalsIgnoreCase(SLFHttpRequestConstants.REQUEST_METHOD_GET)&&params==null||method.equalsIgnoreCase(SLFHttpRequestConstants.REQUEST_METHOD_POST)&&params==null){
            signatureStr =method+"&"+url+"&"+"app_id="+SLFHttpRequestConstants.APP_ID+"&"+ secret+"&"+ts;
        }else if (method.equalsIgnoreCase(SLFHttpRequestConstants.REQUEST_METHOD_GET)&&params!=null){
            signatureStr =method+"&"+generateParamsGetUrl(url,params)+"&"+"app_id="+SLFHttpRequestConstants.APP_ID+"&"+secret+"&"+ts;
        }else if (method.equalsIgnoreCase(SLFHttpRequestConstants.REQUEST_METHOD_POST)&&params!=null){
            signatureStr =method+"&"+url+"&"+"app_id="+SLFHttpRequestConstants.APP_ID+"&"+"data="+SLFStringUtil.replaceBlank(encryptData(params))+"&"+secret+"&"+ts;
        }
        if ("".equals(signatureStr)){
            return "";
        }
        try {
            String signatureBase64 = Base64.encodeToString(signatureStr.getBytes(CHARSET_UTF8),Base64.DEFAULT);
            sha256SignStr = getSha256Str(signatureBase64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha256SignStr;
    }

    /**
     * 通过map传递参数的get的请求需要拼接url进行传递
     * @param url
     * @param params
     * @return
     */
    private static String generateParamsGetUrl (String url, Map<String, Object> params) {
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuilder sb = new StringBuilder();
        sb.append(url+"?");
        for (int i=0;i<keys.length;i++){
            String key = keys[i];
            Object value = params.get(key);
            sb.append(key);
            sb.append("=");
            sb.append(value);
            if (i<keys.length-1){
                sb.append("&");
            }
        }
        SLFLogUtil.d("request","Get 带参加密前请求url：| Request:"+sb.toString());
        return sb.toString();
    }

    /**
     * 使用aes-256加密对携带参数进行加密
     * @param params post携带的参数
     * @return
     */
    private static String encryptData (Map <String, Object> params) {
        String data = "";
        try {
            String json = new GsonBuilder().create().toJson(params);
            byte[] key = secret.getBytes(CHARSET_UTF8);
            String mSecret = encryptMd532(key);
            data = encryptAES(json,mSecret);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * aes加密
     * @param sSrc
     * @param sKey
     * @return
     */
    public static String encryptAES(String sSrc, String sKey){
        String encryptDta = "";
        try {
            byte[] data = sSrc.getBytes(CHARSET_UTF8);
            byte[] encrypted = encrypt(data, sKey);
            encryptDta = Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptDta;
    }

    public static byte[] encrypt(byte[] data, String key) {
        //不足16字节，补齐内容为差值
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(CHARSET_UTF8), "AES");
            Cipher cipher = Cipher.getInstance(CIP_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[] {};
    }

    /**
     *md5生成32个字节
     * @param plainText
     *            明文
     * @return 32位密文
     */
    public static String encryptMd532(byte[] plainText) {
        String md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText);
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            md5 = buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    //生成请求需要的secret
    public static String generateSecret(){
        String sha256Str = "";
        try {
            String nonceStr = nonce;
            byte[] keyStr = SLFHttpRequestConstants.APP_KEY.getBytes(CHARSET_UTF8);
            byte[] nonceBuffer = Base64.decode(nonceStr,Base64.DEFAULT);
            byte[] key_nonceBuffer = new byte[keyStr.length+nonceBuffer.length];
            System.arraycopy(keyStr, 0, key_nonceBuffer, 0, keyStr.length);
            System.arraycopy(nonceBuffer, 0, key_nonceBuffer, keyStr.length, nonceBuffer.length);
            String key_nonceBase64 = Base64.encodeToString(key_nonceBuffer,Base64.DEFAULT);
            StringBuilder keys = new StringBuilder();
            sha256Str = getSha256Str(key_nonceBase64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha256Str;
    }

    /**
     * sha1加密
     *
     * @param str 要加密的字符串
     * @return 加密后的字符串
     */
    public static String getSha1Str(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(Base64.decode(str,Base64.DEFAULT));
            encodeStr = Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }
    /**
     * sha256加密
     *
     * @param str 要加密的字符串
     * @return 加密后的字符串
     */
    public static String getSha256Str(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(Base64.decode(str,Base64.DEFAULT));
            encodeStr = Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1){
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    //生成请求需要的随机数
    public static String generateNonce(){
        long nowMin = System.currentTimeMillis()/1000/60;//10位时间戳除以60
        String nowMinStr = longToHexString(nowMin,8);//nowMin转16进制，不足8位左补0
        String R1Str = String.valueOf(new Double(nowMin*slfRandom()).intValue());
        String R2Str = String.valueOf(new Double(nowMin*slfRandom()).intValue());
        StringBuffer sb = new StringBuffer();
        sb.append(R1Str);
        sb.append(R2Str);
        sb.append(nowMinStr);
        String nonceStr = sb.toString();
        List <Integer> nonceList = hexToIntArray(nonceStr);
        Integer[] nonceArray = nonceList.toArray(new Integer[nonceList.size()]);
        String nonceBase64 = intArrToBase64(nonceArray);
        return nonceBase64;
    }

    //将十进制数组转化为base64
    public static String intArrToBase64(Integer[] intArray){
        byte[] bytes = new byte[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            int value = intArray[i];
            bytes[i] = (byte)value;
        }
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    //将24位16进制字符串两两分割转化为12位的10进制数组
    public static ArrayList <Integer> hexToIntArray(String hexStr){
        ArrayList<Integer> intArray = new ArrayList();
        ArrayList<String> hexArray = new ArrayList();
        if ("".equals(hexStr)||hexStr.length()==0){
            return intArray;
        }
        for (int i=0; i<=hexStr.length()-2; i=i+2){
            hexArray.add(hexStr.substring(i,i+2));
        }
        for (int j=0; j<hexArray.size(); j++){
            intArray.add(Integer.parseInt(hexArray.get(j),16));
        }
        return intArray;
    }

    //10进制转16进制，不足n位左补0
    public static String longToHexString(long num,int numLength){
        String hexStr = Long.toHexString(num);
        int strLen =hexStr.length();
        if (strLen <numLength) {
            while (strLen< numLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(hexStr);//左补0
                hexStr= sb.toString();
                strLen= hexStr.length();
            }
        }
        return hexStr;
    }

    //生成0-1之间保留两位小数的随机数
    public static double slfRandom(){
        double ran=Math.random();
        DecimalFormat df = new DecimalFormat( "0.00" );
        String str=df.format( ran );
        return Double.parseDouble(str);
    }

    public static TreeMap getTreeCrc(String method,String url,TreeMap maps) {
        TreeMap map = new TreeMap();
        try {
            nonce = SLFStringUtil.replaceBlank(generateNonce());
            secret = SLFStringUtil.replaceBlank(generateSecret());
            long ts = System.currentTimeMillis();
            map.put("appId", SLFHttpRequestConstants.APP_ID);
            map.put("signature", SLFStringUtil.replaceBlank(generateSign(method, url, maps,String.valueOf(ts))));
            map.put("nonce", nonce);
            map.put("ts",String.valueOf(ts));
            map.put("Authorization", "abc");
            map.put("secret",encryptMd532(secret.getBytes(CHARSET_UTF8)));
            SLFLogUtil.d("request", "Get request 加密：| 签名: signature="+SLFStringUtil.replaceBlank(generateSign(method, url, maps,String.valueOf(ts))));
            SLFLogUtil.d("request", "Get request 加密：| 密钥: secret="+encryptMd532(secret.getBytes(CHARSET_UTF8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 时间戳转换
    private static String timeStamp2Date() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//设置TimeZone为上海时间
        Date now = new Date();//获取本地时间
        try {
            now = sdf.parse(sdf.format(now));//将本地时间转换为转换时间为东八区
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(now);
    }

    // 检测是否有网络
    @SuppressLint("MissingPermission")
    public static boolean hasNetwork(Context mContext) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return true;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }


    // 将文件进行SHA1加密
    public static String getFileContent(File file) {
        try {
            StringBuffer sb = new StringBuffer();
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            FileInputStream fin = new FileInputStream(file);
            int len = -1;
            byte[] buffer = new byte[1024];//设置输入流的缓存大小 字节
            //将整个文件全部读入到加密器中
            while ((len = fin.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            //对读入的数据进行加密
            byte[] bytes = digest.digest();
            for (byte b : bytes) {
                // 数byte 类型转换为无符号的整数
                int n = b & 0XFF;
                // 将整数转换为16进制
                String s = Integer.toHexString(n);
                // 如果16进制字符串是一位，那么前面补0
                if (s.length() == 1) {
                    sb.append("0" + s);
                } else {
                    sb.append(s);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 2 * 判断字符串是否可以转化为json对象
     * 3 * @param content
     * 4 * @return
     * 5
     */
    public static boolean isJsonObject(String content) {
        if (content == null || TextUtils.isEmpty(content))
            return false;
        try {
            JSONObject jsonObject = new JSONObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
