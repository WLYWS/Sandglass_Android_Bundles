package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Greated by yangjie
 * describe:获取用户邮箱和设备列表
 * time:2023/3/12
 */
public class SLFUserInfoResponseBean extends SLFResponseBaseBean{
    /**tid**/
    private String tid;
    /**data**/
    private SLFUserInfoMoudle data;

    public SLFUserInfoResponseBean(String tid,SLFUserInfoMoudle data){
        this.tid = tid;
        this.data = data;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public SLFUserInfoMoudle getData() {
        return data;
    }

    public void setData(SLFUserInfoMoudle data) {
        this.data = data;
    }

    @Override
    public String toString ( ) {
        return "SLFUserInfoMoudle{" +
                "tid=" + tid +
                ", data=" + data +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
