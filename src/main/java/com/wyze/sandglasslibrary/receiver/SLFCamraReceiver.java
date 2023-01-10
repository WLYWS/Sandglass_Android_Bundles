package com.wyze.sandglasslibrary.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

public class SLFCamraReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SLFLogUtil.d("yj","监听到了摄像完毕的广播");
//        Cursor cursor = context.getContentResolver().query(intent.getData(),
//                null, null, null, null);
//        cursor.moveToFirst();
//        @SuppressLint("Range")
//        String path = cursor.getString(cursor.getColumnIndex("_data"));
//        String name=context.getString(cursor.getColumnIndex("_display_name"));
//        SLFLogUtil.d("yj","path:"+path+" name:"+name);
    }
}
