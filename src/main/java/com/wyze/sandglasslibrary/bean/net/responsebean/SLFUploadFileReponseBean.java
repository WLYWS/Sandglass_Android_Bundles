package com.wyze.sandglasslibrary.bean.net.responsebean;

import java.util.List;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFUploadFileReponseBean extends SLFResponseBaseBean {
    public List <SLFUplaodFileAddressBean> data;
    public String instance_id;

    @Override
    public String toString ( ) {
        return "SLFUploadFileReponseBean{" +
                "data=" + data +
                ", instance_id='" + instance_id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}