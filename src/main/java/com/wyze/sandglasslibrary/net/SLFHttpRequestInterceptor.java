package com.wyze.sandglasslibrary.net;

import android.util.Log;

import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by wangjian on 2022/12/5
 */
public class SLFHttpRequestInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        if (request.body() != null) {
            printParams(request.body());
        }
        SLFLogUtil.d("request", "请求体返回：| Response:" + content);
        SLFLogUtil.d("request", "----------请求耗时:" + duration + "毫秒----------");
        SLFLogUtil.d("request", "请求体返回：| Response 时间戳:" +System.currentTimeMillis());
        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build();
    }

    private void printParams(RequestBody body) {
        Buffer buffer = new Buffer();
        try {
            body.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF_8);
            }
            String params = buffer.readString(charset);
            //SLFLogUtil.d("request", "请求参数： | " + params);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}