package com.wyze.sandglasslibrary.bean.net.responsebean;

import java.util.List;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFCategoryBean {
    public int id;
    public String name;
    public String deviceModel;
    public List <SLFCategoryDetailBean> sub;
    /**是否选中*/
    public boolean isChecked;
    /**是否第一个，中间，最后一个，或者单个*/
    public String round_type;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getRound_type() {
        return round_type;
    }

    public void setRound_type(String round_type) {
        if(round_type==null){
            round_type = "";
        }
        this.round_type = round_type;
    }

    @Override
    public String toString ( ) {
        return "SLFCategoryBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", sub=" + sub +
                '}';
    }
}
