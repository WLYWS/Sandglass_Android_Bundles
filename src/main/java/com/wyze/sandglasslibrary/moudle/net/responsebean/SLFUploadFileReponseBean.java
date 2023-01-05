package com.wyze.sandglasslibrary.moudle.net.responsebean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFUploadFileReponseBean extends SLFResponseBaseBean implements Serializable {
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
