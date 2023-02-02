package com.wyze.sandglasslibrary.moudle.net.responsebean;

import java.util.List;

/**
 * Greated by yangjie
 * describe:留言历史数据模型
 * time:2023/1/29
 */
public class SLFFeedbackDetailItemBean {
    /**总页数*/
    private int pages;
    /**总条数*/
    private int total;
    /**问题记录列表*/
    private List<SLFLeaveMsgRecord> recods;

    public SLFFeedbackDetailItemBean(int pages, int total, List<SLFLeaveMsgRecord> recods){
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

    public List<SLFLeaveMsgRecord> getRecods() {
        return recods;
    }

    public void setRecods(List<SLFLeaveMsgRecord> recods) {
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
