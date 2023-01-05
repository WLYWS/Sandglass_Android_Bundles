package com.wyze.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFFaqWelcomeHotQResponseBean extends SLFResponseBaseBean{
    public SLFFaqWelcomeHotBean data;
    public String instance_id;

    @Override
    public String toString ( ) {
        return "SLFFaqWelcomeHotQResponseBean{" +
                "data=" + data +
                ", instance_id='" + instance_id + '\'' +
                '}';
    }
}
