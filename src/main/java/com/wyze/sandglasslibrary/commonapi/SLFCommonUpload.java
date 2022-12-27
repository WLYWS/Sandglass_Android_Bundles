package com.wyze.sandglasslibrary.commonapi;

import android.content.Context;
import android.content.Intent;

import com.wyze.sandglasslibrary.bean.net.responsebean.SLFUplaodFileAddressBean;
import com.wyze.sandglasslibrary.bean.net.responsebean.SLFUploadFileReponseBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Greated by yangjie
 * describe:上传链接的单例
 * time:2022/12/27
 */
public class SLFCommonUpload {

    private static HashMap<String, SLFUplaodFileAddressBean> mInstance;

    private static ArrayList<String> mListInstance;

    public synchronized static HashMap<String,SLFUplaodFileAddressBean> getInstance(){
        if(mInstance==null){
                mInstance = new HashMap<String,SLFUplaodFileAddressBean>();
        }
        return mInstance;
    }

    public synchronized static ArrayList<String> getListInstance(){
        if(mListInstance==null){
            mListInstance = new ArrayList<>();
        }
        return mListInstance;
    }

    public synchronized static void setSLFcommonUpload(SLFUploadFileReponseBean slfUploadFileReponseBean){
        if(slfUploadFileReponseBean!=null&&slfUploadFileReponseBean.data!=null&&slfUploadFileReponseBean.data.size()==9){
            if(mInstance==null){
                mInstance = new HashMap<>();
            }
            if(mListInstance==null){
                mListInstance = new ArrayList<>();
            }
            mInstance.clear();
            mListInstance.clear();
            for(int i=0;i<slfUploadFileReponseBean.data.size();i++){
                mInstance.put(slfUploadFileReponseBean.data.get(i).path,slfUploadFileReponseBean.data.get(i));
                mListInstance.add(slfUploadFileReponseBean.data.get(i).path);
            }
        }
    }
}
