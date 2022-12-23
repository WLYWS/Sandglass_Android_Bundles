package com.wyze.sandglasslibrary.moudle;

import androidx.annotation.NonNull;

import com.wyze.sandglasslibrary.moudle.SLFRequest.SLFRequestMoudle;

import java.util.List;

public class SLFProblemType  {
    /**Problem id*/
    public  int id;
    /**Problem name*/
    public String name;
    /**是否选中*/
    public boolean isChecked;
    /**是否第一个，中间，最后一个，或者单个*/
    public String round_type;
    /**Problem 子目录*/
    public List<SLFProblemOverviewType> sub;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SLFProblemOverviewType> getSub() {
        return sub;
    }

    public void setSub(List<SLFProblemOverviewType> sub) {
        this.sub = sub;
    }

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
        this.round_type = round_type;
    }

    public SLFProblemType() {
        if(round_type==null){
            round_type ="";
        }
    }

    public SLFProblemType(int id,String name,List<SLFProblemOverviewType> sub,boolean isChecked,String round_type){
        this.id = id;
        this.name = name;
        this.sub = sub;
        this.isChecked = isChecked;
        if(round_type==null){
            round_type ="";
        }
        this.round_type = round_type;
    }



    @NonNull
    @Override
    public String toString() {
        return "SLFServiceType{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                "sub='" + sub + '\'' +
                ", isChecked='" + isChecked + '\'' +
                "round_type='" + round_type + '\'' +
                '}';
    }
}
