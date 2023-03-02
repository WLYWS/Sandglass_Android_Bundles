package com.sandglass.sandglasslibrary.moudle.net.responsebean;

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
    private List<SLFLeaveMsgRecord> records;

    public SLFFeedbackDetailItemBean(int pages, int total, List<SLFLeaveMsgRecord> records){
        this.pages = pages;
        this.total = total;
        this.records = records;
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
        return records;
    }

    public void setRecods(List<SLFLeaveMsgRecord> records) {
        this.records = records;
    }

    @Override
    public String toString ( ) {
        return "SLFFeedbackItemBean{" +
                "pages=" + pages +
                ", total='" + total + '\'' +
                ", recods='" + records + '\'' +
                '}';
    }
}
