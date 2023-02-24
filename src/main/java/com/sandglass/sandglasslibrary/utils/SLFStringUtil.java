package com.sandglass.sandglasslibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangjian on 2022/12/21
 */
public class SLFStringUtil {

    /**
     * 剔除字符串中的换行，空格
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {

        String dest = "";

        if (str != null) {

            Pattern p = Pattern.compile("\\s*|\t|\r|\n");

            Matcher m = p.matcher(str);

            dest = m.replaceAll("");

        }

        return dest;

    }

    public static String getClassName(String type){
        int last = type.toString().lastIndexOf(".");
        String className = type.toString().substring(last+1);
        return className;
    }
}
