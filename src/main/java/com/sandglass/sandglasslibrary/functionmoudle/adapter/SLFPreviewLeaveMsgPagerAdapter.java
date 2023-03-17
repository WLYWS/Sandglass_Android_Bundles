package com.sandglass.sandglasslibrary.functionmoudle.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.interf.SLFIPhotoView;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFLeveMsgRecordMoudle;
import com.sandglass.sandglasslibrary.uiutils.SLFPhotoViewAttacher;
import com.sandglass.sandglasslibrary.uiutils.SLFPhotoViewImpl;

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
            FrameLayout slf_pre_progress = itemView.findViewById(R.id.slf_pre_progress);
            videoView.setVisibility(View.GONE);
            imgTub.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mediaData.getUrl())){
                Glide.with(mContext).load(mediaData.getThumbnailUrl())
                        .placeholder(R.drawable.slf_photo_adapter_defult_icon)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //slf_pre_progress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                //slf_pre_progress.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(videoThub);
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
                        slf_pre_progress.setVisibility(View.VISIBLE);
                    }
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                                @Override
                                public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                                    if(i==MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                        slf_pre_progress.setVisibility(View.GONE);
                                       return true;
                                    }
                                    return false;
                                }
                            });
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
                            slf_pre_progress.setVisibility(View.GONE);
                            return true;
                        }
                    });
                }
            });

        } else {
            itemView = new SLFPhotoViewImpl(mContext);
            String photoPath = picPathLists.get(position).getUrl();
//            FrameLayout slf_pre_progress = itemView.findViewById(R.id.slf_pre_progress);
//            ProgressBar slf_pre_progressbar = itemView.findViewById(R.id.slf_pre_progressbar);
            //slf_pre_progress.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(photoPath)
                    .placeholder(R.drawable.slf_photo_adapter_defult_icon)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //slf_pre_progress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //slf_pre_progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into((ImageView) itemView);
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
