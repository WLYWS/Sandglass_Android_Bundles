package com.sandglass.sandglasslibrary.functionmoudle.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.commonapi.SLFCommonUpload;
import com.sandglass.sandglasslibrary.commonui.SLFProgressView;
import com.sandglass.sandglasslibrary.moudle.SLFMediaData;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.utils.SLFAdapterUtils;
import com.sandglass.sandglasslibrary.utils.SLFImageShapes;
import com.sandglass.sandglasslibrary.utils.SLFImageUtil;
import com.sandglass.sandglasslibrary.utils.SLFStringFormatUtil;
import com.sandglass.sandglasslibrary.utils.videocompress.SLFVideoSlimmer;

import java.util.List;

/**
 * created by yangjie
 * describe:选择图片控件adapter实现
 * time: 2022/12/6
 */
public class SLFAndPhotoAdapter extends SLFQuickAdapter<SLFMediaData> {

    private Context mContext;

    private long progress;

    private SLFProgressView progressView;

    private static final int DELETECHANGE = 20230427;

    public SLFAndPhotoAdapter(Context context, List<SLFMediaData> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    public void convert(final SLFAdapterUtils helper, final SLFMediaData object, int position) {

        if (getCount() >= 4 && position == 3) {
            helper.getView().setVisibility(View.GONE);
        } else {
            helper.getView().setVisibility(View.VISIBLE);
            helper.setVisible(R.id.slf_iv_photo_fail, false);
            helper.setVisible(R.id.slf_center_addimg, true);
            helper.setVisible(R.id.slf_photo_count, true);
            helper.setText(R.id.slf_photo_count, SLFStringFormatUtil.getFormatString(R.string.slf_feedback_photo_count, getCount() - 1));
            SLFFontSet.setSLF_RegularFont(mContext, helper.getView(R.id.slf_photo_count));
            if (TextUtils.isEmpty(object.getMimeType())) {
                helper.setVisible(R.id.slf_iv_video, false);
                helper.setVisible(R.id.slf_iv_delete, false);
                helper.setVisible(R.id.slf_iv_photo, false);
            } else if (object.getMimeType().contains("video")) {
                helper.setVisible(R.id.slf_iv_video, true);
                helper.setVisible(R.id.slf_iv_delete, true);
                helper.setVisible(R.id.slf_iv_photo, true);
            } else {
                helper.setVisible(R.id.slf_iv_video, false);
                helper.setVisible(R.id.slf_iv_delete, true);
                helper.setVisible(R.id.slf_iv_photo, true);
            }
        }

        if (object.getUploadStatus().equals(SLFConstants.UPLOADING)) {
            if (object.getMimeType().contains("video") || object.getMimeType().contains("mp4")) {
                helper.setVisible(R.id.slf_progress_view,true);
                progressView = ((SLFProgressView) helper.getView(R.id.slf_progress_view));
                progressView.setProgress(object.getProgress()/100f);
                helper.setVisible(R.id.slf_progress, true);
                helper.setVisible(R.id.slf_progressbar,false);
                helper.setVisible(R.id.slf_iv_delete, true);
                helper.getView(R.id.slf_iv_delete).setTag(position);
                helper.setVisible(R.id.slf_center_addimg, true);
                helper.setVisible(R.id.slf_photo_count, true);
                helper.setImageResource(R.id.slf_iv_photo, R.drawable.slf_photo_adapter_defult_icon);
            } else {
                helper.setVisible(R.id.slf_progress_view,false);
                helper.setVisible(R.id.slf_progress, true);
                helper.setVisible(R.id.slf_progressbar,true);
                helper.setVisible(R.id.slf_iv_delete, true);
                helper.setVisible(R.id.slf_center_addimg, true);
                helper.setVisible(R.id.slf_photo_count, true);
                helper.setImageResource(R.id.slf_iv_photo, R.drawable.slf_photo_adapter_defult_icon);
            }
        } else if (object.getUploadStatus().equals(SLFConstants.UPLOADFAIL)) {
            helper.getView(R.id.slf_progress).clearAnimation();
            helper.setVisible(R.id.slf_progress, false);
            helper.setVisible(R.id.slf_progress_view,false);
            helper.setVisible(R.id.slf_progressbar,false);
            helper.setVisible(R.id.slf_center_addimg, false);
            helper.setVisible(R.id.slf_photo_count, false);
            helper.setVisible(R.id.slf_iv_video, false);
            helper.setVisible(R.id.slf_iv_photo_fail, true);
        } else {
            helper.getView(R.id.slf_progress).clearAnimation();
            helper.setVisible(R.id.slf_progress, false);
            helper.setVisible(R.id.slf_progress_view,false);
            helper.setVisible(R.id.slf_progressbar,false);
            helper.setVisible(R.id.slf_center_addimg, true);
            helper.setVisible(R.id.slf_photo_count, true);
            helper.setVisible(R.id.slf_iv_photo_fail, false);
            if (object.getThumbnailSmallPath() != null) {
//                SLFImageUtil.loadImage(getContext(),object.getOriginalPath()
//                        ,(ImageView) helper.getView(R.id.slf_iv_photo),R.drawable.slf_photo_adapter_defult_icon,R.drawable.slf_photo_adapter_defult_icon
                if (object.getThumbnailSmallPath().equals(helper.getView(R.id.slf_iv_photo).getTag(R.id.slf_iv_photo))) {

                } else {
                    //四周都是圆角的圆角矩形图片。
//                    Glide.with(mContext).load(object.getThumbnailSmallPath()).apply(
//                                    RequestOptions.bitmapTransform(new RoundedCorners(10))).
//                            error(SLFResourceUtils.getDrawable(R.drawable.slf_photo_adapter_defult_icon))
//                            //注:是否跳过内存缓存，设置为false，如为true的话每次闪烁也正常~
//                            .skipMemoryCache(false)
//                            //取消Glide自带的动画
//                            .dontAnimate()
//                            .into((ImageView) helper.getView(R.id.slf_iv_photo));
                    ViewGroup.LayoutParams lp = helper.getView(R.id.slf_iv_photo).getLayoutParams();
                    lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    helper.getView(R.id.slf_iv_photo).setLayoutParams(lp);
                    SLFImageUtil.loadImage(getContext(), object.getThumbnailSmallPath(), (ImageView) helper.getView(R.id.slf_iv_photo), R.drawable.slf_photo_adapter_defult_icon, R.drawable.slf_photo_adapter_defult_icon
                            , SLFImageShapes.SQUARE, SLFImageShapes.ROUND);
                    helper.getView(R.id.slf_iv_photo).setTag(R.id.slf_iv_photo, object.getThumbnailSmallPath());

                }

            } else {
                SLFImageUtil.loadImage(getContext(), "", (ImageView) helper.getView(R.id.slf_iv_photo), R.drawable.slf_photo_adapter_defult_icon, R.drawable.slf_photo_adapter_defult_icon
                        , SLFImageShapes.SQUARE, SLFImageShapes.ROUND);
            }
        }

        helper.getView(R.id.slf_iv_delete).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Log.d("yj","delete----object---touch--ssss");
                    SLFConstants.isCloseClick = true;
                    Log.d("yj","delete----object---touch--ssss::"+ SLFConstants.isCloseClick );
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Log.d("yj","delete----object---ssss");
//                            if(object!=null) {
//                                if (object.getMimeType().contains("video") || object.getMimeType().contains("mp4")) {
//                                    SLFVideoSlimmer.stopConvertVideo();
//                                }
//                            }
                            if (SLFCommonUpload.getListInstance().size() == 8) {
                                for (int i = 0; i < SLFCommonUpload.getListInstance().size(); i++) {
                                    if (SLFCommonUpload.getListInstance().get(i).equals(object.getUploadPath())) {
                                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(i)).isIdle = true;
                                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(i + 1)).isIdle = true;
                                    }
                                }
                            }

                            getList().remove(object);
                            Message msg = handler.obtainMessage();
                            msg.what = DELETECHANGE;
                            handler.sendMessage(msg);
                        }
                    }).start();
                    return true;
                }
                return false;
            }
        });

//        helper.getView(R.id.slf_iv_delete).setOnClickListener( v -> {
//
//
//        });
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case DELETECHANGE:
                    notifyDataSetChanged();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SLFConstants.isCloseClick = false;
                        }
                    },500);
                    break;
            }
            return true;
        }
    });

    @Override
    public int getContentView() {
        return R.layout.slf_photo_add_item;
    }


}
