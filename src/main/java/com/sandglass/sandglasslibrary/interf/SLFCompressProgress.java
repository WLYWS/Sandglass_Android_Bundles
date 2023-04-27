package com.sandglass.sandglasslibrary.interf;

/**
 * Greated by yangjie
 * describe:回调视频压缩进度
 * time:2023/4/25
 */
public interface SLFCompressProgress {
    void getCompressProgress(long id,long percent);
}
