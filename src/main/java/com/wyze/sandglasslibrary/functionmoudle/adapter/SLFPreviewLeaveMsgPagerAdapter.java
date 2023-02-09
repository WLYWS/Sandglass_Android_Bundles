package com.wyze.sandglasslibrary.functionmoudle.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.interf.SLFIPhotoView;
import com.wyze.sandglasslibrary.moudle.SLFMediaData;
import com.wyze.sandglasslibrary.moudle.net.responsebean.SLFLeveMsgRecordMoudle;
import com.wyze.sandglasslibrary.uiutils.SLFPhotoViewAttacher;
import com.wyze.sandglasslibrary.uiutils.SLFPhotoViewImpl;
import com.wyze.sandglasslibrary.utils.SLFDateFormatUtils;
import com.wyze.sandglasslibrary.utils.SLFImageUtil;

import java.util.ArrayList;

public class SLFPreviewLeaveMsgPagerAdapter extends PagerAdapter {

    private ArrayList<SLFLeveMsgRecordMoudle> picPathLists;
    private Context mContext;
    private MediaPlayer mediaPlayer;
    private int mCurrentPosition = -1;
    private   OnClickListener mListener;

    public SLFPreviewLeaveMsgPagerAdapter(Context context, ArrayList<SLFLeveMsgRecordMoudle> picPathLists) {
        this.picPathLists = picPathLists;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return picPathLists.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        SLFLeveMsgRecordMoudle mediaData = picPathLists.get(position);
        View itemView = null;
        if (mediaData.getContentType().contains("video")) {
            itemView = View.inflate(mContext, R.layout.slf_video_item, null);
            final VideoView videoView = itemView.findViewById(R.id.slf_video_view);
            videoView.setVideoPath(mediaData.getUrl());
            RelativeLayout imgTub = itemView.findViewById(R.id.slf_previewView_rl_parent);
            ImageView videoThub = itemView.findViewById(R.id.slf_iv_photo);
            ImageView playVideo = itemView.findViewById(R.id.slf_iv_video);
            TextView durningText = itemView.findViewById(R.id.slf_tv_video_duration);
            videoView.setVisibility(View.GONE);
            imgTub.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mediaData.getUrl())){
                SLFImageUtil.loadImage(mContext,mediaData.getUrl()
                        ,videoThub,R.drawable.slf_photo_adapter_defult_icon,R.drawable.slf_photo_adapter_defult_icon);
            }else{
                videoThub.setImageBitmap(null);
            }
            //durningText.setText(SLFDateFormatUtils.timeStampToDate(mContext, mediaData.getDuration()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick();
                    }
                    if(videoView.isPlaying()){
                        mCurrentPosition = videoView.getCurrentPosition();
                        imgTub.setVisibility(View.VISIBLE);
                        videoView.setVisibility(View.GONE);
                        videoView.pause();
                    }else{
                        imgTub.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                        if(mCurrentPosition!=-1) {
                            videoView.seekTo(mCurrentPosition);
                        }else{
                            videoView.seekTo(0);
                        }
                        videoView.start();

                    }
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                        }
                    });
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mCurrentPosition = -1;
                            videoView.stopPlayback();
                            imgTub.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.GONE);
                        }
                    });
                    videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            mCurrentPosition = -1;
                            videoView.stopPlayback();
                            imgTub.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.GONE);
                            return true;
                        }
                    });
                }
            });

        } else {
            itemView = new SLFPhotoViewImpl(mContext);
            String photoPath = picPathLists.get(position).getUrl();
            Glide.with(mContext).load(photoPath).into((ImageView) itemView);
            //mAttacher = new SLFPhotoViewAttacher(ivPic);
            ((SLFIPhotoView) itemView).setOnPhotoTapListener(new SLFPhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (mListener != null) {
                        mListener.onClick();
                    }
                }
            });
        }

        container.addView(itemView);


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setOnClickListener(OnClickListener mListener) {
         this.mListener = mListener;
    }

    public interface OnClickListener {
        void onClick();
    }
}
