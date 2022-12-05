package com.wyze.sandglasslibrary.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.commonui.SLFSpinnerView;
import com.wyze.sandglasslibrary.commonui.SLFTextView;

import java.util.ArrayList;

/**
 * Greated by yangjie for photoSelector
 */
public class SLFPhotoSelectorActivity extends SLFBaseActivity implements View.OnClickListener {

    private SLFSpinnerView slfSpinner;//下拉控件

    private TextView slfTitle;//标题栏标题文本

    private ImageView slfBack;//状态栏返回按钮

    private EditText slfEditQuestion;//问题编辑框

    private GridView slfPhotoSelector;//显示添加附件的控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slf_photo_selector);
        initTitle();
        initView();
    }

    //初始化view
    private void initView(){
        slfEditQuestion = findViewById(R.id.question_content);
        slfSpinner = (SLFSpinnerView)findViewById(R.id.spinner_question);
        slfPhotoSelector = findViewById(R.id.noScrollgridview);
        String[] questions = getResources().getStringArray(R.array.slf_question_selector);
        ArrayList<String> list = new ArrayList<String>();
        list.add(getResources().getString(R.string.slf_feedback_selector_question));
        for(int i=0;i<questions.length;i++){
            list.add(questions[i]);
        }
        slfSpinner.setItemsData(list);
    }

    //初始化标题栏
    private void initTitle(){
        slfTitle = findViewById(R.id.slf_tv_title_name);
        slfBack = findViewById(R.id.slf_iv_back);
        slfTitle.setText(getResources().getText(R.string.slf_feedback_title_content));
        slfBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
       if(view.getId()==R.id.slf_iv_back){
           finish();
       }else if(view.getId() == R.id.submit_feedback){
           //TODO submit feedback
       }
    }
}
