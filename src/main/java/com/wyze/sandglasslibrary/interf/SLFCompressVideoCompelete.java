package com.wyze.sandglasslibrary.interf;

import com.wyze.sandglasslibrary.moudle.SLFMediaData;

/**
 * Greated by yangjie
 * describe:视频压缩完成回调
 * time：2023/1/3
 *
 */
public interface SLFCompressVideoCompelete {
        void isCompelete(String path, String fileName, SLFMediaData slfMediaData);
    }