package com.example.springboot.demo.httpclient.asyn;

import com.example.springboot.demo.httpclient.asyn.config.hander.HttpResponseHandler;
import com.example.springboot.demo.httpclient.asyn.utils.UrlUtils;
import com.ning.http.client.*;
import com.ning.http.client.cookie.Cookie;
import com.ning.http.client.cookie.CookieDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class DemoHttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoHttpClient.class);
    private static final String HTTP_HEADER_SET_COOKIE = "Set-Cookie";
    private AsyncHttpClient httpClient;


    public DemoHttpClient(AsyncHttpClient client) {
        this.httpClient = client;
    }

    /**
     * 调用此方法之前，需要实现HttpResponseHandler里的抽象方法（可通过匿名函数实现）。
     *
     * 调用http请求（post，get等）
     * */
    public Response executeRequestWithRetries(Request req, Set<Cookie> cookies,
                                              HttpResponseHandler handler, int retryCount, int retryBaseTime, boolean isOptional) throws Exception {
        int count = 0;
        Response response = null;
        URI uri = URI.create(req.getUrl());

        while (count < retryCount) {
            if (count != 0) {
                // 尝试机制被触发时,基于时间随机等待*毫秒
                try {
                    int minSleepTime = retryBaseTime;
                    int maxSleepTime = retryBaseTime * retryCount;
                    int sleepTime = new Random().nextInt(maxSleepTime)%(maxSleepTime-minSleepTime+1) + minSleepTime;
                    LOGGER.debug("login request url[{}] failed, retry[{}] times, sleeptime[{}]ms", uri, count, sleepTime);
                    TimeUnit.MILLISECONDS.sleep(sleepTime);
                } catch (InterruptedException e) {
                    LOGGER.error("login request url[{}] sleep failed", uri, e);
                }
            }
            try {
                ListenableFuture<Response> future = httpClient.executeRequest(req);
                while (response == null) {//this is a bug for ahc
                    response = future.get();
                }
                //todo 抽象方法，调用时自己实现
                 handler.handleHttpCode(response, isOptional);
                break;
            } catch (CancellationException e) { // 如果取消了一个任务，那么再次从任务中获取执行结果
                LOGGER.error("登录线程被取消,无法获取执行结果", e);
                throw new Exception("登录线程被取消,无法获取执行结果");
            } catch (InterruptedException e) {  // 线程已经被中断
                LOGGER.error("登录线程被中断", e);
                throw new Exception("登录线程被中断");
            } catch (ExecutionException e) {
                LOGGER.error("Request {} met exception", req.getUrl(), e);
                count++;
                if (count >= retryCount) {
                    throw new Exception("网络异常");
                }
            } catch (Exception e) {
                LOGGER.error("Request {} met exception", req.getUrl(), e);
                count++;
                if (count >= retryCount) {
                    throw new Exception("网络异常");
                }
            }
        }
        if (response == null) {
            LOGGER.error("login request{} response is null", req.getUrl());
            throw new Exception("login request{} response is null");
        }

        List<String> setcookies = response.getHeaders(HTTP_HEADER_SET_COOKIE);
        for (String scookies : setcookies) {
            Cookie cookie = CookieDecoder.decode(scookies);
            if (StringUtils.isBlank(cookie.getDomain())) {
                cookie = new Cookie(cookie.getName(), cookie.getValue(), cookie.isWrap(), uri.getHost(),
                        cookie.getPath(), cookie.getMaxAge(), cookie.isSecure(), cookie.isHttpOnly());
            }
            UrlUtils.addOrReplaceCookie(cookie,cookies);
        }

        return response;
    }


}
