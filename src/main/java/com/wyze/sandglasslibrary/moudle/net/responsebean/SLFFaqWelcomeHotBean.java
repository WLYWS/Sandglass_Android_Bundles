package com.wyze.sandglasslibrary.moudle.net.responsebean;

import java.util.List;

/**
 * Created by wangjian on 2023/1/5
 */
public class SLFFaqWelcomeHotBean {
    public String welcome;
    public List <String> hotFaq;

    @Override
    public String toString ( ) {
        return "SLFFaqWelcomeHotBean{" +
                "welcome='" + welcome + '\'' +
                ", hotFaq=" + hotFaq +
                '}';
    }
}
