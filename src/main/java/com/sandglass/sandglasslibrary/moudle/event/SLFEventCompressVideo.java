package com.sandglass.sandglasslibrary.moudle.event;

/**
 * Greated by yangjie
 * describe:视频压缩完成通知
 * time:2023/1/3
 */
public class SLFEventCompressVideo {
    public boolean isCompelte;

    public String path;

    public String filename;

    public long id;

    public SLFEventCompressVideo(boolean isCompelte,String path,String filename,long id){
        this.isCompelte = isCompelte;
        this.path = path;
        this.filename = filename;
        this.id = id;
    }
}
