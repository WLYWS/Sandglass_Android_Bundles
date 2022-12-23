package com.wyze.sandglasslibrary.commonui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseApplication;
import com.wyze.sandglasslibrary.utils.SLFCommonUtils;
import com.wyze.sandglasslibrary.utils.SLFConvertUtil;
import com.wyze.sandglasslibrary.utils.SLFImageUtil;
import com.wyze.sandglasslibrary.utils.SLFSystemUtil;
import com.wyze.sandglasslibrary.utils.SLFViewUtil;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

/**
 * created by yangjie
 * describe:Toast工具类,showloading等
 * time: 2022/12/22
 */
@SuppressLint("InflateParams")
public class SLFToastUtil {
    private static final String TAG = "SLFToastUtil";

    public static final int COMMON_DURATION = 3000;
    public static final int COMMON_SHARE_DURATION = 4500;
    public static final long SHORT_LOADING_DURATION = 30000;
    public static final long LONG_LOADING_DURATION = 180000;
    public static final int NOTICE_SHARING_FAILED = 10;

    private static Toast mToast;
    private static PopupWindow mPopWindow;
    private static PopupWindow mLoadingView;
    private static boolean isCanGoBack;
    private static CountDownTimer timer;
    private static CountDownTimer loadingTimer;

    private SLFToastUtil(){}

    public static void showLongText(CharSequence text) {
        showText(text, Toast.LENGTH_LONG);
    }

    public static void showText(CharSequence text) {
        showText(text, Toast.LENGTH_SHORT);
    }

    private static void showText(CharSequence text, int duration) {
        mToast = new Toast(SLFBaseApplication.getAppContext());
        @SuppressLint("InflateParams")
        View toastView = LayoutInflater.from(SLFBaseApplication.getAppContext()).inflate(R.layout.slf_common_toast, null);

        mToast.setGravity(Gravity.BOTTOM, 0, SLFConvertUtil.dp2px(45));
        mToast.setView(toastView);
        mToast.setDuration(duration);

        LinearLayout relativeLayout = mToast.getView().findViewById(R.id.slf_common_toast_parent);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(layoutParams);
        TextView tvContent = relativeLayout.findViewById(R.id.slf_common_toast_tv_content);
        tvContent.setText(text);
        tvContent.setLayoutParams(tvContent.getLayoutParams());
        relativeLayout.measure(0, 0);

        mToast.show();
    }

    @SuppressWarnings("deprecation")
    public static synchronized void showText(Activity act, CharSequence text) {

        if (mPopWindow == null) {
            //设置contentView
            @SuppressLint("InflateParams")
            View contentView = LayoutInflater.from(SLFBaseApplication.getAppContext()).inflate(R.layout.slf_common_toast, null);

            mPopWindow = new PopupWindow(contentView,
                    SLFCommonUtils.getScreenWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mPopWindow.setContentView(contentView);

            // 设置这两个属性
            mPopWindow.setOutsideTouchable(false);
            mPopWindow.setFocusable(false);

        }
        mPopWindow.dismiss();
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        final LinearLayout viewParent = mPopWindow.getContentView().findViewById(R.id.slf_common_toast_parent);


        //设置各个控件的点击响应
        TextView tvContent = viewParent.findViewById(R.id.tv_content);
        tvContent.setText(text);
        viewParent.measure(0,0);

        //显示的位置
        handler.postDelayed(() -> mPopWindow.showAtLocation(act.getWindow().getDecorView(),Gravity.BOTTOM, 0, SLFConvertUtil.dp2px(45)+ SLFSystemUtil.getNavigationBarHeight(act)), 150);

        if (timer != null) {
            timer.cancel();
            timer =  null;
        }
        timer = new CountDownTimer(COMMON_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //
            }

            @Override
            public void onFinish() {
                mPopWindow.dismiss();
            }
        }.start();

    }

    /**
     * @deprecated Recommend uniform use {@link #showText(CharSequence)} api.
     */
    @Deprecated
    public static void showCenterText(CharSequence text) {
        mToast = new Toast(SLFBaseApplication.getAppContext());
        @SuppressLint("InflateParams")
        View toastView = LayoutInflater.from(SLFBaseApplication.getAppContext()).inflate(R.layout.slf_common_toast, null);

        mToast.setGravity(Gravity.CENTER, 0, SLFConvertUtil.dp2px(45));
        mToast.setView(toastView);
        mToast.setDuration(Toast.LENGTH_SHORT);

        LinearLayout relativeLayout = mToast.getView().findViewById(R.id.slf_common_toast_parent);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(layoutParams);

        TextView tvContent = relativeLayout.findViewById(R.id.slf_common_toast_tv_content);
        tvContent.setText(text);
        tvContent.setLayoutParams(tvContent.getLayoutParams());
        relativeLayout.measure(0, 0);

        mToast.show();
    }

    /**
     * @param text           被显示的内容
     * @param pxMarginBottom 如果传入的值 <1 ,则采用默认的显示位置
     */
    public static void showToastWithMarginBottom(String text, int pxMarginBottom) {

        if (mToast != null) {
            mToast.cancel();
        }
        mToast = new Toast(SLFBaseApplication.getAppContext());
        View toastView = LayoutInflater.from(SLFBaseApplication.getAppContext()).inflate(R.layout.slf_common_toast,
                (ViewGroup) null);
        if (pxMarginBottom < 0) {
            pxMarginBottom = SLFConvertUtil.dp2px(45.0F);
        }

        mToast.setGravity(Gravity.BOTTOM, 0, pxMarginBottom);
        mToast.setView(toastView);
        mToast.setDuration(Toast.LENGTH_SHORT);
        LinearLayout relativeLayout = (LinearLayout) mToast.getView().findViewById(R.id.slf_common_toast_parent);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        relativeLayout.setLayoutParams(layoutParams);
        TextView tvContent = (TextView) relativeLayout.findViewById(R.id.slf_common_toast_tv_content);
        tvContent.setText(text);
        tvContent.setLayoutParams(tvContent.getLayoutParams());
        relativeLayout.measure(0, 0);
        mToast.show();

    }

    private static int count = 0;
    private static final Handler handler = new Handler();
    @SuppressLint("StaticFieldLeak")
    private static TextView tvContent;
    public static final int LOADING_TYPE_WHITE = 1;
    public static final int LOADING_TYPE_BLACK = 2;
    public static final int LOADING_TYPE_WHITE2 = 3;
    public static final int LOADING_TYPE_GREY = 4;
    public static  ImageView ivLoading;

    public static void showLoading(final Activity act) {
        showLoading(act, false, LOADING_TYPE_WHITE,SHORT_LOADING_DURATION);
    }

    public static void showLoading(final Activity act,boolean isGoBack) {
        showLoading(act,isGoBack, LOADING_TYPE_WHITE,SHORT_LOADING_DURATION);
    }

    public static void showLoading(final Activity act,int type) {
        showLoading(act,false,type,SHORT_LOADING_DURATION);
    }

    public static void showLoading(final Activity act,int type,boolean isGoBack) {
        showLoading(act,isGoBack,type,SHORT_LOADING_DURATION);
    }

    public static void showCommonLoading(final Activity act,boolean isGoBack){
        showCommonLoading(act,isGoBack, LOADING_TYPE_WHITE2,SHORT_LOADING_DURATION);
    }

    public static void showCommonLoading(final Activity act,int type){
        showCommonLoading(act,false,type,SHORT_LOADING_DURATION);
    }

    public static void showCommonLoading(final Activity act,int type,boolean isGoBack){
        showCommonLoading(act,isGoBack,type,SHORT_LOADING_DURATION);
    }

    @SuppressWarnings({"deprecation", "java:S3776"})
    public static void showCommonLoading(final Activity act, boolean isGoBack, int type, final long duration) {
        if(act == null || act.getWindow()==null){
            return;
        }

        if(mLoadingView == null){
            //设置contentView
            View contentView = LayoutInflater.from(SLFBaseApplication.getAppContext()).inflate(R.layout.slf_common_loading, null);
            mLoadingView = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mLoadingView.setContentView(contentView);
            //当点击popwindow以外的地方关闭窗口
            mLoadingView.setBackgroundDrawable(new BitmapDrawable());
            // 设置这两个属性
            mLoadingView.setOutsideTouchable(false);
            mLoadingView.setFocusable(false);
            mLoadingView.setClippingEnabled(false);
        }

            if (type == LOADING_TYPE_WHITE) {
                //设置contentView
                @SuppressLint("InflateParams") View contentView = LayoutInflater.from(SLFBaseApplication.getAppContext()).inflate(R.layout.slf_common_loading, null);
                final RelativeLayout relativeLayout = contentView.findViewById(R.id.slf_common_loading_parent);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(SLFConvertUtil.dp2px(136), SLFConvertUtil.dp2px(136));
                relativeLayout.setLayoutParams(layoutParams);
                mLoadingView.setContentView(contentView);
                //当点击popwindow以外的地方关闭窗口
                tvContent = contentView.findViewById(R.id.slf_common_loading_tv_content);
                ivLoading = contentView.findViewById(R.id.slf_common_laoding_iv_loading);
                SLFImageUtil.loadDefaultImage(act, R.drawable.slf_common_loading_img, ivLoading);
                SLFViewUtil.slfImageViewStartAnim(ivLoading);
            }else if (type == LOADING_TYPE_BLACK){
                //设置contentView
            }else if (type == LOADING_TYPE_WHITE2) {
                //设置contentView
            }else if (type == LOADING_TYPE_GREY) {
                //设置contentView

            }


        isCanGoBack = isGoBack;


        try {
            mLoadingView.showAtLocation(act.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

            count = 0;
            if (loadingTimer != null) {
                loadingTimer.cancel();
                loadingTimer =  null;
            }
            loadingTimer = new CountDownTimer(duration, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if(tvContent == null) {
                        return;
                    }
                    count++;
                    if (count % 4 == 1) {
                        tvContent.setText(act.getResources().getString(R.string.loading1));
                    } else if (count % 4 == 2) {
                        tvContent.setText(act.getResources().getString(R.string.loading2));
                    } else if (count % 4 == 3) {
                        tvContent.setText(act.getResources().getString(R.string.loading3));
                    } else if (count % 4 == 0) {
                        tvContent.setText(act.getResources().getString(R.string.loading4));
                    }
                }

                @Override
                public void onFinish() {
                    mLoadingView.dismiss();
                }
            }.start();

        }catch (Exception e){
            SLFLogUtil.e(TAG, Log.getStackTraceString(e));
        }


    }

    @SuppressWarnings("deprecation")
    public static void showLoading(final Activity act, boolean isGoBack,int type, long duration) {
        if(act == null || act.getWindow()==null){
            return;
        }

        if(mLoadingView == null){
            //设置contentView
            @SuppressLint("InflateParams") View contentView = LayoutInflater.from(SLFBaseApplication.getAppContext()).inflate(R.layout.slf_common_loading, null);
            mLoadingView = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mLoadingView.setContentView(contentView);
            //当点击popwindow以外的地方关闭窗口
            mLoadingView.setBackgroundDrawable(new BitmapDrawable());
            // 设置这两个属性
            mLoadingView.setOutsideTouchable(false);
            mLoadingView.setFocusable(false);
            mLoadingView.setClippingEnabled(false);
        }

        if (type == LOADING_TYPE_WHITE) {
            //设置contentView
            View contentView = LayoutInflater.from(SLFBaseApplication.getAppContext()).inflate(R.layout.slf_common_loading, null);
            final RelativeLayout relativeLayout = contentView.findViewById(R.id.slf_common_loading_parent);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(SLFConvertUtil.dp2px(136), SLFConvertUtil.dp2px(136));
            relativeLayout.setLayoutParams(layoutParams);
            mLoadingView.setContentView(contentView);
            //当点击popwindow以外的地方关闭窗口
            tvContent = contentView.findViewById(R.id.slf_common_loading_tv_content);
            ImageView ivLoading = contentView.findViewById(R.id.slf_common_laoding_iv_loading);
            SLFImageUtil.loadDefaultImage(act, R.drawable.slf_common_loading_img, ivLoading);
            SLFViewUtil.slfImageViewStartAnim(ivLoading);
        }else if (type == LOADING_TYPE_BLACK){
            //设置contentView
        }else if (type == LOADING_TYPE_WHITE2) {
            //设置contentView
        }else if (type == LOADING_TYPE_GREY) {
            //设置contentView
        }


        isCanGoBack = isGoBack;

        act.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLoadingView.showAtLocation(act.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                act.getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        count = 0;
        if (loadingTimer != null) {
            loadingTimer.cancel();
            loadingTimer =  null;
        }
        loadingTimer = new CountDownTimer(duration, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

                count++;
                if (count % 4 == 1) {
                    tvContent.setText(act.getResources().getString(R.string.loading1));
                } else if (count % 4 == 2) {
                    tvContent.setText(act.getResources().getString(R.string.loading2));
                } else if (count % 4 == 3) {
                    tvContent.setText(act.getResources().getString(R.string.loading3));
                } else if (count % 4 == 0) {
                    tvContent.setText(act.getResources().getString(R.string.loading4));
                }
            }

            @Override
            public void onFinish() {
                mLoadingView.dismiss();
            }
        }.start();

    }



    public static void hideCommonLoading() {
        if(ivLoading!=null) {
            SLFViewUtil.slfImageViewCancelAnim(ivLoading);
        }
        if (mLoadingView != null) {
            if (loadingTimer != null) {
                loadingTimer.cancel();
            }
            handler.removeCallbacksAndMessages(null);
            mLoadingView.dismiss();
        }
    }

    public static void hideLoading() {
        if(ivLoading!=null) {
            SLFViewUtil.slfImageViewCancelAnim(ivLoading);
        }
        if (mLoadingView != null) {
            handler.postDelayed(() -> {
                if (loadingTimer != null) {
                    loadingTimer.cancel();
                }
                mLoadingView.dismiss();
            }, 500);
        }


    }

    public static void setOnLoadingDismissListener(PopupWindow.OnDismissListener listener){
        if(mLoadingView!=null){
            mLoadingView.setOnDismissListener(listener);
        }
    }

    public static void removeLoadingDismissListener(){
        if(mLoadingView!=null){
            mLoadingView.setOnDismissListener(null);
        }
    }
    public static boolean isLoading() {
        return mLoadingView != null && mLoadingView.isShowing();
    }

    public static boolean isCanGoBack() {
        return isCanGoBack;
    }





    public static void cancelToast(){
        if(mToast!=null){
            mToast.cancel();
        }
    }


}