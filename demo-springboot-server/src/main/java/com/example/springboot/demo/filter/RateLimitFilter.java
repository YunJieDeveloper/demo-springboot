package com.example.springboot.demo.filter;

/***
 * @Date 2018/1/24
 * @Description 限流
 * @author zhanghesheng
 * */
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class RateLimitFilter implements Filter {
  private static  Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    /**限流请求处理类*/
  private RateLimitHandler rateLimitHandler;

    public RateLimitFilter(RateLimitHandler rateLimitHandler) {
        this.rateLimitHandler = rateLimitHandler;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException{
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        try {
            //限流逻辑
            rateLimitHandler.recordRate(uri);

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            log.error("action=rateLimit Too many request,url={}",  uri);
            this.sendError(request, (HttpServletResponse)servletResponse,e);
        }

    }

    @Override
    public void destroy() {

    }

    private void sendError(HttpServletRequest req, HttpServletResponse rsp,Exception e) throws IOException {
        rsp.reset();
        rsp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        rsp.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
        rsp.setStatus(429);
        String errorStr = new ObjectMapper().writeValueAsString(URI.create(req.getRequestURI())+"\t"+e.getMessage());
        rsp.getWriter().write(errorStr);
    }
}
