package com.wyze.sandglasslibrary.net;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by wangjian on 2022/12/5
 */
public interface SLFApiService {
    // baseUrl = http://www.5mins-sun.com:8081/

    // 例如：http://www.baidu.com不使用baseUrl
    @GET
    Observable<String> getPath(@Url String url,@HeaderMap TreeMap <String, Object> map);

    // GET请求(无参) api = news
    // http://www.5mins-sun.com:8081/news
    @GET("{api}")
    Observable<String> getData(@Path("api") String api,@HeaderMap TreeMap <String, Object> map);

    // GET请求(带参) api = news
    // http://www.5mins-sun.com:8081/news?name=admin&pwd=123456
    @GET
    Observable<String> getData(@Url String url, @QueryMap TreeMap <String, Object> map,@HeaderMap TreeMap <String, Object> headMap);

    // POST请求(无参)
    @POST("{api}")
    Observable<String> postData(@Path(value = "api", encoded = true) String api,@HeaderMap TreeMap <String, Object> headMap);

    // POST请求,以RequestBody方式提交  api = news
    // http://www.5mins-sun.com:8081/news
    // RequestBody:
    // {
    //    "albumID": 2,
    //    "sectionID": 16
    // }
    @POST("{api}")
    Observable<String> postData(@Path(value = "api", encoded = true) String api, @Body RequestBody requestBody,@HeaderMap TreeMap <String, Object> headMap);

    // Post请求,以表单方式提交
    // http://www.5mins-sun.com:8081/news
    // user=admin
    // pwd=123456
    @FormUrlEncoded
    @POST("{api}")
    Observable<ResponseBody> postData(@Path("api") String api, @FieldMap Map <String, String> maps,@HeaderMap TreeMap <String, Object> headMap);

    // Post请求（带数组）
    @FormUrlEncoded
    @POST("{api}")
    Observable<ResponseBody> postData(@Path("api") String api, @FieldMap Map<String, String> maps,@HeaderMap TreeMap <String, Object> headMap, @Query("meta[]") String... linked);

    // 上传单个文件
    @Multipart
    @POST
    Observable<String> upload(@Url String api,@Part MultipartBody.Part file);

    // 上传多个文件
    @Multipart
    @POST
    Observable <String> uploadMultipart(@Url String api, @PartMap Map<String, RequestBody> maps, @Part List <MultipartBody.Part> params);

    @PUT
    Observable<String> putBodyFile(@Url String url, @Body RequestBody file);

}

