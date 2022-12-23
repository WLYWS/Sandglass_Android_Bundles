package com.wyze.sandglasslibrary.moudle.SLFRequest;

import com.google.gson.Gson;

/**
 * Greated by yangjie
 * describe:请求json数据转换
 * time:2022/12/15
 */
public class SLFRequestMoudle {

    public String beanToJson(SLFRequestMoudle request) {
        Gson gson = new Gson();
        return gson.toJson(request);
    }
}
