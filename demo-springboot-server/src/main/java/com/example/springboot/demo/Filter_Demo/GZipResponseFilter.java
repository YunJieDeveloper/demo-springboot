package com.example.springboot.demo.Filter_Demo;


import org.springframework.http.HttpHeaders;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GZipResponseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request =(HttpServletRequest) servletRequest;
        HttpServletResponse response =(HttpServletResponse) servletResponse;
        // Content-Encoding 设置为 none 不进行压缩处理
        if (request.getHeader(HttpHeaders.ACCEPT_ENCODING) != null &&
                request.getHeader(HttpHeaders.ACCEPT_ENCODING).contains("none")) {
            filterChain.doFilter(request, response);
        } else {
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(request, responseWrapper);
            byte[] gzipData = gzip(responseWrapper.getContentAsByteArray());
            responseWrapper.addHeader(HttpHeaders.CONTENT_ENCODING, "gzip");
            responseWrapper.resetBuffer();
            responseWrapper.setContentLength(gzipData.length);
            responseWrapper.getResponse().getOutputStream().write(gzipData);
        }
    }

    @Override
    public void destroy() {

    }


    private byte[] gzip(byte[] data) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(10240);
        GZIPOutputStream output = null;
        try {
            output = new GZIPOutputStream(byteOutput);
            output.write(data);
        } catch (IOException e) {
        } finally {
            try {
                output.close();
            } catch (IOException e) {
            }
        }
        return byteOutput.toByteArray();
    }
}
