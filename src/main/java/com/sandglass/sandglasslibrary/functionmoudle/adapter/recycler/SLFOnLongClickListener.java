package com.sandglass.sandglasslibrary.functionmoudle.adapter.recycler;

/**
 * created by yangjie
 * describe:RecyclerView 长按点击事件
 * time: 2022/12/15
 */
public interface SLFOnLongClickListener {
    /**
     * @param holder 操作的ViewHolder
     * @param position 点击item的位置
     */
    void onLongClick(SLFRecyclerHolder holder, int position);
}