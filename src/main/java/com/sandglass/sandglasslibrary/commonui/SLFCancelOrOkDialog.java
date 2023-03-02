package com.sandglass.sandglasslibrary.commonui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.uiutils.SLFClickableSpan;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;

public class SLFCancelOrOkDialog extends Dialog {

    private final TextView tvTitle;
    private final TextView tvContent;
    private final TextView tbLeft;
    private final TextView tbRight;
    private final TextView tvOk;
    private final TextView tvCancel;

    private OnHintDialogListener mListener;

    public static final int STYLE_CANCEL_OK = 0;
    public static final int STYLE_ONLY_OK = 1;
    public static final int STYLE_ALL = 2;

    public SLFCancelOrOkDialog(Context context, final int style) {
        super(context, R.style.slf_dialog_style);
        setContentView(R.layout.slf_cancel_or_ok_common_dialog);
        // 设置宽高
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tbLeft = findViewById(R.id.tb_left);
        tbRight = findViewById(R.id.tb_right);
        tvOk = findViewById(R.id.tv_ok);
        tvCancel = findViewById(R.id.tv_cancel);
        SLFFontSet.setSLF_RegularFont(context,tvTitle);
        SLFFontSet.setSLF_RegularFont(context,tvContent);
        SLFFontSet.setSLF_RegularFont(context,tbLeft);
        SLFFontSet.setSLF_RegularFont(context,tbRight);
        SLFFontSet.setSLF_RegularFont(context,tvCancel);
        tvContent.setVisibility(View.GONE);

        switch (style) {
            case STYLE_CANCEL_OK:
                tvOk.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
                findViewById(R.id.view_line_1).setVisibility(View.GONE);
                findViewById(R.id.view_line_2).setVisibility(View.GONE);
                break;
            case STYLE_ONLY_OK:
                tvOk.setVisibility(View.GONE);
                tvCancel.setVisibility(View.GONE);
                tbLeft.setVisibility(View.GONE);
                findViewById(R.id.view_stand).setVisibility(View.GONE);
                findViewById(R.id.view_line_1).setVisibility(View.GONE);
                findViewById(R.id.view_line_2).setVisibility(View.GONE);
                break;
            case STYLE_ALL:
                tbLeft.setVisibility(View.GONE);
                findViewById(R.id.view_stand).setVisibility(View.GONE);
                tbRight.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }


        tbLeft.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onClickCancel();
                dismiss();
            }
        });
        tbRight.setOnClickListener(v -> {
            if (null != mListener) {
                if (style == STYLE_ALL) {
                    mListener.onClickOther();
                } else {
                    mListener.onClickOk();
                }
                dismiss();
            }
        });
        tvOk.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onClickOk();
                dismiss();
            }
        });

        tvCancel.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onClickCancel();
                dismiss();
            }
        });

        setCancelable(false);
        setCanceledOnTouchOutside(false);

//        mHintDialog = this;
    }

//    private static WpkHintDialog mHintDialog;

    public static SLFCancelOrOkDialog create(Context context, final int style) {
        return new SLFCancelOrOkDialog(context, style);
    }

    public SLFCancelOrOkDialog setTitle(String titleContent) {
        setTitleText(titleContent);
        return this;
    }

    public SLFCancelOrOkDialog hideTitle(boolean isHideTitle) {
        if (isHideTitle) {
            hideTitle();
        } else {
            showTitle();
        }
        return this;
    }

    public SLFCancelOrOkDialog showTitle(boolean isShow) {
        if (isShow) {
            showTitle();
        } else {
            hideTitle();
        }
        return this;
    }



    public SLFCancelOrOkDialog setContent(String content) {
        setContentText(content);
        return this;
    }

    public SLFCancelOrOkDialog setContent(String content, String clickText, SLFClickableSpan.OnClickListener listener){
        if(TextUtils.isEmpty(content) || TextUtils.isEmpty(clickText)){
            return this;
        }
        int index = content.lastIndexOf(clickText);
        if(index<0){
            tvContent.setText(content);
            return this;
        }
        SpannableString mSpannableString = new SpannableString(content);
        mSpannableString.setSpan(new ForegroundColorSpan(SLFResourceUtils.getColor(R.color.slf_feedback_page_submit_btn_bg)),index,index+clickText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mSpannableString.setSpan(new StyleSpan(Typeface.BOLD),index,index+clickText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ClickableSpan clickableSpan = new SLFClickableSpan(listener);
        mSpannableString.setSpan(clickableSpan,index,index+clickText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setHighlightColor(Color.TRANSPARENT);
        tvContent.setText(mSpannableString);
        tvContent.setVisibility(View.VISIBLE);
        return this;
    }

    public SLFCancelOrOkDialog hideContent(boolean isHideContent) {
        if (isHideContent) {
            hideContent();
        } else {
            showContent();
        }
        return this;
    }


    public SLFCancelOrOkDialog setLeftBtnText(String text) {
        setLeftText(text);
        return this;
    }

    public SLFCancelOrOkDialog setRightBtnText(String text) {
        setRightText(text);
        return this;
    }

    public SLFCancelOrOkDialog setRightBtnColor(int color) {
        tbRight.setTextColor(color);
        return this;
    }

    public SLFCancelOrOkDialog setLeftBtnColor(int color) {
        tbLeft.setTextColor(color);
        return this;
    }

    public SLFCancelOrOkDialog setMiddleColor(int color) {
        tvOk.setTextColor(color);
        return this;
    }

    public void setDoneSizeAndBold(int size, boolean isBold) {
        if (tvOk == null || tvCancel == null || tbRight == null || tbLeft == null) {
            return;
        }
        tvOk.setTextSize(size);
        tvCancel.setTextSize(size);
        tbRight.setTextSize(size);
        tbLeft.setTextSize(size);
        if (isBold) {
            tvOk.setTypeface(Typeface.DEFAULT_BOLD);
            tvCancel.setTypeface(Typeface.DEFAULT_BOLD);
            tbRight.setTypeface(Typeface.DEFAULT_BOLD);
            tbLeft.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    public SLFCancelOrOkDialog setTopBtnText(String text) {
        setTopText(text);
        return this;
    }

    public SLFCancelOrOkDialog setMiddleBtnText(String text) {
        setMiddleText(text);
        return this;
    }

    public SLFCancelOrOkDialog setBottomBtnText(String text) {
        setBottomText(text);
        return this;
    }

    public SLFCancelOrOkDialog setDialogListener(SimpleOnHintDialogListener listener) {
        setOnListener(listener);
        return this;
    }

    public SLFCancelOrOkDialog setDialogListener(OnHintDialogListener listener) {
        setOnListener(listener);
        return this;
    }


    public void showDialog() {
        show();
    }


    public void hideTitle() {

        tvTitle.setVisibility(View.GONE);
    }

    public void showTitle() {
        tvTitle.setVisibility(View.VISIBLE);
    }

    public void setTopText(String text) {
        tbRight.setText(text);
        tbRight.setVisibility(View.VISIBLE);
    }

    //文字加粗
    public void setTopTextBold() {
        tbRight.setTypeface(Typeface.DEFAULT_BOLD);
    }

    //文字加粗
    public void setTitleNonBold() {
        tvTitle.setTypeface(Typeface.DEFAULT);
    }

    public void setTitleBold() {
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setMiddleText(String text) {
        tvOk.setText(text);
        tvOk.setVisibility(View.VISIBLE);
    }

    public void setBottomText(String text) {
        tvCancel.setText(text);
        tvCancel.setVisibility(View.VISIBLE);
    }

    public void hideContent() {
        tvContent.setVisibility(View.GONE);
    }

    public void showContent() {
        tvContent.setVisibility(View.VISIBLE);
    }

    public void setContentText(CharSequence contentText) {
        tvContent.setText(contentText);
        tvContent.setVisibility(View.VISIBLE);
    }

    public void setTitleText(String titleText) {
        tvTitle.setText(titleText);
        tvTitle.setVisibility(View.VISIBLE);
    }

    public void addContentText(CharSequence contentText) {
        tvContent.setVisibility(View.VISIBLE);
        tvContent.append(contentText);
    }

    public void addContentText(CharSequence contentText, int color) {
        tvContent.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(contentText)) contentText = " ";
        SpannableString spanString = new SpannableString(contentText);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spanString.setSpan(span, 0, contentText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvContent.append(spanString);
    }

    public void addContentText(CharSequence contentText, int color, int sizeDp) {
        tvContent.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(contentText)) contentText = " ";
        SpannableString spanString = new SpannableString(contentText);
        CharacterStyle span = new ForegroundColorSpan(color);
        spanString.setSpan(span, 0, contentText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        span = new AbsoluteSizeSpan((int) (sizeDp * getContext().getResources().getDisplayMetrics().density));
        spanString.setSpan(span, 0, contentText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvContent.append(spanString);
    }

    public TextView getContentText() {
        return tvContent;
    }

    public void setLeftText(String leftText) {
        tbLeft.setVisibility(View.VISIBLE);
        tbLeft.setText(leftText);
    }

    public void hideLeft() {
        findViewById(R.id.view_stand).setVisibility(View.GONE);
        tbLeft.setVisibility(View.GONE);
    }

    public void showLeft() {
        tbLeft.setVisibility(View.VISIBLE);
    }

    public void setRightText(String rightText) {
        tbRight.setVisibility(View.VISIBLE);
        tbRight.setText(rightText);
    }

    public void hideRight() {
        findViewById(R.id.view_line_top).setVisibility(View.GONE);
        findViewById(R.id.view_stand).setVisibility(View.GONE);
        tbRight.setVisibility(View.GONE);
    }

    public void setOnListener(OnHintDialogListener listener) {
        this.mListener = listener;
    }


    /**
     * 设置回调接口
     */
    @SuppressWarnings("java:S100")
    public interface OnHintSureListener {
        void OnHintSure();
    }

    /**
     * 设置回调接口
     */

    public static class SimpleOnHintDialogListener implements OnHintDialogListener {
        @Override
        public void onClickOk() {
            //empty
        }

        @Override
        public void onClickCancel() {
            //empty
        }

        @Override
        public void onClickOther() {
            //empty
        }
    }

    /**
     * 设置回调接口
     */

    public interface OnHintDialogListener {
        public void onClickOk();

        public void onClickCancel();

        public void onClickOther();
    }
}
