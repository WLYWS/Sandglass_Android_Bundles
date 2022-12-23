package com.wyze.sandglasslibrary.interf;

/**
 * Greated by yangjie
 * describe:log读取成功失败的接口
 * time"2022/12/10
 */
public interface SLFDebugLogReadCallback {

    void onReadSuccess(String logFilePath, String fileContent);

    void onReadFailed(String logFilePath);
}