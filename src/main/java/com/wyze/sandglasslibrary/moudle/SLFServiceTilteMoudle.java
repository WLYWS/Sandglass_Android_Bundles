package com.wyze.sandglasslibrary.moudle;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Greated by yangjie
 * describe:serviceTypeTile数据类型
 * time:2022/12/15
 * */
public class SLFServiceTilteMoudle {
    /**id*/
    public  int id;
    /**name*/
    public String name;
    /**serviceTypeTile sub*/
    public List<SLFServiceType> sub;
    /**是什么类型*/
    public String round_type;


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

    public List<SLFServiceType> getSub() {
        return sub;
    }

    public void setSub(List<SLFServiceType> sub) {
        this.sub = sub;
    }


    public String getRound_type() {
        return round_type;
    }

    public void setRound_type(String round_type) {
        this.round_type = round_type;
    }

    public SLFServiceTilteMoudle() {
        if(round_type==null){
            round_type ="";
        }
    }

    public SLFServiceTilteMoudle(int id, String name, List<SLFServiceType> sub,String round_type){
        this.id = id;
        this.name = name;
        this.sub = sub;
        if(round_type==null){
            round_type ="";
        }
        this.round_type = round_type;
    }



    @NonNull
    @Override
    public String toString() {
        return "SLFServiceTitleMoudle{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                "sub='" + sub + '\'' +
                "round_type='" + round_type + '\'' +
                '}';
    }
}
