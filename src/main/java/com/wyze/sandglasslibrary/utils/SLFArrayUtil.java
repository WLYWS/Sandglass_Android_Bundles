package com.wyze.sandglasslibrary.utils;

/**数组操作类
 * Created by wangjian on 2022/12/16
 */
public class SLFArrayUtil {

    /**
     * 合并两个byte[]数组
     * @param firstArray
     * @param secondArray
     * @return
     */
    public static byte[] concat(byte[] firstArray, byte[] secondArray) {
        if (firstArray == null || secondArray == null) {
            return null;
        }
        byte[] bytes = new byte[firstArray.length + secondArray.length];
        System.arraycopy(firstArray, 0, bytes, 0, firstArray.length);
        System.arraycopy(secondArray, 0, bytes, firstArray.length,
                secondArray.length);
        return bytes;
    }
}
