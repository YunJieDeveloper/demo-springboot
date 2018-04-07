package com.example.springboot.demo.httpclient.asyn;

import com.example.springboot.demo.httpclient.asyn.config.HttpConfiguration;
import com.example.springboot.demo.httpclient.asyn.entity.TrueX509TrustManager;
import com.example.springboot.demo.httpclient.asyn.utils.RegexUtils;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.providers.netty.NettyAsyncHttpProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Http服务
 *
 */
@Slf4j
@Service
public class HttpService {

    /** 同步标识 */
    private static final Object lock = new Object();

    /** 默认Http参数配置 */
    @Autowired
    private HttpConfiguration httpConfiguration;

    /**
     * 获取具体逻辑中的HttpClient实例
     *
     * @param
     * @return
     * @throws Exception
     */
    public DemoHttpClient getHttpClient() throws Exception {
        DemoHttpClient demoHttpClient =null;
        try {   // 防止并发处理逻辑
            synchronized (lock) {
                if (null != demoHttpClient) {
                    return demoHttpClient;
                }
                SSLContext sc = SSLContext.getInstance("SSL");
                try {
                    TrustManager[] trustAllCerts = new TrustManager[]{
                            new TrueX509TrustManager(null)
                    };
                    sc.init(null, trustAllCerts, null);
                } catch (KeyStoreException | KeyManagementException e) {
                    //ignore
                }
                AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder()
                        .setSSLContext(sc)
                        .setAllowPoolingConnections(true)
                        .setAllowPoolingSslConnections(true)
                        .setAcceptAnyCertificate(true)
                        .setRequestTimeout(httpConfiguration.getRequestTimeout())
                        .setConnectTimeout(httpConfiguration.getConnectionTimeout())
                        .setReadTimeout(httpConfiguration.getReadTimeout())
                        .setFollowRedirect(false)
                        .setPooledConnectionIdleTimeout(5000)  // 等待
                        .setMaxConnections(httpConfiguration.getLoginMaxConnections())        // 最大链接数
                        .setMaxConnectionsPerHost(httpConfiguration.getLoginMaxConnectionsPerHost())
                        .setMaxRequestRetry(3)  // 自动重试2次
                        .build();

                AsyncHttpClient client= new AsyncHttpClient(new NettyAsyncHttpProvider(config), config);
                demoHttpClient =new DemoHttpClient(client);
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("build httpclient failed", e);
            throw new Exception( "build httpclient failed");
        }
        return demoHttpClient;
    }

       /**处理response乱码问题*/
    public String responseCharset(String contentType) {
        String charset = "utf-8";
        if(StringUtils.isNotBlank(contentType) && contentType.contains("charset")) {
            String value = RegexUtils.regexPattern("charset=(.*?)$",contentType,  1);
            if(StringUtils.isNotBlank(value)) {
                charset = value;
                log.debug("contentType [{}] charset = [{}]", contentType, charset);
            }
        }
        return charset;
    }
}
