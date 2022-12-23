package com.wyze.sandglasslibrary.functionmoudle.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.moudle.SLFMediaData;
import com.wyze.sandglasslibrary.utils.SLFDateFormatUtils;
import com.wyze.sandglasslibrary.utils.SLFImageUtil;
import com.wyze.sandglasslibrary.utils.SLFViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SLFPhotoListAdapter extends BaseAdapter {

    private Context context;
    private List<SLFMediaData> mDatas;

    private int mScreenWidth;
    private int mRowWidth;

    private int currentPosition = -1;
    private int selectedNum;
    private boolean isChecked;
    private ArrayList<SLFMediaData> picPathLists;
    private ArrayList<String> picPositionLists;
    private SLFViewHolder helper;

    public SLFPhotoListAdapter(Context context, List<SLFMediaData> mDatas,  int selectedNum) {
        this.context = context;
        this.mDatas = mDatas;
        this.mScreenWidth = getScreenWidth(context);
        this.mRowWidth = mScreenWidth / 4;
        this.selectedNum = selectedNum;
        // picIndexLists = new ArrayList<Long>()
        picPathLists = new ArrayList<>();
        picPositionLists = new ArrayList<>();
    }

    private int getScreenWidth(Context context2) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        helper = SLFViewHolder.get(context, convertView, parent, R.layout.slf_photo_list_item, position);
        convertView = helper.getConvertView();
        if(mDatas!=null && !mDatas.isEmpty()){
            SLFMediaData mSLFMediaData = mDatas.get(position);
            if(mSLFMediaData!=null){
                if(position!=mDatas.size()-1) {
                    SLFImageUtil.loadImage(context, mSLFMediaData.getOriginalPath(), helper.getView(R.id.slf_iv_photo));
                }else{
                    helper.setImageResource(R.id.slf_iv_photo,R.drawable.slf_camra_icon);
                }
                if(picPathLists.size()>=selectedNum){
                    if(picPathLists.contains(mDatas.get(position))||position==mDatas.size()-1){
                        helper.setVisibility(R.id.slf_iv_photo_mask,false);
                    }else {
                        helper.setVisibility(R.id.slf_iv_photo_mask, true);
                    }
                }else{
                    helper.setVisibility(R.id.slf_iv_photo_mask,false);
                }
                if(mSLFMediaData.getMimeType().contains("video")){
//					WpkImageUtil.loadImage(context,WpkImageUtil.getVideoUriById(mSLFMediaData.getId()),helper.getView(R.id.iv_photo))
                    helper.setVisibility(R.id.slf_tv_media_type,false);
                    helper.setVisibility(R.id.slf_iv_video,true);
                    helper.setVisibility(R.id.slf_tv_video_duration,true);
                    helper.setText(R.id.slf_tv_video_duration, SLFDateFormatUtils.timeStampToDate(context,mSLFMediaData.getDuration()));
                }else if(mSLFMediaData.getMimeType().contains("gif")){
//					WpkImageUtil.loadImage(context,WpkImageUtil.getImageUriById(mSLFMediaData.getId()),helper.getView(R.id.iv_photo))
                    helper.setText(R.id.slf_tv_media_type,"gif");
                    helper.setVisibility(R.id.slf_iv_video,false);
                    helper.setVisibility(R.id.slf_tv_media_type,true);
                    helper.setVisibility(R.id.slf_tv_video_duration,false);
                }else{
//					WpkImageUtil.loadImage(context,WpkImageUtil.getImageUriById(mSLFMediaData.getId()),helper.getView(R.id.iv_photo))
                    helper.setVisibility(R.id.slf_tv_media_type,false);
                    helper.setVisibility(R.id.slf_iv_video,false);
                    helper.setVisibility(R.id.slf_tv_video_duration,false);
                }
            }

            final CheckBox checkedPhoto = convertView.findViewById(R.id.slf_checked_btn);
            //final CheckBox checkedNum = convertView.findViewById(R.id.slf_checked_num);
            //相机位置与图片的区分
            if(position == mDatas.size() -1) {
                checkedPhoto.setVisibility(View.GONE);
            }else {
                checkedPhoto.setVisibility(View.VISIBLE);
                if (picPathLists.contains(mDatas.get(position))) {
                    checkedPhoto.setChecked(true);
                } else {
                    checkedPhoto.setChecked(false);
                }
            }
        }


        ViewGroup.LayoutParams lp = convertView.getLayoutParams();
        lp.height = mScreenWidth / 4 - 8;
        convertView.setLayoutParams(lp);

        return convertView;
    }

    public ArrayList<String> getPickPositions(){
        return picPositionLists;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;

            if (picPathLists.contains(mDatas.get(currentPosition))) {
                picPathLists.remove(mDatas.get(currentPosition));
                picPositionLists.remove(String.valueOf(currentPosition));
            } else {
                if(picPathLists.size() >= selectedNum){
                   // SLFToastUtil.showText(context.getString(R.string.select_pic_over_hint)+" "+selectedNum+"！");
					Toast.makeText(context, context.getString(R.string.slf_only_select_3), Toast.LENGTH_SHORT).show();
                    return;
                }
                picPathLists.add(mDatas.get(currentPosition));
                picPositionLists.add(String.valueOf(currentPosition));
            }

//        }
        notifyDataSetChanged();
    }

    public boolean ischecked() {
        return isChecked;
    }

    public ArrayList<SLFMediaData> getPicList() {
        return picPathLists;
    }


}
