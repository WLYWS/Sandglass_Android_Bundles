package com.sandglass.sandglasslibrary.utils;

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

    public static final String SLF_PHONE_ID = "slf_phone_id";

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

    public static boolean put(String key, Object value) {
        if (sp == null) {
            return false;
        } else {
            SharedPreferences.Editor editor = sp.edit();
            if (value == null) {
                return false;
            } else {
                String type = value.getClass().getSimpleName();

                boolean result;
                try {
                    byte var6 = -1;
                    switch(type.hashCode()) {
                        case -1808118735:
                            if (type.equals("String")) {
                                var6 = 3;
                            }
                            break;
                        case -672261858:
                            if (type.equals("Integer")) {
                                var6 = 4;
                            }
                            break;
                        case 2374300:
                            if (type.equals("Long")) {
                                var6 = 1;
                            }
                            break;
                        case 67973692:
                            if (type.equals("Float")) {
                                var6 = 2;
                            }
                            break;
                        case 1729365000:
                            if (type.equals("Boolean")) {
                                var6 = 0;
                            }
                    }

                    switch(var6) {
                        case 0:
                            editor.putBoolean(key, (Boolean)value);
                            break;
                        case 1:
                            editor.putLong(key, (Long)value);
                            break;
                        case 2:
                            editor.putFloat(key, (Float)value);
                            break;
                        case 3:
                            editor.putString(key, (String)value);
                            break;
                        case 4:
                            editor.putInt(key, (Integer)value);
                            break;
                        default:
                            String json = JSON.toJSONString(value);
                            editor.putString(key, json);
                    }

                    result = true;
                } catch (Exception var8) {
                    result = false;
                }

                editor.apply();
                return result;
            }
        }
    }

    /**
     * 保存数据到SharedPreferences
     *
     * @param key   键
     * @param value 需要保存的数据
     * @return 保存结果
     */
    public static boolean putCommit(String key, Object value) {
        if (sp == null) {
            return false;
        }
        SharedPreferences.Editor editor = sp.edit();
        if (value==null){
            return false;
        }
        String type = value.getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    editor.putBoolean(key, (Boolean) value);
                    break;
                case "Long":
                    editor.putLong(key, (Long) value);
                    break;
                case "Float":
                    editor.putFloat(key, (Float) value);
                    break;
                case "String":
                    editor.putString(key, (String) value);
                    break;
                case "Integer":
                    editor.putInt(key, (Integer) value);
                    break;
                default:
                    String json = JSON.toJSONString(value);
                    editor.putString(key, json);
                    break;
            }

        } catch (Exception e) {
            return false;
        }

        return editor.commit();
    }

    public static int getInt(String key, int defaultValue){
        if (sp == null) {
            return defaultValue;
        }
        return sp.getInt(key, defaultValue);
    }

    public static String getString(String key, String defaultValue){
        if (sp == null) {
            return defaultValue;
        }
        return sp.getString(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue){
        if (sp == null) {
            return defaultValue;
        }
        return sp.getBoolean(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue){
        if (sp == null) {
            return defaultValue;
        }
        return sp.getLong(key, defaultValue);
    }

    public static float getFloat(String key, float defaultValue){
        if (sp == null) {
            return defaultValue;
        }
        return sp.getFloat(key, defaultValue);
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