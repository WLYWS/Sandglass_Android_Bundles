package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Greated by yangjie openai 数据模型
 */
public class SLFFaqOpenAiResponseBean extends SLFResponseBaseBean{
    private String instance_id;
    private String data;
    public SLFFaqOpenAiResponseBean ( ) {
    }

    public SLFFaqOpenAiResponseBean (String data, String instance_id) {
        this.data = data;
        this.instance_id = instance_id;
    }

    @Override
    public String toString ( ) {
        return "SLFFaqOpenAiResponseBean{" +
                "data=" + data +
                ", instance_id='" + instance_id + '\'' +
                '}';
    }

    public String getInstance_id() {
        return instance_id;
    }

    public void setInstance_id(String instance_id) {
        this.instance_id = instance_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
