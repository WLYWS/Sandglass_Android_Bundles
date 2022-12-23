package com.wyze.sandglasslibrary.base;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;

/**
 * created by yangjie
 * describe:图片选择BaseActivity
 * time: 2022/12/6
 */
public abstract class SLFPhotoBaseActivity extends SLFBaseActivity{
    protected final String TAG = this.getClass().getSimpleName();
    private int colorRes;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initContentView();
        init();
    }
    public abstract void initContentView();

    @SuppressWarnings("java:S1874")
    private void init(){
//		String appColor = getIntent().getStringExtra("app_color")
        colorRes = getIntent().getIntExtra("app_color", getResources().getColor(R.color.transparent));
        int textColor = getResources().getColor(R.color.white);
        RelativeLayout slf_photo_TitleBar = findViewById(R.id.slf_photo_title_titleBar);
        ImageView slf_photo_ivBack = findViewById(R.id.slf_photo_title_back);
        Button slf_photo_selectedBtn = findViewById(R.id.slf_photo_selector_iv_confirm);
        //Button slf_photo_btnCrop = findViewById(R.id.slf_photo_base_iv_crop);
        TextView slf_photo_tvFileName = findViewById(R.id.slf_photo_title_filename);
        ImageView slf_photo_ivArrowDown = findViewById(R.id.slf_iv_arrow_down);


        if(colorRes !=0){
            slf_photo_TitleBar.setBackgroundColor(colorRes);

//            if(slf_photo_selectedBtn!=null){
//                slf_photo_selectedBtn.updateBackGround(colorRes);
//            }
//
//            if(slf_photo_btnCrop!=null){
//                slf_photo_btnCrop.updateBackGround(colorRes);
//            }

            int r = Color.red(colorRes);
            int g = Color.green(colorRes);
            int b = Color.blue(colorRes);
            double y = 0.2126 * r + 0.7152 * g + 0.0722 * b;
            if(y>200){  //背景接近白色则切换为黑色图标
                textColor = getResources().getColor(R.color.black);
                slf_photo_ivBack.setImageResource(R.drawable.slf_title_icon_dark_back);
                slf_photo_ivArrowDown.setImageResource(R.drawable.slf_title_icon_white_back);
                if(slf_photo_selectedBtn!=null){
                    slf_photo_selectedBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.slf_title_icon_dark_back,0,0,0);
                }
//                if(slf_photo_btnCrop!=null){
//                    slf_photo_btnCrop.setCompoundDrawablesWithIntrinsicBounds(R.drawable.slf_title_icon_dark_back,0,0,0);
//                }
            }
        }else{
            colorRes = getResources().getColor(R.color.transparent);
            slf_photo_TitleBar.setBackgroundColor(colorRes);
        }


        slf_photo_tvFileName.setTextColor(textColor);

    }

    public int getColor(){
        return colorRes;
    }

}
