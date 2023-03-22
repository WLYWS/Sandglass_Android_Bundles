package com.sandglass.sandglasslibrary.functionmoudle.activity.helpAndFeedback;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackSubmitActivity;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqDetailBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqDetailResponseBean;
import com.sandglass.sandglasslibrary.net.SLFApiContant;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestCallback;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestConstants;
import com.sandglass.sandglasslibrary.net.SLFHttpUtils;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Greated by yangjie
 * describe:问题详情页
 * time:2023/2/23
 */
public class SLFHelpAndFeedbackDetail<T> extends SLFBaseActivity implements View.OnClickListener, SLFHttpRequestCallback<T> {

    private WebView mWebView;

    private Button mFeedbackBtn;

    private String titleName;

    private long faq_id;

    private TextView slfTitle;

    private ImageView slfBack;

    private SLFFaqDetailBean slfFaqDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SLFStatusBarColorChange.transparencyBar(this);
        setContentView(R.layout.slf_faq_detail);
        initView();
        initData();
        initTitle();

    }

    private void initView(){
        mWebView = findViewById(R.id.slf_faq_detail_web);
        mFeedbackBtn = findViewById(R.id.slf_faq_to_feedback);
        mFeedbackBtn.setOnClickListener(this);
        SLFFontSet.setSLF_MediumFontt(getContext(),mFeedbackBtn);
        WebSettings settings = mWebView.getSettings();
        // 設置WebView支持JavaScript
        settings.setJavaScriptEnabled(true);
        //支持自動適配
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);  //支持放大縮小
        settings.setBuiltInZoomControls(true); //顯示縮放按鈕
        settings.setBlockNetworkImage(true);// 把圖片加載放在最後來加載渲染
        settings.setAllowFileAccess(true); // 容許訪問文件
        settings.setSaveFormData(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);/// 支持經過JS打開新窗口
//        Typeface typeFace =Typeface.createFromAsset(getContext().getAssets(),"fonts/Rany.otf");
//        settings.setfont(getAssets()+"/fonts/Rany.otf");
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE,null);//开启硬件加速
        //設置不讓其跳轉瀏覽器
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        // 添加客戶端支持
        mWebView.setWebChromeClient(new WebChromeClient());
        // mWebView.loadUrl(TEXTURL);

        //不加這個圖片顯示不出來
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.getSettings().setBlockNetworkImage(false);

        //容許cookie 否則有的網站沒法登錄
        CookieManager mCookieManager = CookieManager.getInstance();
        mCookieManager.setAcceptCookie(true);
        mCookieManager.setAcceptThirdPartyCookies(mWebView, true);
    }

    private void initData(){
        titleName =getIntent().getStringExtra(SLFConstants.FAQ_TITLE_NAME);
        faq_id = getIntent().getLongExtra(SLFConstants.FAQ_ID,-1);
        if(null==titleName){
            titleName = "";
        }
        if(faq_id!=-1){
            getFaqDetaiData();
        }else{
            showToast("faq id is error");
        }
    }

    private void initTitle(){
        slfTitle = findViewById(R.id.slf_tv_title_name);
        slfBack = findViewById(R.id.slf_iv_back);
        slfTitle.setText(titleName);
        SLFFontSet.setSLF_MediumFontt(getContext(),slfTitle);
        slfBack.setOnClickListener(this);
    }

    private void getFaqDetaiData(){
        SLFLogUtil.sdkd("yj","url::"+SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_FAQ_DETAIL+faq_id);
        SLFHttpUtils.getInstance().executePathGet(getContext(),
                SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_FAQ_DETAIL+faq_id, SLFFaqDetailResponseBean.class, this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.slf_iv_back){
            finish();
        }else if(view.getId() == R.id.slf_faq_to_feedback){
            gotoFeedback();
        }
    }

    private void gotoFeedback(){
        Intent in = new Intent(getContext(), SLFFeedbackSubmitActivity.class);
        startActivity(in);
    }

    @Override
    public void onRequestNetFail(T type) {

    }

    @Override
    public void onRequestSuccess(String result, T type) {
        if(type instanceof SLFFaqDetailResponseBean){
            String replaceAll;
            slfFaqDetailBean = ((SLFFaqDetailResponseBean) type).data;
            String content = slfFaqDetailBean.getContent();
            replaceAll = content.replaceAll("<img ","<img style=\"max-width:100%;height:auto\" ");

            mWebView.loadDataWithBaseURL(null,replaceAll,"text/html","utf-8",null);
        }
    }

    public List<String> getImgSrc(String htmlStr) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();
// String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img ="<img[^>]*/>";
        p_image = Pattern.compile(regEx_img);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            pics.add(m_image.group(1));
// Matcher m =
        }
        return pics;
    }

    private String changeStyle(String content){
        String regex = "(?<head><img\\s+alt\\s*=\\s*\"\"\\s+)style\\s*=\\s*\"([^\"]|\\\\\")+\"\\s*(?<tail>/>)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if(matcher.matches()){
            System.out.println("get here");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                content = matcher.group("head") + matcher.group("tail");
            }
        }
        return content;
    }

    @Override
    public void onRequestFail(String value, String failCode, T type) {
        SLFLogUtil.sdke("yj","error____");
    }
}
