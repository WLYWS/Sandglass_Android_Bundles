package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Greated by yangjie
 * describe:获取未读反馈的数量
 * time:2023/2/22
 */
public class SLFUnReadCount extends SLFResponseBaseBean {
    public int data;
    public String tid;

    @Override
    public String toString ( ) {
        return "SLFCategoriesResponseBean{" +
                "data=" + data +
                ", code=" + code +
                ", tid=" + tid +
                ", message='" + message + '\'' +
                '}';
    }
}
