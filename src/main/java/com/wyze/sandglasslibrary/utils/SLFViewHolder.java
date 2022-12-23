package com.wyze.sandglasslibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyze.sandglasslibrary.R;

public class SLFViewHolder{
    private final SparseArray<View> mViews;
    private Context context;
    private int mPosition;
    private View mConvertView;

    private SLFViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {
        this.context = context;
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个SLFViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static SLFViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new SLFViewHolder(context, parent, layoutId, position);
        }
        return (SLFViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 设置控件显隐藏
     *
     * @param viewId
     * @return
     */
    public SLFViewHolder setVisibility(int viewId, boolean visibility) {
        View view = getView(viewId);
        if(visibility){
            view.setVisibility(View.VISIBLE);
        }else {
            view.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public SLFViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public SLFViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public SLFViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public SLFViewHolder setImageByUrl(int viewId, String url) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.slf_photo_adapter_defult_icon)
        //		.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .transform(new CenterCrop(),new SLFRoundedCornersTransformation(10,0))
                .into((ImageView) getView(viewId));
        return this;
    }

    public int getPosition() {
        return mPosition;
    }

}
