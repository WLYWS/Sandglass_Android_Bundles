package com.wyze.sandglasslibrary.bean.net.responsebean;

import java.util.List;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFCategoryBean {
    public int id;
    public String name;
    public String deviceModel;
    public List <SLFCategoryDetailBean> sub;

    @Override
    public String toString ( ) {
        return "SLFCategoryBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", sub=" + sub +
                '}';
    }
}
