package com.sandglass.sandglasslibrary.functionmoudle.adapter.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sandglass.sandglasslibrary.utils.SLFViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * created by yangjie
 * describe:RecyclerView adpater适配器
 * time: 2022/12/15
 */
public abstract class SLFRecyclerAdatper<T> extends RecyclerView.Adapter<SLFRecyclerHolder> {

    private final Context mContext;
    private List<T> mList;
    private SLFOnItemClickListener mOnItemClickListener;
    private SLFOnLongClickListener mOnLongClickListener;

    public SLFRecyclerAdatper(Context context) {
        this(context, new ArrayList<>());
    }

    public SLFRecyclerAdatper(Context context, List<T> list) {
        mContext = context;
        mList = list;
        SLFExpandableViewHoldersUtil.getInstance().init();
    }

    public Context getContext() {
        return mContext;
    }

    public void setOnItemClickListener(SLFOnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    public void setOnLongClickListener(SLFOnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public SLFRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = getLayoutInflater().inflate(getContentView(viewType), parent,
                false);
        View expandView = contentView.findViewById(getItemExpandView());
        View iconArrow = contentView.findViewById(getItemIconArrow());
        if(expandView == null){
            return new SLFRecyclerHolder(contentView);
        }
        SLFExpandableViewHoldersUtil.getInstance().setNeedExplanedOnlyOne(isExplandOnlyOne());
        return new SLFRecyclerHolder(contentView,expandView,iconArrow,getItemArrowRotationAngle());
    }

    @SuppressWarnings("java:S1185")
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override  @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull final SLFRecyclerHolder holder, final int position) {
        if(getItemExpandView()!=0){
            SLFExpandableViewHoldersUtil.getInstance().getKeepOneHolder().bind(holder,position);
        }
        onInitView(holder, getItem(position), position);
        // 实现item点击事件回调
        if (null != mOnItemClickListener) {
            holder.getView().setOnClickListener(v -> mOnItemClickListener.onItemClick(holder, position));
        }

        if (null != mOnLongClickListener) { // 实现item点击事件回调
            holder.getView().setOnLongClickListener(v -> {
                mOnLongClickListener.onLongClick(holder,position);
                return true;
            });
        }
    }


    @Override
    public int getItemCount() {
        if (null != mList) {
            return mList.size();
        }
        return 0;
    }

    //---------------------------------abstract--------------------------------

    /**
     * item布局ID
     *
     * @param viewType 类别
     * @return item布局ID
     */
    public abstract int getContentView(int viewType);

    /**
     * item展开动画布局ID
     *
     * @return item展开动画布局ID
     */
    @SuppressWarnings("java:S3400")
    public int getItemExpandView(){
        return 0;
    }

    /**
     * item展开动画布局中箭头的ID
     *
     * @return item展开动画布局中指示箭头的ID
     */
    @SuppressWarnings("java:S3400")
    public int getItemIconArrow(){
        return 0;
    }
    /**
     * item展开动画布局中箭头的旋转角度
     *
     * @return item展开动画布局中指示箭头旋转角度
     */
    @SuppressWarnings("java:S3400")
    public int getItemArrowRotationAngle(){
        return 90;
    }

    /**
     * list是否同是只展开一个
     * @return isExplandOnlyOne
     */
    public boolean isExplandOnlyOne(){
        return true;
    }


    /**
     * 设置item展开动画
     * @param holder //viewHolder
     */
    @SuppressWarnings("unchecked")
    public void setItemViewExpand(SLFRecyclerHolder holder){
        SLFExpandableViewHoldersUtil.getInstance().getKeepOneHolder().toggle(holder);
    }

    /**
     * item刷新的回调
     *
     * @param holder   ViewHolder
     * @param object   Item数据对象
     * @param position 刷新的位置
     */
    public abstract void onInitView(SLFRecyclerHolder holder, T object, int position);

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
            SLFRecyclerAdatper.this.onClick(v.getId(), mPosition);
        }

    }

    public void onClick(int viewId, int position) {
    }


    //---------------------------------List--------------------------------
    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> list) {
        if (null == list) {
            this.mList.clear();
        } else {
            this.mList = list;
        }
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
        if (null == list) {
            this.mList.clear();
        } else {
            this.mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    //---------------------------------other--------------------------------
    protected LayoutInflater getLayoutInflater() {
        return (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * @param view converView
     * @param id   控件的id
     * @return 返回<T extends View>
     */
    protected <E extends View> E get(View view, int id) {
        return SLFViewUtil.get(view, id);
    }

}