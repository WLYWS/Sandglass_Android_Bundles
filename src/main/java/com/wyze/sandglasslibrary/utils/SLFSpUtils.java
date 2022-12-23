package com.wyze.sandglasslibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Greated by yangjie
 * describe:sharedprefrence工具类
 * time:2022/12/10
 */
public class SLFSpUtils {
    private static SharedPreferences sp;

    private SLFSpUtils() {}

    /**
     * 初始化SharedPreferencesUtil,只需要初始化一次，建议在Application中初始化
     *
     * @param context 上下文对象
     * @param name    SharedPreferences Name
     */
    public static void getInstance(Context context, String name) {
        //        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        if(sp == null){
            sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
    }


    /**
     * 用于保存集合
     *
     * @param key  key
     * @param list 集合数据
     */
    public static <T> void putListData(String key, List<T> list) {
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        JSONArray array = new JSONArray();
        array.addAll(list);
        editor.putString(key, array.toString());
        editor.apply();
    }

    /**
     * 获取保存的List
     *
     * @param key key
     * @return 对应的Lis集合
     */
    public static <T> List<T> getListData(String key, Class<T> cls) {
        List<T> list = new ArrayList<>();
        if (sp == null) {
            return list;
        }
        String json = sp.getString(key, "");
        if (!json.equals("") && json.length() > 0) {
            JSONArray array = JSON.parseArray(json);
            list.addAll(array.toJavaList(cls));
        }
        return list;
    }

    /**
     * 用于保存map
     *
     * @param key key
     * @param map map数据
     * @return 保存结果
     */
    @SuppressWarnings("UnusedReturnValue")
    public static <K, V> boolean putHashMapData(String key, Map<K, V> map) {
        if (sp == null) {
            return false;
        }
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        try {
            String json = JSON.toJSONString(map);
            editor.putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        editor.apply();
        return result;
    }

    /**
     * 用于获取保存的map
     *
     * @param key key
     * @return HashMap
     */
    @SuppressWarnings({"java:S3740","java:S1319","rawtypes"})
    public static HashMap getHashMapData(String key) {
        if (sp == null) {
            return null;
        }
        String json = sp.getString(key, "");
        if(TextUtils.isEmpty(json)){
            return null;
        }
        return JSON.parseObject(json, HashMap.class);
    }

    //删除某个key的值
    public static void remove(String key) {
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key).apply();
    }
}
