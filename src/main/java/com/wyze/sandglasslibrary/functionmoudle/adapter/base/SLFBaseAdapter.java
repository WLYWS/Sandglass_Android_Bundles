package com.wyze.sandglasslibrary.functionmoudle.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wyze.sandglasslibrary.interf.SLFAdapterClickListener;

import java.util.List;
/**
 * created by yangjie
 * describe:baseApater单个布局
 * time: 2022/12/6
 */
public abstract class SLFBaseAdapter <T> extends SLFBaseMutiAdapter<T> implements SLFAdapterClickListener {

    public SLFBaseAdapter(Context context) {
        super(context);
    }

    public SLFBaseAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = getLayoutInflater().inflate(getContentView(), parent, false);
        }
        onInitView(convertView, getItem(position), position);
        return convertView;
    }

    public abstract int getContentView();

    public abstract void onInitView(View view, T object, int position);


    //---------------------------------AdapterClick--------------------------------

    /**
     * 设置item里面view的点击事件
     *
     * @param view     监听的view
     * @param position 下标
     */
    public void setClick(View view, int position) {
        view.setOnClickListener(new ClickListener(position));
    }

    private class ClickListener implements View.OnClickListener {
        private final int mPosition;

        public ClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            SLFBaseAdapter.this.onClick(v.getId(), mPosition);
        }

    }

    @Override
    public void onClick(int viewId, int position) {
    }

}
