package com.example.springboot.demo.config;

import com.example.springboot.demo.filter.GZipResponseFilter;
import com.example.springboot.demo.filter.RateLimitFilter;
import com.example.springboot.demo.filter.RateLimitHandler;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/***
 * @Date 2017/12/20
 *@Description 配置类  配置依赖Bean、Filter-->filter
 * @author zhanghesheng
 * */
@Configuration
public class GateWayConfiguration {

    @Autowired
    private RateLimitHandler rateLimitHandler;
     /**Filter（1）过滤器配置*/
    @Bean
    @Order(1)//Filter 执行顺序
    public FilterRegistrationBean gZipResponseFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new GZipResponseFilter());
        registration.addUrlPatterns("/*");
        registration.setName("gZipResponseFilter");
        return registration;
    }

    /**Filter （2）过滤器配置*/
    @Bean
    @Order(2)
    public FilterRegistrationBean carrierRateLimitFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RateLimitFilter(rateLimitHandler));
        registration.addUrlPatterns("/jsp/*");
        registration.setName("carrierRateLimitFilter");
        return registration;
    }



    /** 调用其他工程**/
   /* @Bean
    public CrawlerManagerClientV3 crawlerManagerClientV3() {
        return new CrawlerManagerClientV3(gateWayServiceProperties.getTagCrawlerManager());
    }*/


}
