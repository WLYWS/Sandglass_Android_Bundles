package com.wyze.sandglasslibrary.bean.net.responsebean;

import java.util.List;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFCategoryCommonBean {
    public int id;
    public String name;
    public List <SLFCategoryCommonBean> sub;
    /**是否选中*/
    public boolean isChecked;
    /**是否第一个，中间，最后一个，或者单个*/
    public String round_type;

    @Override
    public String toString ( ) {
        return "SLFCategoryCommonBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sub=" + sub +
                '}';
    }
}
