package com.sandglass.sandglasslibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.punet.punetwork.net.PUNApiContant;
import com.punet.punetwork.net.PUNHttpRequestCallback;
import com.punet.punetwork.net.PUNHttpRequestConstants;
import com.punet.punetwork.net.PUNHttpUtils;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.bean.SLFSPContant;
import com.sandglass.sandglasslibrary.moudle.event.SLFUnReadtoReadAllEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFUpdateFaqCategoryEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFUpdateFaqDetailEvent;
import com.sandglass.sandglasslibrary.moudle.event.SLFUpdateFeedbackCategoryEvent;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFaqWelcomeHotQResponseBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFeedBackCacheTimeData;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFeedBackCacheTimeResponseBean;
import com.sandglass.sandglasslibrary.utils.SLFSpUtils;
import com.logutil.logutil.SLFLogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.Nullable;

/**用于后台请求缓存相关接口
 * Created by wangjian on 2023/3/23
 */
public class SLFUpdateCacheService extends Service implements PUNHttpRequestCallback {

    public static final String TAG = SLFUpdateCacheService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    @Override
    public void onCreate ( ) {
        super.onCreate();
        getFeedBackCacheTime();
    }

    /**
     * 获取数据更新时间
     */
    private void getFeedBackCacheTime() {
        SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":onRequestUrl（Get）:"+PUNHttpRequestConstants.BASE_URL + PUNApiContant.FEEDBACK_UPDATE_CACHE);
        PUNHttpUtils.getInstance().executePathGet(this, PUNHttpRequestConstants.BASE_URL + PUNApiContant.FEEDBACK_UPDATE_CACHE, SLFFeedBackCacheTimeResponseBean.class, this);
    }

    @Override
    public void onRequestNetFail (Object bean) {
        SLFLogUtil.sdke(TAG,"获取数据更新时间没网");
    }

    @Override
    public void onRequestSuccess (String result, Object type) {
        SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":onRequestSuccess:type:("+type.getClass()+")\nresult:"+result);
        if (type instanceof SLFFeedBackCacheTimeResponseBean) {
            SLFFeedBackCacheTimeResponseBean bean = (SLFFeedBackCacheTimeResponseBean) type;
            if (bean.data.feedbackCategory != SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FEEDBACKCATEGORY, -1)) {
                //通知反馈分类更新
                SLFSpUtils.put(SLFSPContant.UPDATE_TIME_FEEDBACKCATEGORY_CACHE, bean.data.feedbackCategory);
            }

            if (bean.data.faqCategory != SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FAQCATEGORY, -1)) {
                //通知faq分类更新
                SLFSpUtils.put(SLFSPContant.UPDATE_TIME_FAQCATEGORY_CACHE, bean.data.faqCategory);
                EventBus.getDefault().post(new SLFUpdateFaqCategoryEvent(bean.data.faqCategory));
            }

            if (bean.data.faqDetail != SLFSpUtils.getLong(SLFSPContant.UPDATE_TIME_FAQDETAIL, -1)) {
                //通知faq分类更新
                SLFSpUtils.put(SLFSPContant.UPDATE_TIME_FAQDETAIL_CACHE, bean.data.faqDetail);
                EventBus.getDefault().post(new SLFUpdateFaqDetailEvent(bean.data.faqDetail));
            }
        }
    }

    @Override
    public void onRequestFail (String value, String failCode, Object bean) {
        //非网络错误，接口请求错误
        SLFLogUtil.sdke(TAG,"获取数据更新时间,非网络错误，接口请求错误");
        SLFLogUtil.sdke(TAG, "ActivityName:"+this.getClass().getSimpleName()+":onRequestFail:failCode:"+failCode+"*value:"+value);
    }

}
