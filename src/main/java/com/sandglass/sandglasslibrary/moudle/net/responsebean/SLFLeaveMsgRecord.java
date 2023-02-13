package com.sandglass.sandglasslibrary.moudle.net.responsebean;
/**
 * Greated by yangjie
 * describe:历史留言记录模型
 * time:2023/1/31
 */

import java.io.Serializable;
import java.util.List;

/**问题记录模型*/
public class SLFLeaveMsgRecord implements Serializable {
    /**反馈问题内容*/
    private String content;
    /**反馈时间戳*/
    private long replyTs;
    /**是否是用户留言*/
    private boolean user;
    /**多媒体附件*/
    private List<SLFLeveMsgRecordMoudle> attrList;

    public SLFLeaveMsgRecord(String content, long replyTs, boolean user, List<SLFLeveMsgRecordMoudle> attrList){
        this.content = content;
        this.replyTs = replyTs;
        this.user = user;
        this.attrList = attrList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getReplyTs() {
        return replyTs;
    }

    public void setReplyTs(long replyTs) {
        this.replyTs = replyTs;
    }

    public boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
    }

    public List<SLFLeveMsgRecordMoudle> getAttrList() {
        return attrList;
    }

    public void setAttrList(List<SLFLeveMsgRecordMoudle> attrList) {
        this.attrList = attrList;
    }

    @Override
    public String toString ( ) {
        return "SLFLeaveMsgRecord{" +
                "content=" + content +
                ", replyTs='" + replyTs + '\'' +
                ", user='" + user + '\'' +
                ", attrList=" + attrList +
                '}';
    }
}
