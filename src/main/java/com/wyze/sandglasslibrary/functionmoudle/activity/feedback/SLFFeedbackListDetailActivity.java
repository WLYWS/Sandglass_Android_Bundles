package com.wyze.sandglasslibrary.functionmoudle.activity.feedback;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseActivity;
import com.wyze.sandglasslibrary.functionmoudle.adapter.SLFFragmentAdapter;
import com.wyze.sandglasslibrary.functionmoudle.fragment.SLFFeedbackAllHistoryFragment;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFRecode;
import com.wyze.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

/**
 * Greated by yangjie
 * describe:反馈列表详情页
 * time:2023/1/30
 */
public class SLFFeedbackListDetailActivity extends SLFBaseActivity implements View.OnClickListener{
    /**处理状态*/
    private TextView slf_title_status;
    /**反馈id*/
    private TextView slf_feedback_id;
    /**问题类型*/
    private TextView slf_feedback_question_type;
    /**留言列表*/
    private RecyclerView slf_feedback_leave_list;
    /**底部继续留言按钮*/
    private RelativeLayout slf_feedback_bottom_relative;
    /**留言列表adapter*/

    /**留言列表的数据*/

    /**传过来的反馈对象*/
    private SLFRecode slfRecode;
    /**状态栏文字*/
    private TextView slfTitle;
    /**状态栏返回按钮*/
    private ImageView slfBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_feedback_list_detail);
        initData();
        initTitleBar();
        initView();
    }
    /**初始化数据*/
    private void initData(){

    }

    /**初始化view*/
    private void initView(){
       slf_title_status = findViewById(R.id.slf_feedback_list_item_title_status);
       slf_feedback_id = findViewById(R.id.slf_feedback_list_item_title_id);
       slf_feedback_question_type = findViewById(R.id.slf_feedback_list_item_type);
       slf_feedback_leave_list = findViewById(R.id.slf_feedback_list_leave_message_list);
       slf_feedback_bottom_relative = findViewById(R.id.slf_feedback_list_bottom_relative);
    }
    /**初始化statusBar*/
    private void initTitleBar(){
        slfTitle = findViewById(R.id.slf_tv_title_name);
        slfBack = findViewById(R.id.slf_iv_back);
        slfTitle.setText(getResources().getText(R.string.slf_feedback_list_leave_message_title_text));
        slfBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.slf_iv_back){
            finish();
        }
    }
}
