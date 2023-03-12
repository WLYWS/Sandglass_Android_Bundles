package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Greated by yangjie
 * describe:敏感词校验responsebean
 * time:2023/3/12
 */
public class SLFillagelWordResponseBean extends SLFResponseBaseBean{
    /**是否包含敏感词*/
    private boolean data;
    /**instance_id*/
    private String instance_id;

    public SLFillagelWordResponseBean(boolean data,String instance_id,int code,String message){
        this.data = data;
        this.instance_id = instance_id;
        this.code = code;
        this.message = message;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    public String getInstance_id() {
        return instance_id;
    }

    public void setInstance_id(String instance_id) {
        this.instance_id = instance_id;
    }

    @Override
    public String toString ( ) {
        return "SLFCategoriesResponseBean{" +
                "data=" + data +
                "instance_id=" + instance_id +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
