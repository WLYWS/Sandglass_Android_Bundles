package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import java.util.List;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFProlemDataBean {
    public Long id;
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
