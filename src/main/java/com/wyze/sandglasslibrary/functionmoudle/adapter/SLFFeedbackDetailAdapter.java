package com.wyze.sandglasslibrary.functionmoudle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFLeaveMsgRecord;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFRecode;
import com.wyze.sandglasslibrary.utils.SLFDateFormatUtils;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjie on 2023/2/2.
 */

public class SLFFeedbackDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SLFLeaveMsgRecord> datas;
    private Context context;
    private int normalType = 0;
    private int footType = 1;
    private boolean hasMore = true;
    private boolean fadeTips = false;
    private OnItemClickLitener   mOnItemClickLitener;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public SLFFeedbackDetailAdapter(List<SLFLeaveMsgRecord> datas, Context context, boolean hasMore) {
        this.datas = datas;
        this.context = context;
        this.hasMore = hasMore;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == normalType) {
            return new NormalHolder(LayoutInflater.from(context).inflate(R.layout.slf_feedback_list_all_item, parent,false));
        } else {
            return new FootHolder(LayoutInflater.from(context).inflate(R.layout.slf_feedback_list_footview, parent,false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof NormalHolder) {

            //通过为条目设置点击事件触发回调
            if (mOnItemClickLitener != null) {
                ((NormalHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickLitener.onItemClick(view, position);
                    }
                });
            }
        } else {
            ((FootHolder) holder).progressBar.setVisibility(View.VISIBLE);
            if (hasMore == true) {
                fadeTips = false;
                if (datas.size() > 0) {
                    //((FootHolder) holder).tips.setText("正在加载更多...");
                    ((FootHolder) holder).progressBar.setVisibility(View.VISIBLE);
                }
            } else {
                if (datas.size() > 0) {
                    //((FootHolder) holder).tips.setText("没有更多数据了");
                    ((FootHolder) holder).progressBar.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //((FootHolder) holder).tips.setVisibility(View.GONE);
                            ((FootHolder) holder).progressBar.setVisibility(View.GONE);
                            fadeTips = true;
                            hasMore = true;
                        }
                    }, 500);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    public int getRealLastPosition() {
        return datas.size();
    }


    public void updateList(List<SLFLeaveMsgRecord> newDatas, boolean hasMore) {
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    class NormalHolder extends RecyclerView.ViewHolder {
        private TextView slf_feedback_list_item_title;
        private TextView slf_feedback_list_item_date;
        private TextView slf_feedback_list_item_content;
        private ImageView slf_feedback_list_item_right_back;
        private ImageView slf_feedback_list_item_content_point;
        private TextView slf_feedback_list_item_bottom_title;
        private View itemView;

        public NormalHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public FootHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.slf_more_loading);
        }
    }

    public boolean isFadeTips() {
        return fadeTips;
    }

    public void resetDatas() {
        datas = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return footType;
        } else {
            return normalType;
        }
    }

    //设置回调接口
    public interface OnItemClickLitener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener){
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
