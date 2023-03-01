package com.sandglass.sandglasslibrary.functionmoudle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.moudle.SLFPhotoFolderInfo;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.utils.SLFViewHolder;

import java.util.List;

public class SLFFileListAdapter extends BaseAdapter {

    private Context context;
    private List<SLFPhotoFolderInfo> mDatas;

    public SLFFileListAdapter(Context context, List<SLFPhotoFolderInfo> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
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
    public View getView(final int position, View convertView, ViewGroup arg2) {

        SLFViewHolder helper = SLFViewHolder.get(context, convertView, arg2, R.layout.slf_photo_file_list_item, position);

        SLFPhotoFolderInfo mFolderInfo = mDatas.get(position);
        if(mFolderInfo!=null){
            if(mFolderInfo.getCoverPhoto()!=null){
                helper.setImageByUrl(R.id.slf_iv_photo, mFolderInfo.getCoverPhoto().getOriginalPath());
            }
            helper.setText(R.id.slf_tv_file_name, mFolderInfo.getFolderName()+" ("+mFolderInfo.getPhotoList().size()+")");
            SLFFontSet.setSLF_RegularFont(context,helper.getView(R.id.slf_tv_file_name));
        }

        convertView = helper.getConvertView();
        return convertView;
    }


}
