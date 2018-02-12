package com.example.springboot.demo.retrofit;


import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhanghesheng on 2017/7/5.
 */
@Slf4j
@Service
public class DemoClient {
    public static final String URL = "http://121.40.32.34:8181/";//要调用的工程的机器或访问路径
    private Retrofit apiFactory;
    private DemoApi api;

    @PostConstruct
    public void initMonitor() {
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true).build();
        apiFactory = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient)
                .build();
        api = apiFactory.create(DemoApi.class);
    }

    public <T> T callVerify(String taskId, String mobile) {
        Call<? extends Object> verifyRsp = api.getVerifyRsp(taskId, mobile);
        Response<? extends Object> rspResponse=null;
        try {
           rspResponse = verifyRsp.execute();
        } catch (IOException e) {
            log.error("call error", e.getMessage());
            e.printStackTrace();
        }
        return (T)rspResponse.body();
    }

}
