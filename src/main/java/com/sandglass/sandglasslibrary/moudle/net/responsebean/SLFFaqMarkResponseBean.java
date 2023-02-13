package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFFaqMarkResponseBean extends SLFResponseBaseBean {
    public String data;
    public String instance_id;

    @Override
    public String toString ( ) {
        return "SLFFaqMarkResponseBean{" +
                "data='" + data + '\'' +
                ", instance_id='" + instance_id + '\'' +
                '}';
    }
}
