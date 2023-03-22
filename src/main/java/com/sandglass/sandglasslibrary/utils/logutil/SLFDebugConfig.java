package com.sandglass.sandglasslibrary.utils.logutil;

import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.utils.SLFFileUtils;

/**
 * Greated by yangjie
 * describe:log工具类
 * time:2022/12/10
 */
public class SLFDebugConfig {
    private SLFDebugConfig() {
    }

    public static final String ROOT_PATH = SLFConstants.slfCachePath + "debugLog";
    public static final String CRASH = "Crash";
    public static final String NETWORK = "Network";
    public static final String EXCEPTION = "Exception";
    public static final String INFO = "Info";

    private static boolean openLogEnable = false;//功能开关, 是否可以写

    public static boolean isOpenLogEnable() {
        return openLogEnable;
    }

    public static void setOpenLogEnable(boolean openLogEnable) {
        SLFLogUtil.sdki("SLFDebugConfig", "LogEnable: " + openLogEnable);
        SLFDebugConfig.openLogEnable = openLogEnable;
    }

    /**
     * 删除所有log
     *
     * @return 是否删除成功
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean deleteAllLog() {
        if (!SLFDebugConfig.isOpenLogEnable()) {
            return false;
        }
        return   SLFFileUtils.deleteFiles(ROOT_PATH);
    }

    /**
     * 根据log路径删除
     *
     * @return 是否删除成功
     */
    @SuppressWarnings("UnusedReturnValue")
    public static boolean deleteLog(String path) {
        if (!SLFDebugConfig.isOpenLogEnable()) {
            return false;
        }
        return SLFFileUtils.deleteFiles(path);
    }

}
