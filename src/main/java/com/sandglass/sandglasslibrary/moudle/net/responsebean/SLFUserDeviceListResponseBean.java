package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import java.util.ArrayList;
import java.util.List;

/**
 * Greated by yangjie
 * describe:获取用户邮箱和用户头像
 * time:2023/3/12
 */
public class SLFUserDeviceListResponseBean extends SLFResponseBaseBean{
    /**tid**/
    private String tid;
    /**data**/
    private ArrayList<SLFUserinfoDeviceMoudle> data;
    /**是否第一个，中间，最后一个，或者单个*/
    public String round_type;

    public SLFUserDeviceListResponseBean(){}

    public SLFUserDeviceListResponseBean(String tid, ArrayList<SLFUserinfoDeviceMoudle> data,String round_type){
        this.tid = tid;
        this.data = data;
        this.round_type = round_type;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public ArrayList<SLFUserinfoDeviceMoudle> getData() {
        return data;
    }

    public void setData(ArrayList<SLFUserinfoDeviceMoudle> data) {
        this.data = data;
    }

    public String getRound_type() {
        return round_type;
    }

    public void setRound_type(String round_type) {
        this.round_type = round_type;
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
