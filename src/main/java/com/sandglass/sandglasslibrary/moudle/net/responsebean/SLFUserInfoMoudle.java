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
    /**iconUrl头像地址*/
    private String iconUrl;


    public SLFUserInfoMoudle(String email,String iconUrl){
        this.email = email;
        this.iconUrl = iconUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public String toString ( ) {
        return "SLFUserInfoMoudle{" +
                "email=" + email +
                ", iconUrl=" + iconUrl + '\'' +
                '}';
    }
}
