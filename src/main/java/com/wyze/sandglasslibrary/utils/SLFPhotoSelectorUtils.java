package com.wyze.sandglasslibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFUploadFileReponseBean;
import com.wyze.sandglasslibrary.functionmoudle.activity.feedback.SLFPhotoGridActivity;
import com.wyze.sandglasslibrary.functionmoudle.enums.SLFMediaQuality;
import com.wyze.sandglasslibrary.functionmoudle.enums.SLFMediaType;
import com.wyze.sandglasslibrary.interf.SLFOnSelectorListener;

public class SLFPhotoSelectorUtils {

    private static Intent mIntent;
    private static Context mContext;
    private static SLFPhotoSelectorUtils util;
    public static final String PIC_PATH_LIST = "pic_path_list";

    public static SLFOnSelectorListener mListenter;
    public static SLFMediaType selectType;
    public static SLFMediaQuality quality;
    public static boolean isOnlyCamera = false;



    public static SLFPhotoSelectorUtils with(Context context) {
        util = new SLFPhotoSelectorUtils();
        mIntent = new Intent(context, SLFPhotoGridActivity.class);
        mContext = context;
        selectType = SLFMediaType.Image;
        quality = SLFMediaQuality.Medium;
        return util;
    }

    public SLFPhotoSelectorUtils themeColor(int colorRes) {
        if (mIntent != null) {
            mIntent.putExtra("app_color", colorRes);
        }
        return util;
    }

    public SLFPhotoSelectorUtils aspect(int aspectX, int aspectY) {
        if (mIntent != null) {
            mIntent.putExtra("aspect_x", aspectX);
            mIntent.putExtra("aspect_y", aspectY);
        }
        return util;
    }

    public SLFPhotoSelectorUtils SLFMediaQuality(SLFMediaType type) {
        SLFPhotoSelectorUtils.selectType = type;
        return util;
    }

    public SLFPhotoSelectorUtils SLFMediaQuality(SLFMediaQuality quality) {
        SLFPhotoSelectorUtils.quality = quality;
        return util;
    }

    public SLFPhotoSelectorUtils selectedNum(int num) {
        if (mIntent != null) {
            mIntent.putExtra("selected_number", num);
        }
        return util;
    }

    public SLFPhotoSelectorUtils directCrop(boolean isDirectCrop) {
        if (mIntent != null) {
            mIntent.putExtra("direct_crop", isDirectCrop);
        }
        return util;
    }

    public SLFPhotoSelectorUtils insertAlbum(boolean isInsertAlbum) {
        if (mIntent != null) {
            mIntent.putExtra("insert_album", isInsertAlbum);
        }
        return util;
    }

    public SLFPhotoSelectorUtils setUploadObject(SLFUploadFileReponseBean slfUploadFileReponseBean){
        if(mIntent !=null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("uploadbean",slfUploadFileReponseBean);
            mIntent.putExtras(bundle);
        }
        return util;
    }

//	public SLFPhotoSelectorUtils open(int requestCode) {
//		if (mIntent != null && mActivity != null) {
//			mActivity.startActivityForResult(mIntent, requestCode);
//		}
//		return util;
//	}

    public SLFPhotoSelectorUtils open(SLFOnSelectorListener mListenter) {
        isOnlyCamera = false;
        if (mIntent != null && mContext != null) {
            SLFPhotoSelectorUtils.mListenter = mListenter;
            mContext.startActivity(mIntent);
        }
        return util;
    }

//    public SLFPhotoSelectorUtils openCamera(SLFOnSelectorListener mListenter) {
//        isOnlyCamera = true;
//        if (mIntent!=null && mContext != null) {
////			int colorRes = mIntent.getIntExtra("app_color",0);
////			int aspectX = mIntent.getIntExtra("aspect_x",0);
////			int aspectY = mIntent.getIntExtra("aspect_y",0);
////			mIntent.setClass(mContext, TakePhotoActivity.class);
//            mIntent.setClass(mContext, SLFTakeToCamra.class);
//            SLFPhotoSelectorUtils.mListenter = mListenter;
//            mContext.startActivity(mIntent);
//        }
//        return util;
//    }

}
