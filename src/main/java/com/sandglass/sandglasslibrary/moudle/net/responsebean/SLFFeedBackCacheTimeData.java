package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * 获取数据更新时间data数据model
 * Created by wangjian on 2023/3/23
 */
public class SLFFeedBackCacheTimeData {
    public long feedbackCategory;
    public long faqCategory;
    public long faqDetail;

    @Override
    public String toString ( ) {
        return "SLFFeedBackCacheTimeData{" +
                "feedbackCategory='" + feedbackCategory + '\'' +
                ", faqCategory='" + faqCategory + '\'' +
                ", faqDetail='" + faqDetail + '\'' +
                '}';
    }
}
