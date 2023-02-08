package com.wyze.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by yangjie on 2023/2/8
 */
public class SLFSendLogRepsonseBean extends SLFResponseBaseBean {
    public String tid;

    @Override
    public String toString ( ) {
        return "SLFSendLogRepsonseBean{" +
                ", tid='" + tid + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
