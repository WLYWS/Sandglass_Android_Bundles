package com.wyze.sandglasslibrary.moudle.net.responsebean;

import java.util.List;

/**
 * Greated by yangjie
 * describe:反馈单数据模型
 * time:2023/1/29
 */
public class SLFFeedbackItemBean{
    /**总页数*/
    private int pages;
    /**总条数*/
    private int total;
    /**问题记录列表*/
    private List<SLFRecode> recods;

    public SLFFeedbackItemBean(int pages,int total,List<SLFRecode> recods){
        this.pages = pages;
        this.total = total;
        this.recods = recods;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SLFRecode> getRecods() {
        return recods;
    }

    public void setRecods(List<SLFRecode> recods) {
        this.recods = recods;
    }

    @Override
    public String toString ( ) {
        return "SLFFeedbackItemBean{" +
                "pages=" + pages +
                ", total='" + total + '\'' +
                ", recods='" + recods + '\'' +
                '}';
    }
}
