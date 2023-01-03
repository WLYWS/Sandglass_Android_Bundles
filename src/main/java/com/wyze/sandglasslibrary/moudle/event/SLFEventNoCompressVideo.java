package com.wyze.sandglasslibrary.moudle.event;

import com.wyze.sandglasslibrary.moudle.SLFMediaData;

/**
 * Greated by yangjie
 * describe:视频压缩失败完成通知
 * time:2023/1/3
 */
public class SLFEventNoCompressVideo {

    public String path;

    public String filename;

    public SLFMediaData slfMediaData;

    public SLFEventNoCompressVideo(String path, String filename, SLFMediaData slfMediaData){
        this.path = path;
        this.filename = filename;
        this.slfMediaData = slfMediaData;
    }
}
