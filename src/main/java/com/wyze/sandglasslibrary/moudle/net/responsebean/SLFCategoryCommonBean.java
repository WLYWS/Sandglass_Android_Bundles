package com.wyze.sandglasslibrary.moudle.net.responsebean;

import java.util.List;

/**
 * Created by wangjian on 2022/12/25
 */
public class SLFCategoryCommonBean {
    public Long id;
    public String name;
    public List <SLFCategoryCommonBean> sub;
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
        return "SLFCategoryCommonBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sub=" + sub +
                '}';
    }
}
