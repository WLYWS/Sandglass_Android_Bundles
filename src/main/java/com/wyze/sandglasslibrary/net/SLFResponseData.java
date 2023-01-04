package com.wyze.sandglasslibrary.net;

/**
 * Created by wangjian on 2022/12/23
 */
public class SLFResponseData<T> {
    private int code;
    private String msg;
    private T data;

    public void setCode (int code) {
        this.code = code;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }

    public void setData (T data) {
        this.data = data;
    }

    public int getCode ( ) {
        return code;
    }

    public String getMsg ( ) {
        return msg;
    }

    public T getData ( ) {
        return data;
    }
}
