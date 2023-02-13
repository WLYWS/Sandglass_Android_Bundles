package com.sandglass.sandglasslibrary.moudle.event;

/**
 * Greated by yangjie
 * describe:视频压缩失败完成通知
 * time:2023/1/3
 */
public class SLFEventNoCompressVideo {

    public String path;

    public String filename;

    public long id;

    public SLFEventNoCompressVideo(String path, String filename,long id){
        this.path = path;
        this.filename = filename;
        this.id = id;
    }
}
