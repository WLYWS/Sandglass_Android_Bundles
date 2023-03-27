package com.sandglass.sandglasslibrary.moudle;

import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryCommonBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryDetailBean;

import java.util.List;

/**
 * Greated by yangjie
 * describe:需要上传的user信息
 * time:2023/3/12
 */
public class SLFUserDeviceSaved {
    /**device id*/
    private String deviceid;
    /**device module*/
    private String deviceMoudle;
    /**真实的serviceType id*/
    private Long serviceTypeid;
    /**固件版本信息*/
    private String firmewareVersion;

    public SLFUserDeviceSaved(){}

    public SLFUserDeviceSaved(String deviceid,String deviceMoudle,Long serviceTypeid,String firmewareVersion){
        this.deviceid = deviceid;
        this.deviceMoudle = deviceMoudle;
        this.serviceTypeid = serviceTypeid;
        this.firmewareVersion = firmewareVersion;

    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDeviceMoudle() {
        return deviceMoudle;
    }

    public void setDeviceMoudle(String deviceMoudle) {
        this.deviceMoudle = deviceMoudle;
    }

    public Long getServiceTypeid() {
        return serviceTypeid;
    }

    public void setServiceTypeid(Long serviceTypeid) {
        this.serviceTypeid = serviceTypeid;
    }

    public String getFirmewareVersion() {
        return firmewareVersion;
    }

    public void setFirmewareVersion(String firmewareVersion) {
        this.firmewareVersion = firmewareVersion;
    }
}
