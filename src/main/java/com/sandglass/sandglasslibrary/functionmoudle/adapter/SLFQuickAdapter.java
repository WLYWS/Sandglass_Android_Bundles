package com.sandglass.sandglasslibrary.functionmoudle.adapter;

import android.content.Context;
import android.view.View;

import com.sandglass.sandglasslibrary.utils.SLFAdapterUtils;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.base.SLFBaseAdapter;

import java.util.List;
/**
 * created by yangjie
 * describe:快速apdaer的抽象类
 * time: 2022/12/6
 */
public abstract class SLFQuickAdapter<T> extends SLFBaseAdapter<T> {

    public SLFQuickAdapter(Context context) {
        super(context);
    }

    public SLFQuickAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    public void onInitView(View view, T object, int position) {
        SLFAdapterUtils helper = SLFAdapterUtils.get(view, position);
        convert(helper, object, position);
    }

    public abstract void convert(SLFAdapterUtils helper, T object, int position);
}
