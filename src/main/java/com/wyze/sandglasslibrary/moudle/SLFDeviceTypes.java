package com.wyze.sandglasslibrary.moudle;

import androidx.annotation.NonNull;

/**
 * Greated by yangjie
 * describe:设备类型数据结构
 * time:2022/12/22
 */
public class SLFDeviceTypes {
    /**设备id*/
    public int id;
    /**设备名称*/
    public String name;
    /**设备类型*/
    public String type;
    /**icon url*/
    public String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SLFDeviceTypes(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SLFDeviceTypes(int id, String name, String type,String url){
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
    }

    @NonNull
    @Override
    public String toString() {
        return "SLFDeviceTypes{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                "type='" + type + '\'' +
                "url='" + url + '\'' +
                '}';
    }

}
