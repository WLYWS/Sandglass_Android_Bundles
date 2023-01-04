package com.wyze.sandglasslibrary.moudle.event;

import com.wyze.sandglasslibrary.moudle.SLFMediaData;

/**
 * Greated by yangjie
 * describe:视频压缩完成通知
 * time:2023/1/3
 */
public class SLFEventCompressVideo {
    public boolean isCompelte;

    public String path;

    public String filename;

    public SLFMediaData slfMediaData;

    public SLFEventCompressVideo(boolean isCompelte,String path,String filename,SLFMediaData slfMediaData){
        this.isCompelte = isCompelte;
        this.path = path;
        this.filename = filename;
        this.slfMediaData = slfMediaData;
    }
}
