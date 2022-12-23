package com.wyze.sandglasslibrary.functionmoudle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.moudle.SLFDeviceTypes;
import com.wyze.sandglasslibrary.moudle.SLFMediaData;
import com.wyze.sandglasslibrary.utils.SLFAdapterUtils;
import com.wyze.sandglasslibrary.utils.SLFImageShapes;
import com.wyze.sandglasslibrary.utils.SLFImageUtil;
import com.wyze.sandglasslibrary.utils.SLFStringFormatUtil;

import java.util.List;

/**
 * created by yangjie
 * describe:所有设备列表adapter
 * time: 2022/12/22
 */
public class SLFDeviceGridAdapter extends SLFQuickAdapter<SLFDeviceTypes> {

    private Context mContext;

    public SLFDeviceGridAdapter(Context context, List<SLFDeviceTypes> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    public void convert(final SLFAdapterUtils helper, final SLFDeviceTypes object, int position) {

        if(!TextUtils.isEmpty(object.getUrl())){
            SLFImageUtil.loadImage(getContext(),object.getUrl()
                    ,(ImageView) helper.getView(R.id.slf_iv_photo),R.drawable.slf_photo_adapter_defult_icon,R.drawable.slf_photo_adapter_defult_icon);
        }else{
            helper.setImageResource(R.id.slf_help_and_feedback_device_icon,R.drawable.slf_device_icon_demo);
        }
            helper.setText(R.id.slf_help_and_feedback_device_name,object.getName());
    }

    @Override
    public int getContentView() {
        return R.layout.slf_help_and_feedback_grid_item;
    }


}
