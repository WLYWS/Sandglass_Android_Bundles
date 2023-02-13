package com.sandglass.sandglasslibrary.interf;

/**
 * Greated by yangjie
 * describe:上传完成的回调接口，提供给app
 * time:2022/12/28
 */
public interface SLFUploadCompleteCallback {
    void  isUploadComplete(boolean isComplete,String appFileName,String firmwarFileName);
}
