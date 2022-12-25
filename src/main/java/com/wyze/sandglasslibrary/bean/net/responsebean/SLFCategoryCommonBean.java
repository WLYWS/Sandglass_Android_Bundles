package com.wyze.sandglasslibrary.bean.net.responsebean;

import java.util.List;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFCategoryCommonBean {
    public int id;
    public String name;
    public List <SLFCategoryCommonBean> sub;

    @Override
    public String toString ( ) {
        return "SLFCategoryCommonBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sub=" + sub +
                '}';
    }
}
