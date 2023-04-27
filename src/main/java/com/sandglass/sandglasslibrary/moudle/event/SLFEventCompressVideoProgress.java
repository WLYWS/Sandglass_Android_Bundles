package com.sandglass.sandglasslibrary.moudle.event;

/**
 * Greated by yangjie
 * describe:视频压缩进度传送
 * time:2023/4/25
 */
public class SLFEventCompressVideoProgress {

    public long id;

    public long progress;

    public SLFEventCompressVideoProgress(long id,long progress){
        this.id = id;
        this.progress = progress;
    }
}
