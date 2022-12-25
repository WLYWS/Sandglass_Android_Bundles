package com.wyze.sandglasslibrary.bean.net.responsebean;

import java.util.List;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFProlemDataBean {
    public int id;
    public String name;
    public List <SLFCategoryBean> sub;

    @Override
    public String toString ( ) {
        return "ProlemDataBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sub=" + sub +
                '}';
    }
}
