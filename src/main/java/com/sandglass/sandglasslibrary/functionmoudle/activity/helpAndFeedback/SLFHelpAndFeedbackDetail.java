package com.sandglass.sandglasslibrary.functionmoudle.activity.helpAndFeedback;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.base.SLFBaseActivity;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.bean.SLFSPContant;
import com.sandglass.sandglasslibrary.commonui.SLFScrollView;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackSubmitActivity;
import com.sandglass.sandglasslibrary.moudle.event.SLFFinishActivity;
import com.sandglass.sandglasslibrary.moudle.event.SLFUpdateFaqCategoryEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFUpdateFaqDetailEvent;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqDetailBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqDetailResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQResponseBean;
import com.sandglass.sandglasslibrary.net.SLFApiContant;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestCallback;
import com.sandglass.sandglasslibrary.net.SLFHttpRequestConstants;
import com.sandglass.sandglasslibrary.net.SLFHttpUtils;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.uiutils.SLFStatusBarColorChange;
import com.sandglass.sandglasslibrary.utils.SLFSpUtils;
import com.sandglass.sandglasslibrary.utils.SLFCommonUtils;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;
import com.sandglass.sandglasslibrary.utils.manager.SLFCacheToFileManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    private static final String CACHE_FILE_PATH = SLFConstants.rootPath+"/updatefaqdetail/";
    private SLFCacheToFileManager <SLFFaqDetailResponseBean> cacheManager;
    private LinearLayout slf_faq_detail_no_item_linear;
    private TextView slf_faq_detail_no_feedback;
    private TextView slf_faq_detail_no_network;
    private Button slf_faq_detail_try_again;
    private  String content;
    private SLFScrollView slf_feed_back_scroll_view;

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
        slf_feed_back_scroll_view = findViewById(R.id.slf_feed_back_scroll_view);
        mWebView = findViewById(R.id.slf_faq_detail_web);
        mFeedbackBtn = findViewById(R.id.slf_faq_to_feedback);
        slf_faq_detail_no_item_linear = findViewById(R.id.slf_faq_detail_no_item_linear);
        slf_faq_detail_no_feedback = findViewById(R.id.slf_faq_detail_no_feedback);
        slf_faq_detail_no_network = findViewById(R.id.slf_faq_detail_no_network);
        slf_faq_detail_try_again = findViewById(R.id.slf_faq_detail_try_again);
        mFeedbackBtn.setOnClickListener(this);
        mFeedbackBtn.setVisibility(View.GONE);
        SLFFontSet.setSLF_MediumFontt(getContext(),mFeedbackBtn);
        WebSettings settings = mWebView.getSettings();
        // 設置WebView支持JavaScript
        settings.setJavaScriptEnabled(true);
        //支持自動適配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(mWebView, true);
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.setDrawingCacheEnabled(false);
        settings.setLoadWithOverviewMode(true);
//        settings.setBlockNetworkImage(true);// 把圖片加載放在最後來加載渲染
//        settings.setAllowFileAccess(true); // 容許訪問文件
//        settings.setSaveFormData(true);
//        settings.setGeolocationEnabled(true);
//        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);/// 支持經過JS打開新窗口
//        Typeface typeFace =Typeface.createFromAsset(getContext().getAssets(),"fonts/Rany.otf");
//        settings.setfont(getAssets()+"/fonts/Rany.otf");
//        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);//开启硬件加速
        //設置不讓其跳轉瀏覽器


        // 添加客戶端支持
//        mWebView.setWebChromeClient(new WebChromeClient());
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
        slf_faq_detail_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
    }

    private void chageView(){
        if(SLFCommonUtils.isNetworkAvailable(getActivity())) {
            if (content == null) {
                slf_feed_back_scroll_view.setVisibility(View.GONE);
                slf_faq_detail_no_item_linear.setVisibility(View.VISIBLE);
                slf_faq_detail_no_feedback.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_no_item));
                slf_faq_detail_no_feedback.setTextColor(SLFResourceUtils.getColor(R.color.slf_first_page_top_text_normal));
                slf_faq_detail_no_network.setVisibility(View.GONE);
                slf_faq_detail_try_again.setVisibility(View.GONE);
            } else {
                slf_feed_back_scroll_view.setVisibility(View.VISIBLE);
                slf_faq_detail_no_item_linear.setVisibility(View.GONE);
            }
        }else{
            slf_feed_back_scroll_view.setVisibility(View.GONE);
            slf_faq_detail_no_item_linear.setVisibility(View.VISIBLE);
            slf_faq_detail_no_feedback.setText(SLFResourceUtils.getString(R.string.slf_first_page_no_network));
            slf_faq_detail_no_feedback.setTextColor(SLFResourceUtils.getColor(R.color.slf_first_page_no_network_warning));
            slf_faq_detail_no_network.setVisibility(View.VISIBLE);
            slf_faq_detail_try_again.setVisibility(View.VISIBLE);
        }
    }

    private void initData(){
        cacheManager = new SLFCacheToFileManager(SLFFaqDetailResponseBean.class);
        titleName =getIntent().getStringExtra(SLFConstants.FAQ_TITLE_NAME);
        faq_id = getIntent().getLongExtra(SLFConstants.FAQ_ID,-1);
        if(null==titleName){
            titleName = "";
        }
        if(faq_id!=-1){
            SLFFaqDetailResponseBean sLFFaqDetailResponseBean = cacheManager.readObj(CACHE_FILE_PATH+"slf_faq_detail_"+faq_id);
            if (sLFFaqDetailResponseBean!=null){
                if (SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FAQDETAIL_CACHE,0)!=SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FAQDETAIL,-1)){
                    getFaqDetaiData();
                }else {
                    showContent(sLFFaqDetailResponseBean);
                }
            }else {
                getFaqDetaiData();
            }
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
        showLoading();
        SLFLogUtil.sdkd("yj","url::"+ SLFHttpRequestConstants.BASE_URL + SLFApiContant.FEEDBACK_FAQ_DETAIL+faq_id);
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
        in.putExtra(SLFConstants.FROM_FAQ,"faqfrom");
        startActivity(in);
    }

    @Override
    public void onRequestNetFail(T type) {
        chageView();
    }

    @Override
    public void onRequestSuccess(String result, T type) {
        if(type instanceof SLFFaqDetailResponseBean){
            if (SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FAQDETAIL_CACHE,0)!=SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FAQDETAIL,-1)) {
                cacheManager.delete(CACHE_FILE_PATH);
            }
            SLFSpUtils.put(SLFSPContant.UPDATE_TIME_FAQDETAIL,SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FAQDETAIL_CACHE,0));
            cacheManager.saveObj(CACHE_FILE_PATH+"slf_faq_detail_"+faq_id,(SLFFaqDetailResponseBean)type);
            //showContent((SLFFaqDetailResponseBean) type);
            Message msg = handler.obtainMessage();
            msg.what = 10032;
            msg.obj = (SLFFaqDetailResponseBean)type;
            handler.sendMessageDelayed(msg,300);
        }
    }

    Handler handler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 10032:
                    showContent((SLFFaqDetailResponseBean)msg.obj);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mFeedbackBtn.setVisibility(View.VISIBLE);
                        }
                    },300);
                    break;
            }
            return false;
        }
    });


    private void showContent (SLFFaqDetailResponseBean type) {
        String replaceAll;
        slfFaqDetailBean = ((SLFFaqDetailResponseBean) type).data;
        content = slfFaqDetailBean.getContent();

        replaceAll = content.replaceAll("<img ","<img style=\"max-width:100%;height:auto\" ");
        hideLoading();
        mWebView.loadDataWithBaseURL(null,replaceAll,"text/html","utf-8",null);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadDataWithBaseURL(null,replaceAll,"text/html","utf-8",null);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        chageView();

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
        chageView();
        SLFLogUtil.sdke("yj","error____");
    }

    /**
     * 接受缓存更新事件，要更新缓存
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFUpdateFaqDetailEvent event) {
        SLFSpUtils.put(SLFSPContant.UPDATE_TIME_FAQDETAIL_CACHE,event.updateTime);
        getFaqDetaiData();
    }

    /**
     * 关闭页面
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SLFFinishActivity event) {
        if(event.finish){
            finish();
        }
    }
}
