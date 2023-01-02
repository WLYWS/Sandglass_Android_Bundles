package com.wyze.sandglasslibrary.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.wyze.sandglasslibrary.base.SLFBaseApplication;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.moudle.event.SLFEventNetWorkChange;

import org.greenrobot.eventbus.EventBus;

/**
 * created by lmj
 * describe:网络切换监听
 * time: 2019/4/25
 * Copyright© 2019 WYZE.
 */
public class SLFNetworkChangeReceiver extends BroadcastReceiver {
    @SuppressWarnings("unused")
    private static final String TAG = "SLFNetworkChangeReceiver";

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    EventBus.getDefault().post(new SLFEventNetWorkChange(SLFConstants.MOBILE_AVAILABILITY));
                    break;
                case 1:
                    EventBus.getDefault().post(new SLFEventNetWorkChange(SLFConstants.WIFI_AVAILABILITY));
                    break;
                case 2:
                    EventBus.getDefault().post(new SLFEventNetWorkChange(SLFConstants.NETWORK_UNAVAILABILITY));
                    break;
                default:
                    break;
            }
        }
    };

    @Override @SuppressWarnings("java:S1874")
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isAvailable()) {

                final int netWorkState = networkInfo.getType();
                if (netWorkState == 0) {
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessageDelayed(0,1000);
//                    EventBus.getDefault().post(new MessageEvent(MessageEvent.MOBILE_AVAILABILITY))
                } else if (netWorkState == 1) {
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessageDelayed(1,1000);
//                    EventBus.getDefault().post(new MessageEvent(MessageEvent.WIFI_AVAILABILITY))
                } else if (netWorkState == -1) {
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessageDelayed(2,1000);
//                    EventBus.getDefault().post(new MessageEvent(MessageEvent.NETWORK_UNAVAILABILITY))
                }

            } else {
                EventBus.getDefault().post(new SLFEventNetWorkChange(SLFConstants.NETWORK_UNAVAILABILITY));
            }
        }
    }


}