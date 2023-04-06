package com.sandglass.sandglasslibrary.functionmoudle.adapter;

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
import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.interf.SLFIPhotoView;
import com.sandglass.sandglasslibrary.moudle.SLFMediaData;
import com.sandglass.sandglasslibrary.uiutils.SLFPhotoViewAttacher;
import com.sandglass.sandglasslibrary.uiutils.SLFPhotoViewImpl;
import com.sandglass.sandglasslibrary.utils.SLFDateFormatUtils;
import com.sandglass.sandglasslibrary.utils.SLFImageUtil;

import java.util.ArrayList;

public class SLFPreviewPagerAdapter extends PagerAdapter {

    private ArrayList<SLFMediaData> picPathLists;
    private Context mContext;
    private MediaPlayer mediaPlayer;
    private int mCurrentPosition = -1;
    private   OnClickListener mListener;

    public SLFPreviewPagerAdapter(Context context, ArrayList<SLFMediaData> picPathLists) {
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

        SLFMediaData mediaData = picPathLists.get(position);
        View itemView = null;
        if (mediaData.getMimeType().contains("video")) {
            itemView = View.inflate(mContext, R.layout.slf_video_item, null);
            final VideoView videoView = itemView.findViewById(R.id.slf_video_view);
            videoView.setVideoPath(mediaData.getOriginalPath());
            RelativeLayout imgTub = itemView.findViewById(R.id.slf_previewView_rl_parent);
            ImageView videoThub = itemView.findViewById(R.id.slf_iv_photo);
            ImageView playVideo = itemView.findViewById(R.id.slf_iv_video);
            TextView durningText = itemView.findViewById(R.id.slf_tv_video_duration);
            videoView.setVisibility(View.GONE);
            imgTub.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mediaData.getOriginalPath())){
                SLFImageUtil.loadImage(mContext,mediaData.getOriginalPath()
                        ,videoThub,R.drawable.slf_photo_adapter_defult_icon,R.drawable.slf_photo_adapter_defult_icon);
            }else{
                videoThub.setImageBitmap(null);
            }
            durningText.setVisibility(View.GONE);
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
            String photoPath = picPathLists.get(position).getOriginalPath();
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
