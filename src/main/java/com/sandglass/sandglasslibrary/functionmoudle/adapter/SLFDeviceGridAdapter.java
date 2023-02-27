package com.sandglass.sandglasslibrary.functionmoudle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.recycler.SLFRecyclerAdatper;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.recycler.SLFRecyclerHolder;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQBean;
import com.sandglass.sandglasslibrary.utils.SLFImageUtil;

import java.util.List;

/**
 * created by yangjie
 * describe:所有设备列表adapter
 * time: 2023/2/20
 */
public class SLFDeviceGridAdapter extends SLFRecyclerAdatper<SLFFirstPageFAQBean> {

    private Context mContext;
    private List<SLFFirstPageFAQBean> mDatas;


    public SLFDeviceGridAdapter(Context context, List<SLFFirstPageFAQBean> list) {
        super(context, (List<SLFFirstPageFAQBean>) list);
        this.mContext = context;
        this.mDatas = list;
    }


    @Override
    public int getContentView(int i) {
            return R.layout.slf_help_and_feedback_icon_item;
    }

    @Override
    public void onInitView(SLFRecyclerHolder holder, SLFFirstPageFAQBean object, int position) {
        CheckBox slf_device_item_checked = holder.getView(R.id.slf_device_item_checked);
        ImageView slf_device_item_icon_img = holder.getView(R.id.slf_device_item_icon_img);
        TextView slf_device_item_name = holder.getView(R.id.slf_device_item_name);
        if(!TextUtils.isEmpty(object.getIconUrl())){
            SLFImageUtil.loadImage(mContext,object.getIconUrl(),slf_device_item_icon_img,R.drawable.slf_photo_adapter_defult_icon,R.drawable.slf_photo_adapter_defult_icon);
        }else{
            slf_device_item_icon_img.setImageResource(R.drawable.slf_center_default_icon);
        }
        slf_device_item_name.setText(object.getName());
        slf_device_item_checked.setChecked(object.isChecked());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public int getType(int position) {
        return 0;
    }
}
