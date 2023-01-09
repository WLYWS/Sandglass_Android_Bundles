package com.wyze.sandglasslibrary.functionmoudle.activity.helpAndFeedback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.commonui.SLFToastUtil;
import com.wyze.sandglasslibrary.functionmoudle.activity.chatbot.SLFChatBotActivity;
import com.wyze.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackList;
import com.wyze.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackSubmitActivity;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFDeviceGridAdapter;
import com.wyze.sandglasslibrary.moudle.SLFDeviceTypes;
import com.wyze.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Greated by yangjie
 * describe:智能助手和反馈首页
 * time:2022/12/22
 */
public class SLFHelpAndFeedback extends SLFBaseActivity implements View.OnClickListener{
    /**设备数据列表*/
    private List<SLFDeviceTypes> deviceTypesList = new ArrayList<SLFDeviceTypes>();
    /**智能机器人按钮布局*/
    private RelativeLayout chatBotLayout;
    /**反馈按钮布局*/
    private RelativeLayout feedbackLayout;
    /**设备列表显示网格*/
    private GridView deviceGrid;
    /**设备列表显示布局*/
    private LinearLayout deviceAllLinear;
    /**设备列表adapter*/
    private SLFDeviceGridAdapter slfDeviceGridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_help_and_feedback);
        getDeviceTypesList();
        initTitle();
        initView();

    }

    private void initTitle(){
        TextView title = findViewById(R.id.slf_tv_title_name);
        ImageView imgRight = findViewById(R.id.slf_iv_second_right);
        title.setText(SLFResourceUtils.getString(R.string.slf_help_and_feedback_bar_title));
        imgRight.setVisibility(View.VISIBLE);
        imgRight.setImageResource(R.drawable.slf_help_feedback_format);
        imgRight.setOnClickListener(this);
    }

    private void initView(){
        chatBotLayout = findViewById(R.id.slf_help_and_feedback_chat_bot);
        feedbackLayout = findViewById(R.id.slf_help_and_feedback_feedback);
        deviceGrid = findViewById(R.id.slf_all_devices_grid);
        deviceAllLinear = findViewById(R.id.slf_all_devices_grid_linear);
        slfDeviceGridAdapter = new SLFDeviceGridAdapter(getContext(),deviceTypesList);
        deviceGrid.setAdapter(slfDeviceGridAdapter);
        chatBotLayout.setOnClickListener(this);
        feedbackLayout.setOnClickListener(this);
        deviceGrid.setOnItemClickListener((parent, view, position, id) -> {
            //TODO gotoFAQ
            SLFLogUtil.d("yj","deviceGrid click position::"+position);
        });
    }

    private List<SLFDeviceTypes> getDeviceTypesList(){
        //TODO 获取所有设备类型
        for(int i=0;i<10;i++) {
            SLFDeviceTypes deviceTypes = new SLFDeviceTypes(1, "RVI", "RVI", "");
            deviceTypesList.add(deviceTypes);
        }
        return deviceTypesList;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.slf_iv_second_right){
            //TODO gotoFeedbackFormat
            gotoFeedbackHistoryList();
        }else if(view.getId() == R.id.slf_help_and_feedback_chat_bot){
            //TODO gotoChatBot
            gotoChatBot();
        }else if(view.getId() == R.id.slf_help_and_feedback_feedback){
            gotoFeedback();
        }
    }

    private void gotoChatBot ( ) {
        Intent in = new Intent(getContext(), SLFChatBotActivity.class);
        startActivity(in);
    }

    private void gotoFeedback(){
        Intent in = new Intent(getContext(), SLFFeedbackSubmitActivity.class);
        startActivity(in);
    }
    private void gotoFeedbackHistoryList(){
        Intent in = new Intent(getContext(), SLFFeedbackList.class);
        startActivity(in);
    }
}
