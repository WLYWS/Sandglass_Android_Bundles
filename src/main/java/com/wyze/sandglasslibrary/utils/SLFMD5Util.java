package com.wyze.sandglasslibrary.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class SLFMD5Util {

    private SLFMD5Util(){}

    public static String encode(String str) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = md5Byte & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    // 计算文件的 MD5 值
    public static String getFileMD5(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        byte[] buffer = new byte[8192];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            try (FileInputStream in = new FileInputStream(file)) {
                while ((len = in.read(buffer)) != -1) {
                    md5.update(buffer, 0, len);
                }
            }
            byte[] bytes = md5.digest();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
        } catch (Exception e) {
            //empty code
        }
        return result.toString();
    }


}
