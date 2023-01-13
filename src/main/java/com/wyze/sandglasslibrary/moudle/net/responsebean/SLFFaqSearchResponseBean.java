package com.wyze.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFFaqSearchResponseBean extends SLFResponseBaseBean{
    public SLFFaqSearchReslutBean data;
    public String instance_id;
    public long requestTime;

    public SLFFaqSearchResponseBean ( ) {
    }

    public SLFFaqSearchResponseBean (SLFFaqSearchReslutBean data, String instance_id) {
        this.data = data;
        this.instance_id = instance_id;
    }

    @Override
    public String toString ( ) {
        return "SLFFaqSearchResponseBean{" +
                "data=" + data +
                ", instance_id='" + instance_id + '\'' +
                '}';
    }
}
