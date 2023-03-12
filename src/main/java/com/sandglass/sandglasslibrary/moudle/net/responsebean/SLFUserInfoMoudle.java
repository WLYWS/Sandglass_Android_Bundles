package com.sandglass.sandglasslibrary.moudle.net.responsebean;

import java.util.ArrayList;

/**
 * Greated by yangjie
 * describe:用户信息moudle
 * time:2023/3/12
 */
public class SLFUserInfoMoudle {
    /**email*/
    private String email;
    /**设备列表**/
    private ArrayList<SLFUserinfoDeviceMoudle> deviceList;

    public SLFUserInfoMoudle(String email,ArrayList<SLFUserinfoDeviceMoudle> deviceList){
        this.email = email;
        this.deviceList = deviceList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<SLFUserinfoDeviceMoudle> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(ArrayList<SLFUserinfoDeviceMoudle> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public String toString ( ) {
        return "SLFUserInfoMoudle{" +
                "email=" + email +
                ", deviceList=" + deviceList + '\'' +
                '}';
    }
}
