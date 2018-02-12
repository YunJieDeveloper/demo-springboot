package com.example.springboot.demo.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zhanghesheng on 2017/7/5.
 */
public interface DemoApi {
    //所调用的其他远程工程controller里的方法
    @GET("carrier-verify-service/api/v1/{task_id}/verify")
    @Headers({"Content-Type: application/json;charset=utf-8","Authorization:7034923e47aa44c6bb48e10660b771fe"})
    Call<? extends Object> getVerifyRsp(@Path("task_id") String taskId,
                                 @Query("mobile") String mobile);

}
