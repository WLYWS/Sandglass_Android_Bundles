package com.wyze.sandglasslibrary.functionmoudle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.commonapi.SLFCommonUpload;
import com.wyze.sandglasslibrary.moudle.SLFMediaData;
import com.wyze.sandglasslibrary.utils.SLFAdapterUtils;
import com.wyze.sandglasslibrary.utils.SLFImageShapes;
import com.wyze.sandglasslibrary.utils.SLFImageUtil;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;
import com.wyze.sandglasslibrary.utils.SLFStringFormatUtil;
import com.wyze.sandglasslibrary.utils.SLFViewHolder;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by yangjie
 * describe:选择图片控件adapter实现
 * time: 2022/12/6
 */
public class SLFAndPhotoAdapter extends SLFQuickAdapter<SLFMediaData> {

    private Context mContext;

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
            helper.setText(R.id.slf_photo_count, SLFStringFormatUtil.getFormatString(R.string.slf_feedback_photo_count, getCount() - 1));
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
            helper.setVisible(R.id.slf_progress, true);
            helper.setVisible(R.id.slf_iv_delete, true);
        } else {
            helper.getView(R.id.slf_progress).clearAnimation();
            helper.setVisible(R.id.slf_progress, false);
            if (!TextUtils.isEmpty(object.getThumbnailSmallPath())) {
//                SLFImageUtil.loadImage(getContext(),object.getOriginalPath()
//                        ,(ImageView) helper.getView(R.id.slf_iv_photo),R.drawable.slf_photo_adapter_defult_icon,R.drawable.slf_photo_adapter_defult_icon
                if (object.getThumbnailSmallPath().equals(helper.getView(R.id.slf_iv_photo).getTag(R.id.slf_iv_photo))) {

                } else {
                    //四周都是圆角的圆角矩形图片。
                    Glide.with(mContext).load(object.getThumbnailSmallPath()).apply(
                                    RequestOptions.bitmapTransform(new RoundedCorners(10))).
                            error(SLFResourceUtils.getDrawable(R.drawable.slf_photo_adapter_defult_icon))
                            //注:是否跳过内存缓存，设置为false，如为true的话每次闪烁也正常~
                            .skipMemoryCache(false)
                            //取消Glide自带的动画
                            .dontAnimate()
                            .into((ImageView) helper.getView(R.id.slf_iv_photo));
//                    SLFImageUtil.loadImage(getContext(),object.getThumbnailSmallPath(),,(ImageView) helper.getView(R.id.slf_iv_photo),R.drawable.slf_photo_adapter_defult_icon,R.drawable.slf_photo_adapter_defult_icon);
                    helper.getView(R.id.slf_iv_photo).setTag(R.id.slf_iv_photo, object.getThumbnailSmallPath());
                }
            } else {
                helper.setImageResource(R.id.slf_iv_photo, R.drawable.slf_photo_adapter_defult_icon);
            }

        }

        helper.setOnClickListener(R.id.slf_iv_delete, v -> {

            if (SLFCommonUpload.getListInstance().size() == 9) {
                for (int i = 0; i < SLFCommonUpload.getListInstance().size(); i++) {
                    if (SLFCommonUpload.getListInstance().get(i).equals(object.getUploadPath())) {
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(i)).isIdle = true;
                        SLFCommonUpload.getInstance().get(SLFCommonUpload.getListInstance().get(i + 1)).isIdle = true;
                    }
                }
            }
            getList().remove(object);
            notifyDataSetChanged();
        });
    }


    @Override
    public int getContentView() {
        return R.layout.slf_photo_add_item;
    }


}
