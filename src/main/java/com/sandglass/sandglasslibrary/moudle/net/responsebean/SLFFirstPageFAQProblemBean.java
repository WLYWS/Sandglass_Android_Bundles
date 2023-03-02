package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import java.util.List;

/**
 * Greated by yangjie
 * describe:首页FAQ分类二级菜单数据bean
 * time:2023/2/20
 */
public class SLFFirstPageFAQProblemBean {
    /**id*/
    private long id;
    /**设备名称*/
    private String name;
    /**三级菜单列表*/
    private List<SLFFirstPageFAQFapListBean> faqList;
    /**是否展开*/
    private boolean isExtend;

    public SLFFirstPageFAQProblemBean(long id, String name, List<SLFFirstPageFAQFapListBean> faqList,boolean isExtend){
        this.id = id;
        this.name = name;
        this.faqList = faqList;
        this.isExtend = isExtend;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SLFFirstPageFAQFapListBean> getFaqList() {
        return faqList;
    }

    public void setFaqList(List<SLFFirstPageFAQFapListBean> faqList) {
        this.faqList = faqList;
    }

    public boolean isExtend() {
        return isExtend;
    }

    public void setExtend(boolean extend) {
        isExtend = extend;
    }


    @Override
    public String toString ( ) {
        return "SLFFirstPageFAQProblemBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", faqList='" + faqList + '\'' +
                '}';
    }
}
