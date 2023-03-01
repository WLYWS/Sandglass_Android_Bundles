package com.sandglass.sandglasslibrary.functionmoudle.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackPicPreviewActivity;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackSubmitActivity;
import com.sandglass.sandglasslibrary.moudle.SLFMediaData;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFLeaveMsgRecord;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFLeveMsgRecordMoudle;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.utils.SLFDateFormatUtils;
import com.sandglass.sandglasslibrary.utils.SLFImageShapes;
import com.sandglass.sandglasslibrary.utils.SLFImageUtil;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjie on 2023/2/2.
 */

public class SLFFeedbackDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SLFLeaveMsgRecord> datas;
    private Context context;
    private int usernormalType = 0;
    private int workernormalType = 1;
    private int footType = 2;
    private boolean hasMore;
    private boolean isRefresh;
    private boolean fadeTips = false;
    private OnItemClickLitener mOnItemClickLitener;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    /**
     * 图片路径集合
     */
    private ArrayList<SLFLeveMsgRecordMoudle> picPathLists = new ArrayList<>();

    public SLFFeedbackDetailAdapter(List<SLFLeaveMsgRecord> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == usernormalType) {
            return new UserNormalHolder(LayoutInflater.from(context).inflate(R.layout.slf_feedback_list_detail_item_user, parent, false));
        } else if (viewType == workernormalType) {
            return new WorkerNormalHolder(LayoutInflater.from(context).inflate(R.layout.slf_feedback_list_detail_item_work, parent, false));
        } else {
            return new FootHolder(LayoutInflater.from(context).inflate(R.layout.slf_feedback_list_footview, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof UserNormalHolder) {
            if (datas.get(position).getAttrList() != null && datas.get(position).getAttrList().size() > 0) {
                ((UserNormalHolder) holder).slf_feedback_detail_show_photo_linear.setVisibility(View.VISIBLE);
                if (datas.get(position).getAttrList().size() == 1) {
                    ((UserNormalHolder) holder).slf_frist_media_data.setVisibility(View.VISIBLE);
                    ((UserNormalHolder) holder).slf_second_media_data.setVisibility(View.GONE);
                    ((UserNormalHolder) holder).slf_third_media_data.setVisibility(View.GONE);
                    if (datas.get(position).getAttrList().get(0).getContentType().contains("video")) {
                        ((UserNormalHolder) holder).slf_first_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_first_iv_video.setVisibility(View.VISIBLE);
                    } else {
                        ((UserNormalHolder) holder).slf_first_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_first_iv_video.setVisibility(View.GONE);
                    }
                } else if (datas.get(position).getAttrList().size() == 2) {
                    ((UserNormalHolder) holder).slf_frist_media_data.setVisibility(View.VISIBLE);
                    ((UserNormalHolder) holder).slf_second_media_data.setVisibility(View.VISIBLE);
                    ((UserNormalHolder) holder).slf_third_media_data.setVisibility(View.GONE);
                    if (datas.get(position).getAttrList().get(0).getContentType().contains("video")) {
                        ((UserNormalHolder) holder).slf_first_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_first_iv_video.setVisibility(View.VISIBLE);
                    } else {
                        ((UserNormalHolder) holder).slf_first_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_first_iv_video.setVisibility(View.GONE);
                    }
                    if (datas.get(position).getAttrList().get(1).getContentType().contains("video")) {
                        ((UserNormalHolder) holder).slf_second_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_second_iv_video.setVisibility(View.VISIBLE);
                    } else {
                        ((UserNormalHolder) holder).slf_second_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_second_iv_video.setVisibility(View.GONE);
                    }
                } else {
                    ((UserNormalHolder) holder).slf_frist_media_data.setVisibility(View.VISIBLE);
                    ((UserNormalHolder) holder).slf_second_media_data.setVisibility(View.VISIBLE);
                    ((UserNormalHolder) holder).slf_third_media_data.setVisibility(View.VISIBLE);
                    if (datas.get(position).getAttrList().get(0).getContentType().contains("video")) {
                        ((UserNormalHolder) holder).slf_first_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_first_iv_video.setVisibility(View.VISIBLE);
                    } else {
                        ((UserNormalHolder) holder).slf_first_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_first_iv_video.setVisibility(View.GONE);
                    }
                    if (datas.get(position).getAttrList().get(1).getContentType().contains("video")) {
                        ((UserNormalHolder) holder).slf_second_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_second_iv_video.setVisibility(View.VISIBLE);
                    } else {
                        ((UserNormalHolder) holder).slf_second_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_second_iv_video.setVisibility(View.GONE);
                    }
                    if (datas.get(position).getAttrList().get(2).getContentType().contains("video")) {
                        ((UserNormalHolder) holder).slf_third_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_third_iv_video.setVisibility(View.VISIBLE);
                    } else {
                        ((UserNormalHolder) holder).slf_third_iv_photo.setVisibility(View.VISIBLE);
                        ((UserNormalHolder) holder).slf_third_iv_video.setVisibility(View.GONE);
                    }
                }
                SLFMediaData  slfMediaData = new SLFMediaData();
                for (int i = 0; i < datas.get(position).getAttrList().size(); i++) {
                    if (!TextUtils.isEmpty(datas.get(position).getAttrList().get(i).getThumbnailUrl())) {
                        if (i == 0) {
                            if (datas.get(position).getAttrList().get(i).getThumbnailUrl().equals(((UserNormalHolder) holder).slf_first_iv_photo.getTag(R.id.slf_iv_photo))) {

                            } else {
                                SLFImageUtil.loadImageShow(context,datas.get(position).getAttrList().get(0).getThumbnailUrl(),(ImageView) ((UserNormalHolder) holder).slf_first_iv_photo,((UserNormalHolder) holder).slf_frist_media_data,R.drawable.slf_photo_adapter_defult_icon,R.drawable.slf_photo_adapter_defult_icon
                                        , SLFImageShapes.SQUARE,SLFImageShapes.ROUND);
                                ((UserNormalHolder) holder).slf_first_iv_photo.setTag(R.id.slf_iv_photo, datas.get(position).getAttrList().get(0).getThumbnailUrl());
                                ((UserNormalHolder) holder).slf_first_iv_photo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SLFLogUtil.d("yj", "frist image");
                                        gotoPicPriewActivity(position,0);
                                    }
                                });
                            }
                        } else if (i == 1) {
                            if (datas.get(position).getAttrList().get(i).getThumbnailUrl().equals(((UserNormalHolder) holder).slf_second_iv_photo.getTag(R.id.slf_iv_photo))) {

                            } else {
                                SLFImageUtil.loadImageShow(context,datas.get(position).getAttrList().get(1).getThumbnailUrl(),(ImageView) ((UserNormalHolder) holder).slf_second_iv_photo,((UserNormalHolder) holder).slf_frist_media_data,R.drawable.slf_photo_adapter_defult_icon,R.drawable.slf_photo_adapter_defult_icon
                                        , SLFImageShapes.SQUARE,SLFImageShapes.ROUND);
                                ((UserNormalHolder) holder).slf_second_iv_photo.setTag(R.id.slf_iv_photo, datas.get(position).getAttrList().get(1).getThumbnailUrl());
                                ((UserNormalHolder) holder).slf_second_iv_photo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SLFLogUtil.d("yj", "second image");
                                        gotoPicPriewActivity(position,1);
                                    }
                                });
                            }
                        } else {
                            if (datas.get(position).getAttrList().get(i).getThumbnailUrl().equals(((UserNormalHolder) holder).slf_third_iv_photo.getTag(R.id.slf_iv_photo))) {

                            } else {
                                SLFImageUtil.loadImageShow(context,datas.get(position).getAttrList().get(i).getThumbnailUrl(),(ImageView) ((UserNormalHolder) holder).slf_third_iv_photo,((UserNormalHolder) holder).slf_frist_media_data,R.drawable.slf_photo_adapter_defult_icon,R.drawable.slf_photo_adapter_defult_icon
                                        , SLFImageShapes.SQUARE,SLFImageShapes.ROUND);
                                ((UserNormalHolder) holder).slf_third_iv_photo.setTag(R.id.slf_iv_photo, datas.get(position).getAttrList().get(1).getThumbnailUrl());
                                ((UserNormalHolder) holder).slf_third_iv_photo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SLFLogUtil.d("yj", "third image");
                                        gotoPicPriewActivity(position,2);
                                    }
                                });
                            }
                        }
                    } else {
                        if (i == 0) {
                            ((UserNormalHolder) holder).slf_first_iv_photo.setImageResource(R.drawable.slf_photo_adapter_defult_icon);
                        } else if (i == 1) {
                            ((UserNormalHolder) holder).slf_second_iv_photo.setImageResource(R.drawable.slf_photo_adapter_defult_icon);
                        } else {
                            ((UserNormalHolder) holder).slf_third_iv_photo.setImageResource(R.drawable.slf_photo_adapter_defult_icon);
                        }
                    }
                }
            } else {
                ((UserNormalHolder) holder).slf_feedback_detail_show_photo_linear.setVisibility(View.GONE);
            }
            ((UserNormalHolder) holder).slf_feedback_detail_question_content.setText(datas.get(position).getContent());
            ((UserNormalHolder) holder).slf_feedback_detail_user_time.setText(SLFDateFormatUtils.getDateToMyString(context,datas.get(position).getReplyTs()));
            SLFFontSet.setSLF_RegularFont(context,((UserNormalHolder) holder).slf_feedback_detail_question_content);
            SLFFontSet.setSLF_RegularFont(context,((UserNormalHolder) holder).slf_feedback_detail_user_time);
            //通过为条目设置点击事件触发回调
//            if (mOnItemClickLitener != null) {
//                ((NormalHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mOnItemClickLitener.onItemClick(view, position);
//                    }
//                });
//            }
        } else if (holder instanceof WorkerNormalHolder) {
            ((WorkerNormalHolder) holder).slf_feedback_detail_worker_question_content.setText(datas.get(position).getContent());
            ((WorkerNormalHolder) holder).slf_feedback_detail_worker_time.setText(SLFDateFormatUtils.getDateToMyString(context,datas.get(position).getReplyTs()));
            SLFFontSet.setSLF_RegularFont(context,((WorkerNormalHolder) holder).slf_feedback_detail_worker_question_content);
            SLFFontSet.setSLF_RegularFont(context,((WorkerNormalHolder) holder).slf_feedback_detail_worker_time);
        } else {
            if(isRefresh){
                fadeTips = true;
                ((FootHolder) holder).slf_more_loading_linear.setVisibility(View.GONE);
            }else {
                if (hasMore == true) {
                    fadeTips = false;
                    if (datas.size() > 0) {
                        //((FootHolder) holder).tips.setText("正在加载更多...");
                        ((FootHolder) holder).slf_more_loading_linear.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //((FootHolder) holder).tips.setVisibility(View.GONE);
                                ((FootHolder) holder).progressBar.clearAnimation();
                                ((FootHolder) holder).slf_more_loading_linear.setVisibility(View.GONE);
                                fadeTips = true;
                                hasMore = true;
                            }
                        }, 500);
                    }
                } else {
                    if (datas.size() > 0) {
                        //((FootHolder) holder).tips.setText("没有更多数据了");
                        ((FootHolder) holder).slf_more_loading_linear.setVisibility(View.VISIBLE);
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //((FootHolder) holder).tips.setVisibility(View.GONE);
                                ((FootHolder) holder).progressBar.clearAnimation();
                                ((FootHolder) holder).slf_more_loading_linear.setVisibility(View.GONE);
                                fadeTips = true;
                                hasMore = true;
                            }
                        }, 500);
                    } else {
                        ((FootHolder) holder).progressBar.clearAnimation();
                        ((FootHolder) holder).slf_more_loading_linear.setVisibility(View.GONE);
                    }
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


    public void updateList(List<SLFLeaveMsgRecord> newDatas, boolean hasMore,boolean isRefresh) {
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        this.hasMore = hasMore;
        this.isRefresh = isRefresh;
        notifyDataSetChanged();
    }

    class UserNormalHolder extends RecyclerView.ViewHolder {
        /**
         * user
         */
        private ImageView slf_feedback_list_detail_user_header_img;
        private TextView slf_feedback_detail_question_content;
        private TextView slf_feedback_detail_user_time;
        private RelativeLayout slf_frist_media_data;
        private RelativeLayout slf_second_media_data;
        private RelativeLayout slf_third_media_data;
        private ImageView slf_first_iv_photo;
        private ImageView slf_first_iv_video;
        private ImageView slf_second_iv_photo;
        private ImageView slf_second_iv_video;
        private ImageView slf_third_iv_photo;
        private ImageView slf_third_iv_video;
        private LinearLayout slf_feedback_detail_show_photo_linear;


        private View itemView;

        public UserNormalHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            /**user*/
            slf_feedback_list_detail_user_header_img = itemView.findViewById(R.id.slf_feedback_list_detail_user_header_img);
            slf_feedback_detail_question_content = itemView.findViewById(R.id.slf_feedback_detail_question_content);
            slf_feedback_detail_user_time = itemView.findViewById(R.id.slf_feedback_detail_user_time);
            slf_frist_media_data = itemView.findViewById(R.id.slf_frist_media_data);
            slf_second_media_data = itemView.findViewById(R.id.slf_second_media_data);
            slf_third_media_data = itemView.findViewById(R.id.slf_third_media_data);
            slf_first_iv_photo = itemView.findViewById(R.id.slf_first_iv_photo);
            slf_first_iv_video = itemView.findViewById(R.id.slf_first_iv_video);
            slf_second_iv_photo = itemView.findViewById(R.id.slf_second_iv_photo);
            slf_second_iv_video = itemView.findViewById(R.id.slf_second_iv_video);
            slf_third_iv_photo = itemView.findViewById(R.id.slf_third_iv_photo);
            slf_third_iv_video = itemView.findViewById(R.id.slf_third_iv_video);
            slf_feedback_detail_show_photo_linear = itemView.findViewById(R.id.slf_feedback_detail_show_photo_linear);
        }
    }

    class WorkerNormalHolder extends RecyclerView.ViewHolder {
        /**
         * worker
         */
        private ImageView slf_feedback_list_detail_worker_header_img;
        private TextView slf_feedback_detail_worker_question_content;
        private TextView slf_feedback_detail_worker_time;


        private View itemView;

        public WorkerNormalHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            /**worker*/
            slf_feedback_list_detail_worker_header_img = itemView.findViewById(R.id.slf_feedback_list_detail_worker_header_img);
            slf_feedback_detail_worker_question_content = itemView.findViewById(R.id.slf_feedback_detail_worker_question_content);
            slf_feedback_detail_worker_time = itemView.findViewById(R.id.slf_feedback_detail_worker_time);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        private LinearLayout slf_more_loading_linear;

        public FootHolder(View itemView) {
            super(itemView);
            slf_more_loading_linear = itemView.findViewById(R.id.slf_more_loading_linear);
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
            if (datas.get(position).isUser()) {
                return usernormalType;
            } else {
                return workernormalType;
            }
        }
    }

    //设置回调接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private void gotoPicPriewActivity(int position,int index) {
        picPathLists.clear();
        picPathLists.addAll(datas.get(position).getAttrList());
        Intent in = new Intent();
        in.putExtra("from", "feedback");
        in.setClass(context, SLFFeedbackPicPreviewActivity.class);
        in.putExtra("position", index);
        in.putExtra("leaveMsg",picPathLists);
        context.startActivity(in);
    }
}
