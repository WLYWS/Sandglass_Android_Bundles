package com.wyze.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFLeaveMsgResponseBean extends SLFResponseBaseBean {
    public String instance_id;

    @Override
    public String toString ( ) {
        return "SLFCommonPostResponseBean{" +
                "instance_id='" + instance_id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
