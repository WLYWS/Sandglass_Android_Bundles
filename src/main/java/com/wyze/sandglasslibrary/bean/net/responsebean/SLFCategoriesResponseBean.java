package com.wyze.sandglasslibrary.bean.net.responsebean;

import java.util.List;

/**
 * Created by wangjian on 2022/12/23
 */
public class SLFCategoriesResponseBean extends SLFResponseBaseBean {
    public List <SLFProlemDataBean> data;

    @Override
    public String toString ( ) {
        return "SLFCategoriesResponseBean{" +
                "data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
