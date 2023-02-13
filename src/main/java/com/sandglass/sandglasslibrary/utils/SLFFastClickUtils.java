package com.sandglass.sandglasslibrary.utils;

public class SLFFastClickUtils {

    /**
     * 是否快速双击
     *
     * @return true 快速双击
     */
    private static long lastUpTime = 0;

    public static boolean isFastDoubleClick(int interval) {
        if (System.currentTimeMillis() - lastUpTime >= interval || lastUpTime > System.currentTimeMillis()) {
            lastUpTime = System.currentTimeMillis();
            return false;
        } else {
            return true;
        }
    }
}
