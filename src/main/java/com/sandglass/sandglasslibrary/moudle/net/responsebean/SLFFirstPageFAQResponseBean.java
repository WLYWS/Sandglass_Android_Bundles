package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import java.util.List;

/**
 * Created by yangjie on 2023/2/20
 */
public class SLFFirstPageFAQResponseBean extends SLFResponseBaseBean {
    public List <SLFFirstPageFAQBean> data;


    @Override
    public String toString ( ) {
        return "SLFFirstPageFAQResponseBean{" +
                "data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
