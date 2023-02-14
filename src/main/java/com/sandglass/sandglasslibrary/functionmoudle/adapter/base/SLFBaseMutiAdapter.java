package com.sandglass.sandglasslibrary.functionmoudle.adapter.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.sandglass.sandglasslibrary.utils.SLFImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * created by yangjie
 * describe:listview用于多个布局使用的Adapter
 * time: 2022/12/6
 *
 */
public abstract class SLFBaseMutiAdapter<T> extends android.widget.BaseAdapter{

    private List<T> mList;

    private Context mContext;

    public SLFBaseMutiAdapter(Context context) {
        init(context, new ArrayList<>());
    }

    public SLFBaseMutiAdapter(Context context, List<T> list) {
        init(context, list);
    }

    private void init(Context context, List<T> list) {
        this.mList = list;
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void clear() {
        this.mList.clear();
        notifyDataSetChanged();
    }

    public void remove(int location) {
        if (location >= 0 && mList.size() > location) {
            this.mList.remove(location);
            notifyDataSetChanged();
        }
    }

    public void add(int location, T object) {
        if (object != null) {
            this.mList.add(location, object);
            notifyDataSetChanged();
        }
    }

    public void addLast(T object) {
        if (object != null) {
            this.mList.add(object);
            notifyDataSetChanged();
        }
    }

    public void addHead(T object) {
        if (object != null) {
            this.mList.add(0, object);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<T> list) {
        if (list != null) {
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected LayoutInflater getLayoutInflater() {
        return (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * @param view converView
     * @param id   控件的id
     * @return 返回<T extends View>
     */
    protected <E extends View> E get(View view, int id) {
        return view.findViewById(id);
    }

    @SuppressWarnings("java:S1874")
    protected int getColor(int id) {
        return getContext().getResources().getColor(id);
    }

    protected String getString(int id) {
        return getContext().getResources().getString(id);
    }

    protected float getDimension(int id) {
        return getContext().getResources().getDimension(id);
    }

    @SuppressLint("UseCompatLoadingForDrawables") @SuppressWarnings("java:S1874")
    protected Drawable getDrawable(int id) {
        return getContext().getResources().getDrawable(id);
    }

    protected void loadImage(ImageView img, String url) {
        SLFImageUtil.loadImage(getContext(),url,img);
    }
}
