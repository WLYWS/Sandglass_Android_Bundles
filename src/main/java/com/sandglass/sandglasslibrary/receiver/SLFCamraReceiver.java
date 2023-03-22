package com.sandglass.sandglasslibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

public class SLFCamraReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SLFLogUtil.sdkd("yj", "监听到了拍照完毕的广播ttttttt");
        if(intent.getAction().equals("com.android.camera.NEW_PICTURE")||intent.getAction().equals("android.hardware.action.NEW_PICTURE")) {
            SLFLogUtil.sdkd("yj", "监听到了拍照完毕的广播");
        }else if(intent.getAction().equals("com.android.camera.NEW_VIDEO")||intent.getAction().equals("android.hardware.action.NEW_VIDEO")){
            SLFLogUtil.sdkd("yj","监听到了录制完成的广播");
        }
//        Cursor cursor = context.getContentResolver().query(intent.getData(),
//                null, null, null, null);
//        cursor.moveToFirst();
//        @SuppressLint("Range")
//        String path = cursor.getString(cursor.getColumnIndex("_data"));
//        String name=context.getString(cursor.getColumnIndex("_display_name"));
//        SLFLogUtil.sdkd("yj","path:"+path+" name:"+name);
    }
}
