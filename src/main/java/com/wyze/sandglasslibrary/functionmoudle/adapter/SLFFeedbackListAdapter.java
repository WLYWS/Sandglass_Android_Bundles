package com.wyze.sandglasslibrary.functionmoudle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFRecord;
import com.wyze.sandglasslibrary.utils.SLFDateFormatUtils;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjie on 2023/1/29.
 */

public class SLFFeedbackListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SLFRecord> datas;
    private Context context;
    private int normalType = 0;
    private int footType = 1;
    private boolean hasMore = true;
    private boolean fadeTips = false;
    private OnItemClickLitener   mOnItemClickLitener;
    private Handler mHandler = new Handler(Looper.getMainLooper());


    public SLFFeedbackListAdapter (List <SLFRecord> datas, FragmentActivity context, boolean hasMore) {
        this.datas = datas;
        this.hasMore = hasMore;
        this.context = context;
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
            if(datas.get(position).getStatus()==0) {
                ((NormalHolder) holder).slf_feedback_list_item_title.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_to_be_processed));
                ((NormalHolder) holder).slf_feedback_list_item_title.setTextColor(SLFResourceUtils.getColor(R.color.slf_warning_color));
            }else if(datas.get(position).getStatus()==1||datas.get(position).getStatus()==2){
                ((NormalHolder) holder).slf_feedback_list_item_title.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_in_progress));
                ((NormalHolder) holder).slf_feedback_list_item_title.setTextColor(SLFResourceUtils.getColor(R.color.slf_theme_color));
            }else if(datas.get(position).getStatus()==4){
                ((NormalHolder) holder).slf_feedback_list_item_title.setText(SLFResourceUtils.getString(R.string.slf_feedback_list_item_title_finished));
                ((NormalHolder) holder).slf_feedback_list_item_title.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_add_photos_text_color));
            }
            if(datas.get(position).getRead()==0){
                ((NormalHolder) holder).slf_feedback_list_item_content_point.setVisibility(View.VISIBLE);
            }else{
                ((NormalHolder) holder).slf_feedback_list_item_content_point.setVisibility(View.INVISIBLE);
            }
            ((NormalHolder) holder).slf_feedback_list_item_date.setText(SLFDateFormatUtils.getDateToMyString(datas.get(position).getLastReplyTs(),SLFDateFormatUtils.MDYT));
            ((NormalHolder) holder).slf_feedback_list_item_content.setText(datas.get(position).getContent());
            if(TextUtils.isEmpty(datas.get(position).getServiceTypeText())&&TextUtils.isEmpty(datas.get(position).getCategoryText())&&TextUtils.isEmpty(datas.get(position).getSubCategoryText())){
                ((NormalHolder) holder).slf_feedback_list_item_bottom_title.setText("");
            }else if(!TextUtils.isEmpty(datas.get(position).getServiceTypeText())&&!TextUtils.isEmpty(datas.get(position).getCategoryText())&&TextUtils.isEmpty(datas.get(position).getSubCategoryText())){
                ((NormalHolder) holder).slf_feedback_list_item_bottom_title.setText(datas.get(position).getServiceTypeText()+"/"+datas.get(position).getCategoryText());
            }else if(!TextUtils.isEmpty(datas.get(position).getServiceTypeText())&&TextUtils.isEmpty(datas.get(position).getCategoryText())&&TextUtils.isEmpty(datas.get(position).getSubCategoryText())){
                ((NormalHolder) holder).slf_feedback_list_item_bottom_title.setText(datas.get(position).getServiceTypeText());
            } else {
                ((NormalHolder) holder).slf_feedback_list_item_bottom_title.setText(datas.get(position).getServiceTypeText()+"/"+datas.get(position).getCategoryText()+"/"+datas.get(position).getSubCategoryText());
            }
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


    public void updateList(List<SLFRecord> newDatas, boolean hasMore) {
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
            slf_feedback_list_item_title = (TextView) itemView.findViewById(R.id.slf_feedback_list_item_title);
            slf_feedback_list_item_date = (TextView) itemView.findViewById(R.id.slf_feedback_list_item_date);
            slf_feedback_list_item_content = (TextView) itemView.findViewById(R.id.slf_feedback_list_item_content);
            slf_feedback_list_item_right_back = (ImageView) itemView.findViewById(R.id.slf_feedback_list_item_right_back);
            slf_feedback_list_item_content_point = (ImageView) itemView.findViewById(R.id.slf_feedback_list_item_content_point);
            slf_feedback_list_item_bottom_title = (TextView) itemView.findViewById(R.id.slf_feedback_list_item_bottom_title);
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
