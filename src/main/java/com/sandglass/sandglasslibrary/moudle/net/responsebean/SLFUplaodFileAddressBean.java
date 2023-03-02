package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFUplaodFileAddressBean {
    public String path;
    public String uploadUrl;
    /**是否空闲*/
    public boolean isIdle;

    @Override
    public String toString ( ) {
        return "SLFUplaodFileAddressBean{" +
                "path='" + path + '\'' +
                ", uploadUrl='" + uploadUrl + '\'' +
                '}';
    }
}
