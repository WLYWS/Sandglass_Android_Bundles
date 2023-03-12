package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Greated by yangjie
 * describe：设备信息moudle
 * time：2023/3/12
 */
public class SLFUserinfoDeviceMoudle {
    /**deviceId*/
    private String deviceId;
    /**deviceName*/
    private String deviceName;
    /**deviceModel*/
    private String deviceModel;
    /**固件版本*/
    private String firmwareVersion;

    public SLFUserinfoDeviceMoudle(String deviceId,String deviceName,String deviceModel,String firmwareVersion){
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceModel = deviceModel;
        this.firmwareVersion = firmwareVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    @Override
    public String toString ( ) {
        return "SLFUserinfoDeviceMoudle{" +
                "deviceId=" + deviceId +
                ", deviceName=" + deviceName +
                ", deviceModel='" + deviceModel +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                '}';
    }
}
