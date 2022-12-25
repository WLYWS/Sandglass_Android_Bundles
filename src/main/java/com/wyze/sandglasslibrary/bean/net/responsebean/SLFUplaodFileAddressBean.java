package com.wyze.sandglasslibrary.bean.net.responsebean;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFUplaodFileAddressBean {
    public String path;
    public String uploadUrl;

    @Override
    public String toString ( ) {
        return "SLFUplaodFileAddressBean{" +
                "path='" + path + '\'' +
                ", uploadUrl='" + uploadUrl + '\'' +
                '}';
    }
}
