package com.sandglass.sandglasslibrary.functionmoudle.activity.feedback;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.base.SLFBaseApplication;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.sandglass.sandglasslibrary.utils.SLFStringFormatUtil;

/**
 * Greated by yangjie
 * describe:反馈成功页
 * time:2022/12/21
 */
public class SLFFeedbackSuccessActivity extends SLFBaseActivity {

    private TextView slf_logid;

    private TextView slf_copy;

    private TextView slf_feed_back_succeeded;

    private Button slf_finish_Btn;

    private int logId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_feedback_success);
        logId = getIntent().getIntExtra(SLFConstants.LOGID,-1);
        initTitle();
        initView();
    }


    private void initTitle(){
        ImageView iv_Back = findViewById(R.id.slf_iv_back);
        iv_Back.setVisibility(View.GONE);
        TextView iv_title = findViewById(R.id.slf_tv_title_name);
        iv_title.setText(SLFResourceUtils.getString(R.string.slf_feedback_title_content));
        SLFFontSet.setSLF_RegularFont(getContext(),iv_title);
    }

    private void initView(){
        slf_logid = findViewById(R.id.slf_feed_back_sucess_logid);
        slf_copy = findViewById(R.id.slf_feed_back_success_copy);
        slf_finish_Btn = findViewById(R.id.slf_success_finish);
        slf_feed_back_succeeded = findViewById(R.id.slf_feed_back_succeeded);
        slf_logid.setText(SLFStringFormatUtil.getFormatString(R.string.slf_feedback_success_center_logid,logId));
        slf_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm =(ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(slf_logid.getText().toString());
                showCenterToast(SLFResourceUtils.getString(R.string.slf_feedback_submit_success_copy));
            }
        });

        slf_finish_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SLFBaseApplication.exitAllActivity();
                finish();
            }
        });
        SLFFontSet.setSLF_MediumFontt(getContext(),slf_feed_back_succeeded);
        SLFFontSet.setSLF_RegularFont(getContext(),slf_logid);
        SLFFontSet.setSLF_RegularFont(getContext(),slf_copy);
        SLFFontSet.setSLF_MediumFontt(getContext(),slf_finish_Btn);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            SLFBaseApplication.exitAllActivity();
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}