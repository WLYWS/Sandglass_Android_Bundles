package com.sandglass.sandglasslibrary.moudle.net.responsebean;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFResponseBaseBean  {
    public int code;
    public String message;

    @Override
    public String toString ( ) {
        return "ResponseBaseBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
