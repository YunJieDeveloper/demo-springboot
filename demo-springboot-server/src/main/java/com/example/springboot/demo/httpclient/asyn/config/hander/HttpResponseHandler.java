package com.example.springboot.demo.httpclient.asyn.config.hander;

import com.ning.http.client.Response;

/***
 * @Date 2018/4/3
 * @Description  http请求返回体Response处理接口
 * @author zhanghesheng
 * */
public interface HttpResponseHandler {
    void handleHttpCode(Response response, boolean isOptional) throws Exception;
}
