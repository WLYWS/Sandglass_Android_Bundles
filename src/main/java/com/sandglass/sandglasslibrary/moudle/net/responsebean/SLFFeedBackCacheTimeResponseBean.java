package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFFeedBackCacheTimeResponseBean extends SLFResponseBaseBean{
    public SLFFeedBackCacheTimeData data;
    public String tid;

    @Override
    public String toString ( ) {
        return "SLFFeedBackCacheTimeResponseBean{" +
                "data=" + data +
                ", tid='" + tid + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
