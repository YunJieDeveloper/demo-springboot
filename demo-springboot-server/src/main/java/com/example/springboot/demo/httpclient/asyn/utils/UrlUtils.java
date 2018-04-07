package com.example.springboot.demo.httpclient.asyn.utils;


import com.example.springboot.demo.httpclient.asyn.entity.ProxyServerInfo;
import com.example.springboot.demo.httpclient.asyn.entity.UrlEntity;
import com.google.common.net.HttpHeaders;
import com.ning.http.client.ProxyServer;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.cookie.Cookie;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MimeTypeUtils;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class UrlUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlUtils.class);

    public static String fillUrlUsingParameterMap(String url, Map<String, String> parameterMap)
            throws Exception {
        if (url == null) {
            return null;
        }
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            url = url.replaceAll("\\$\\{" + entry.getKey() + "\\}", entry.getValue());
        }
        if (url.contains("${now}")) {
            url = url.replaceAll("\\$\\{now\\}", System.currentTimeMillis() + "");
        }
        if (url.matches(".+(\\$\\{.+\\}).*")) {
            LOGGER.error("{} contains undefined parameters", url);
            throw new Exception("{} contains undefined parameters");    // 失败,从头重试
        }
        return url;
    }

    public static String[] split(String data, String splitChar) {
        if (StringUtils.isNotEmpty(data) && StringUtils
                .isNotEmpty(splitChar)) {
            return StringUtils.split(data, splitChar);
        }
        return null;
    }


    public static Request buildHttpRequestByUrl(UrlEntity url, Set<Cookie> reqCookies, Map<String, String> parameterMap, ProxyServerInfo proxy)
            throws Exception {
        RequestBuilder requestBuilder = new RequestBuilder();
        if (proxy != null) {
            if ((StringUtils.isNotBlank(proxy.getUsername())) && (StringUtils.isNotBlank(proxy.getPassword()))) {
                requestBuilder.setProxyServer(new ProxyServer(ProxyServer.Protocol.HTTPS, proxy.getIpaddr(), proxy.getPort(), proxy
                        .getUsername(), proxy.getPassword()));
            } else {
                requestBuilder.setProxyServer(new ProxyServer(ProxyServer.Protocol.HTTPS, proxy.getIpaddr(), proxy.getPort()));
            }
        }
        URL netUrl;
        try {
            netUrl = new URL(url.getUrl());
        } catch (MalformedURLException e) {
            throw new Exception( "URL 格式错误");
        }
        for (Cookie cookie : reqCookies) {

            if (StringUtils.isBlank(cookie.getDomain()) || netUrl.getHost().endsWith(cookie.getDomain())) {
                requestBuilder.addOrReplaceCookie(cookie);
            }
        }

        boolean hasContentType = false;
        if (url.getHeads() != null && !url.getHeads().isEmpty()) {
            for (Map.Entry<String, String> entry : url.getHeads().entrySet()) {
                if (entry.getKey().equals(HttpHeaders.CONTENT_TYPE)) {
                    hasContentType = true;
                }
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (!url.getUrl().startsWith("http")) {
            url.setUrl("http://" + url.getUrl());
        }
        requestBuilder.setUrl(url.getUrl());

        String postBody = fillUrlUsingParameterMap(url.getPostBody(), parameterMap);
        LOGGER.debug("call url {} body {}", url.getUrl(), postBody);
        String method = url.getMethod();
        if (method.equals("POST") && postBody != null) {
            if (!hasContentType) {
                requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE,
                        MimeTypeUtils.APPLICATION_FORM_URLENCODED_VALUE + "; charset=UTF-8");
            }
            String[] postParameters = split(postBody, "&");
            if (ArrayUtils.isNotEmpty(postParameters)) {
                for (String param : postParameters) {
                    String[] dataS = org.springframework.util.StringUtils.split(param, "=");
                    if (dataS != null && dataS.length == 2) {
                        try {
                            String key = dataS[1];
                            if (key.contains("%")) {
                                key = URLDecoder.decode(key, "UTF-8");
                            }
                            requestBuilder.addFormParam(dataS[0], key);
                        } catch (UnsupportedEncodingException e) {
                            //ignore
                        }
                    }
                }
            }
        } else if (method.equals("POST_STRING") && postBody != null) {
            method = "POST";
            if (!hasContentType) {
                requestBuilder.addHeader(HttpHeaders.CONTENT_TYPE,
                        MimeTypeUtils.APPLICATION_FORM_URLENCODED_VALUE + "; charset=UTF-8");
            }

            try {
                byte[] bytes = postBody.getBytes("UTF-8");
                requestBuilder.setBody(bytes);
                requestBuilder.setBodyEncoding("UTF-8");
                requestBuilder.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.length));

            } catch (UnsupportedEncodingException e) {
                //ignore
            }

        }
        if (postBody == null && "POST".equals(url.getMethod())) {
            requestBuilder.addHeader(HttpHeaders.CONTENT_LENGTH, "0");
        }

        requestBuilder.setMethod(method);

        requestBuilder.addHeader(HttpHeaders.USER_AGENT,
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:40.0) Gecko/20100101 Firefox/40.0");

        requestBuilder.setFollowRedirects(false);
        return requestBuilder.build();
    }


    public static void addOrReplaceCookie(Cookie cookie, Set<Cookie> cookies) {
        if (cookie == null) {
            return;
        }
        Iterator<Cookie> it = cookies.iterator();
        while (it.hasNext()) {
            boolean sameDomain = false;
            Cookie c = it.next();
            if (c.getDomain() != null && c.getDomain().equals(cookie.getDomain())) {
                sameDomain = true;
            } else if (c.getDomain() == null && cookie.getDomain() == null) {
                sameDomain = true;
            }
            if (sameDomain) {
                if (c.getName().equals(cookie.getName())) {
                    it.remove();
                    break;
                }
            }

        }
        cookies.add(cookie);
    }


    public static String getCookieString(Set<Cookie> cookies, String url) throws Exception {
        StringBuffer cookieString = new StringBuffer();
        URL netUrl;
        try {
            netUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new Exception("Url 格式错误");
        }
        for (Cookie cookie : cookies) {
            if (StringUtils.isBlank(cookie.getDomain()) || netUrl.getHost().endsWith(cookie.getDomain())) {
                cookieString.append("; ");
                cookieString.append(cookie.getName());
                cookieString.append("=");
                if (cookie.isWrap()) {
                    cookieString.append("\"").append(cookie.getValue()).append('\"');
                } else {
                    cookieString.append(cookie.getValue());
                }
            }
        }
        return cookieString.toString();
    }
}
