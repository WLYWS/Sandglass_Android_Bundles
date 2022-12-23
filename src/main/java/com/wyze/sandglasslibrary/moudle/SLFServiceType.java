package com.wyze.sandglasslibrary.moudle;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.wyze.sandglasslibrary.moudle.SLFRequest.SLFRequestMoudle;
import com.wyze.sandglasslibrary.moudle.SLFResponse.SLFBaseResponseMoudle;

import java.util.List;

/**
 * Greated by yangjie
 * describe:serviceType数据类型
 * time:2022/12/15
 * */
public class SLFServiceType{
    /**id*/
    public  int id;
    /**name*/
    public String name;
    /**是否选中*/
    public boolean isChecked;
    /**是否第一个，中间，最后一个，或者单个*/
    public String round_type;
    /**子目录*/
    public List<SLFProblemType> sub;


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

    public List<SLFProblemType> getSub() {
        return sub;
    }

    public void setSub(List<SLFProblemType> sub) {
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

    public SLFServiceType() {
        if(round_type==null){
            round_type ="";
        }
    }

    public SLFServiceType(int id,String name,List<SLFProblemType> sub,boolean isChecked,String round_type){
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
