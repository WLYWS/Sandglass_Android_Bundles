package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import java.util.List;

/**
 * Created by yangjie on 2023/2/23
 * describe:请求faq详情的bean
 *
 */
public class SLFFaqDetailResponseBean extends SLFResponseBaseBean {
    public SLFFaqDetailBean data;
    private String tid;

    @Override
    public String toString ( ) {
        return "SLFFirstPageFAQResponseBean{" +
                "data=" + data +
                ", code=" + code +
                ", tid=" + tid +
                ", message='" + message + '\'' +
                '}';
    }
}
