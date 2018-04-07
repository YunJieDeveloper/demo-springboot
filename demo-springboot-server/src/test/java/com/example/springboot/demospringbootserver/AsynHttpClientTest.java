package com.example.springboot.demospringbootserver;

import com.example.springboot.demo.httpclient.asyn.HttpService;
import com.google.common.collect.Sets;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
@Slf4j
public class AsynHttpClientTest extends BaseSpringTest {


    @Autowired
    HttpService httpService;

    @Test
    public void testAsy() throws Exception{
        RequestBuilder builder = new RequestBuilder();
        builder.setMethod("GET");
        builder.setUrl("http://www.baidu.com");
        Request request =builder.build();
      Response response= httpService.getHttpClient().executeRequestWithRetries(request, Sets.newHashSet(),
                (response1, isOptional) -> {
                    if (response1.getStatusCode() >= 400) {
                        try {
                            log.error("Request Url Error with status code: " + response1.getStatusCode() + ", body: " + response1.getResponseBody());
                        } catch (IOException e) {
                            log.error("Request Url Error with status code: " + response1.getStatusCode() + ", but get body failed.", e);
                        }

                        // 必需校验的情况下,进行重试逻辑处理
                        if (isOptional) {
                            throw  new Exception(new Exception("http request failed"));
                        }
                    }
                },2,1,true);
        String s = httpService.responseCharset(response.getContentType());
        System.out.println("+++++++++++++++++++++++++++"+response.getResponseBody(s));
    }

}
