package com.wyze.sandglasslibrary.moudle.event;

/**
 * Greated by yangjie
 * describe:视频压缩完成通知
 * time:2023/1/3
 */
public class SLFEventCompressVideo {
    public boolean isCompelte;

    public String uploadUrl;

    public String uploadThumbleUrl;

    public SLFEventCompressVideo(boolean isCompelte,String uploadUrl,String uploadThumbleUrl){
        this.isCompelte = isCompelte;
        this.uploadUrl = uploadUrl;
        this.uploadThumbleUrl = uploadThumbleUrl;
    }
}
