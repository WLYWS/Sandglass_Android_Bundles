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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.bean.SLFUserCenter;
import com.sandglass.sandglasslibrary.functionmoudle.activity.feedback.SLFFeedbackPicPreviewActivity;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.recycler.SLFRecyclerHolder;
import com.sandglass.sandglasslibrary.moudle.SLFMediaData;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFLeaveMsgRecord;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFLeveMsgRecordMoudle;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.utils.SLFDateFormatUtils;
import com.sandglass.sandglasslibrary.utils.SLFImageShapes;
import com.sandglass.sandglasslibrary.utils.SLFImageUtil;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangjie on 2023/2/2.
 */

public class SLFFeedbackDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<SLFLeaveMsgRecord> datas;
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


    public SLFFeedbackDetailAdapter(Context context, List<SLFLeaveMsgRecord> list) {
        this.context = context;
        this.datas = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == usernormalType) {
            return new UserNormalHolder(LayoutInflater.from(context).inflate(R.layout.slf_feedback_list_detail_item_user, parent, false));
        } else{
            return new WorkerNormalHolder(LayoutInflater.from(context).inflate(R.layout.slf_feedback_list_detail_item_work, parent, false));
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
                SLFMediaData slfMediaData = new SLFMediaData();
                for (int i = 0; i < datas.get(position).getAttrList().size(); i++) {
                    if (!TextUtils.isEmpty(datas.get(position).getAttrList().get(i).getThumbnailUrl())) {
                        if (i == 0) {
                            if (datas.get(position).getAttrList().get(i).getThumbnailUrl().equals(((UserNormalHolder) holder).slf_first_iv_photo.getTag(R.id.slf_iv_photo))) {

                            } else {
                                SLFImageUtil.loadImageShow(context, datas.get(position).getAttrList().get(0).getThumbnailUrl(), (ImageView) ((UserNormalHolder) holder).slf_first_iv_photo, ((UserNormalHolder) holder).slf_frist_media_data, R.drawable.slf_photo_adapter_defult_icon, R.drawable.slf_photo_adapter_defult_icon
                                        , SLFImageShapes.SQUARE, SLFImageShapes.ROUND);
                                ((UserNormalHolder) holder).slf_first_iv_photo.setTag(R.id.slf_iv_photo, datas.get(position).getAttrList().get(0).getThumbnailUrl());
                                ((UserNormalHolder) holder).slf_first_iv_photo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SLFLogUtil.sdkd("yj", "frist image");
                                        gotoPicPriewActivity(position, 0);
                                    }
                                });
                            }
                        } else if (i == 1) {
                            if (datas.get(position).getAttrList().get(i).getThumbnailUrl().equals(((UserNormalHolder) holder).slf_second_iv_photo.getTag(R.id.slf_iv_photo))) {

                            } else {
                                SLFImageUtil.loadImageShow(context, datas.get(position).getAttrList().get(1).getThumbnailUrl(), (ImageView) ((UserNormalHolder) holder).slf_second_iv_photo, ((UserNormalHolder) holder).slf_frist_media_data, R.drawable.slf_photo_adapter_defult_icon, R.drawable.slf_photo_adapter_defult_icon
                                        , SLFImageShapes.SQUARE, SLFImageShapes.ROUND);
                                ((UserNormalHolder) holder).slf_second_iv_photo.setTag(R.id.slf_iv_photo, datas.get(position).getAttrList().get(1).getThumbnailUrl());
                                ((UserNormalHolder) holder).slf_second_iv_photo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SLFLogUtil.sdkd("yj", "second image");
                                        gotoPicPriewActivity(position, 1);
                                    }
                                });
                            }
                        } else {
                            if (datas.get(position).getAttrList().get(i).getThumbnailUrl().equals(((UserNormalHolder) holder).slf_third_iv_photo.getTag(R.id.slf_iv_photo))) {

                            } else {
                                SLFImageUtil.loadImageShow(context, datas.get(position).getAttrList().get(i).getThumbnailUrl(), (ImageView) ((UserNormalHolder) holder).slf_third_iv_photo, ((UserNormalHolder) holder).slf_frist_media_data, R.drawable.slf_photo_adapter_defult_icon, R.drawable.slf_photo_adapter_defult_icon
                                        , SLFImageShapes.SQUARE, SLFImageShapes.ROUND);
                                ((UserNormalHolder) holder).slf_third_iv_photo.setTag(R.id.slf_iv_photo, datas.get(position).getAttrList().get(1).getThumbnailUrl());
                                ((UserNormalHolder) holder).slf_third_iv_photo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SLFLogUtil.sdkd("yj", "third image");
                                        gotoPicPriewActivity(position, 2);
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
            if(SLFUserCenter.userInfoBean!=null&&SLFUserCenter.userInfoBean.getData()!=null&&SLFUserCenter.userInfoBean.getData().getIconUrl()!=null){
                SLFImageUtil.loadImage(context, SLFUserCenter.userInfoBean.getData().getIconUrl(),((UserNormalHolder) holder).slf_feedback_list_detail_user_header_img , R.mipmap.slf_chat_bot_user_icon, R.mipmap.slf_chat_bot_user_icon
                        , SLFImageShapes.CIRCLE, SLFImageShapes.ROUND);
            }else {
                ((UserNormalHolder) holder).slf_feedback_list_detail_user_header_img.setImageResource(R.mipmap.slf_chat_bot_user_icon);
            }

            ((UserNormalHolder) holder).slf_feedback_detail_question_content.setText(datas.get(position).getContent());
            ((UserNormalHolder) holder).slf_feedback_detail_user_time.setText(SLFDateFormatUtils.getDateToMyString(context, datas.get(position).getReplyTs()));
            SLFFontSet.setSLF_RegularFont(context, ((UserNormalHolder) holder).slf_feedback_detail_question_content);
            SLFFontSet.setSLF_RegularFont(context, ((UserNormalHolder) holder).slf_feedback_detail_user_time);
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
            ((WorkerNormalHolder) holder).slf_feedback_detail_worker_time.setText(SLFDateFormatUtils.getDateToMyString(context, datas.get(position).getReplyTs()));
            SLFFontSet.setSLF_RegularFont(context, ((WorkerNormalHolder) holder).slf_feedback_detail_worker_question_content);
            SLFFontSet.setSLF_RegularFont(context, ((WorkerNormalHolder) holder).slf_feedback_detail_worker_time);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
            if (datas.get(position).isUser()) {
                return usernormalType;
            } else {
                return workernormalType;
            }
    }

    public int getType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void updateList(List<SLFLeaveMsgRecord> newDatas, boolean hasMore,boolean isRefresh) {
        if (newDatas != null) {
           datas.addAll(0,newDatas);
        }
        this.hasMore = hasMore;
        this.isRefresh = isRefresh;
        notifyDataSetChanged();
        //notifyItemRangeChanged(0,10);
    }

    public boolean isFadeTips() {
        return fadeTips;
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

    class UserNormalHolder extends SLFRecyclerHolder {
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
        private RelativeLayout itemview;


        private View itemView;

        public UserNormalHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            /**user*/
            itemview = itemView.findViewById(R.id.slf_user_feedback);
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

    class WorkerNormalHolder extends SLFRecyclerHolder {
        /**
         * worker
         */
        private ImageView slf_feedback_list_detail_worker_header_img;
        private TextView slf_feedback_detail_worker_question_content;
        private TextView slf_feedback_detail_worker_time;
        private RelativeLayout itemview;


        private View itemView;

        public WorkerNormalHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            /**worker*/
            itemview = itemView.findViewById(R.id.slf_worker_feedback);
            slf_feedback_list_detail_worker_header_img = itemView.findViewById(R.id.slf_feedback_list_detail_worker_header_img);
            slf_feedback_detail_worker_question_content = itemView.findViewById(R.id.slf_feedback_detail_worker_question_content);
            slf_feedback_detail_worker_time = itemView.findViewById(R.id.slf_feedback_detail_worker_time);
        }
    }

    //设置回调接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
