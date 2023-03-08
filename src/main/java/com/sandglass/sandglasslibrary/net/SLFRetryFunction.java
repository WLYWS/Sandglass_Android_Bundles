package com.sandglass.sandglasslibrary.net;

import android.util.Log;

import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;

/**
 * Created by wangjian on 2022/12/5
 */
public class SLFRetryFunction implements Function<Observable<Throwable>, ObservableSource <?>> {

    // 可重试次数
    private int maxConnectCount = 0;
    // 当前已重试次数
    private int currentRetryCount = 0;
    // 重试等待时间
    private int waitRetryTime = 0;

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Throwable {
        // 参数Observable<Throwable>中的泛型 = 上游操作符抛出的异常，可通过该条件来判断异常的类型
        return throwableObservable.flatMap(new Function <Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                SLFLogUtil.e("SLFRetryFunction","throwable:::"+throwable.getMessage());
                // 输出异常信息
               // LogUtil.d(SLFHttpRequestConstants.TAG, "发生异常 = " + throwable.toString());

                /**
                 * 需求1：根据异常类型选择是否重试
                 * 即，当发生的异常 = 网络异常 = IO异常 才选择重试
                 */
                if (throwable instanceof IOException) {
                    SLFLogUtil.d(SLFHttpRequestConstants.TAG, "throwable message="+throwable.getMessage());

                    /**
                     * 需求2：限制重试次数
                     * 即，当已重试次数 < 设置的重试次数，才选择重试
                     */
                    if (currentRetryCount < maxConnectCount) {

                        // 记录重试次数
                        currentRetryCount++;
                        Log.d(SLFHttpRequestConstants.TAG, "重试次数 = " + currentRetryCount);

                        /**
                         * 需求2：实现重试
                         * 通过返回的Observable发送的事件 = Next事件，从而使得retryWhen（）重订阅，最终实现重试功能
                         *
                         * 需求3：延迟1段时间再重试
                         * 采用delay操作符 = 延迟一段时间发送，以实现重试间隔设置
                         *
                         * 需求4：遇到的异常越多，时间越长
                         * 在delay操作符的等待时间内设置 = 每重试1次，增多延迟重试时间1s
                         */
                        // 设置等待时间
                        waitRetryTime = 1000 + currentRetryCount * 1000;
                        Log.d(SLFHttpRequestConstants.TAG, "等待时间 =" + waitRetryTime);
                        return Observable.just(1).delay(waitRetryTime, TimeUnit.MILLISECONDS);
                    } else {
                        // 若重试次数已 > 设置重试次数，则不重试
                        // 通过发送error来停止重试（可在观察者的onError（）中获取信息）
                        return Observable.error(new Throwable("重试次数已超过设置次数 = " + currentRetryCount + "，即 不再重试"));
                    }
                }
                // 若发生的异常不属于I/O异常，则不重试
                // 通过返回的Observable发送的事件 = Error事件 实现（可在观察者的onError（）中获取信息）
                else {
                    return Observable.error(new Throwable("发生了非网络异常（非I/O异常）"));
                }
            }
        });
    }
}
