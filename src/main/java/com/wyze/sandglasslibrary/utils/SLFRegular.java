package com.wyze.sandglasslibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Greated by yangjie
 * describle:正则表达式 例如：判断邮箱格式是否正确
 * time:2022/12/14
 */
public class SLFRegular {

    public static boolean isEmail(String email){
            String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
            Pattern p = Pattern.compile(str);
            Matcher m = p.matcher(email);
            return m.matches();
    }
}
