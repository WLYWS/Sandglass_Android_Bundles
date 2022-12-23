package com.wyze.sandglasslibrary.functionmoudle.adapter.recycler;

/**
 * created by yangjie
 * describe:RecyclerView 点击事件
 * time: 2022/12/15
 */
public interface SLFOnItemClickListener {
    /**
     * @param holder 操作的ViewHolder
     * @param position 点击item的位置
     */
    void onItemClick(SLFRecyclerHolder holder, int position);
}
