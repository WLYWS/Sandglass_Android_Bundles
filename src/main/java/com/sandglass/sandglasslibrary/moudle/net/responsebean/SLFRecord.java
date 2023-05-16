package com.sandglass.sandglasslibrary.moudle.net.responsebean;
/**
 * Greated by yangjie
 * describe:问题记录模型
 * time:2023/1/29
 */

import java.io.Serializable;

/**问题记录模型*/
public class SLFRecord implements Serializable {
    /**反馈id*/
    private long id;
    /**deviceId*/
    private String deviceId;
    /**deviceModel*/
    private String deviceModel;
    /**一级类别文案*/
    private String serviceTypeText;
    /**二级类别文案*/
    private String categoryText;
    /**三级类别文案*/
    private String subCategoryText;
    /**用户描述*/
    private String content;
    /**最后一次回复时间戳，单位毫秒*/
    private long lastReplyTs;
    /**0=待处理，1=处理中，4=已完成*/
    private int status;
    /**是否勾选了发送日志 0=否，1=是*/
    private int sendLog;
    /**是否查看了新消息 0=未读，1=读了*/
    private int read;

    public SLFRecord(){}

    public SLFRecord(long id, String deviceId, String deviceModel, String serviceTypeText, String categoryText, String subCategoryText, String content, long lastReplyTs, int status, int sendLog, int read){
        this.id = id;
        this.deviceId = deviceId;
        this.deviceModel = deviceModel;
        this.serviceTypeText = serviceTypeText;
        this.categoryText = categoryText;
        this.subCategoryText = subCategoryText;
        this.content = content;
        this.lastReplyTs = lastReplyTs;
        this.status = status;
        this.sendLog = sendLog;
        this.read = read;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServiceTypeText() {
        return serviceTypeText;
    }

    public void setServiceTypeText(String serviceTypeText) {
        this.serviceTypeText = serviceTypeText;
    }

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public String getSubCategoryText() {
        return subCategoryText;
    }

    public void setSubCategoryText(String subCategoryText) {
        this.subCategoryText = subCategoryText;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getLastReplyTs() {
        return lastReplyTs;
    }

    public void setLastReplyTs(long lastReplyTs) {
        this.lastReplyTs = lastReplyTs;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSendLog() {
        return sendLog;
    }

    public void setSendLog(int sendLog) {
        this.sendLog = sendLog;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    @Override
    public String toString ( ) {
        return "SLFRecord{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", serviceTypeText=" + serviceTypeText +
                ", categoryText=" + categoryText +
                ", subCategoryText=" + subCategoryText +
                ", content=" + content +
                ", lastReplyTs=" + lastReplyTs +
                ", status=" + status +
                ", sendLog=" + sendLog +
                ", read=" + read +
                '}';
    }
}
