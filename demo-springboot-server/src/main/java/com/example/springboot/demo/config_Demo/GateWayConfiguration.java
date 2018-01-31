package com.example.springboot.demo.config_Demo;

import com.example.springboot.demo.Filter_Demo.GZipResponseFilter;
import com.example.springboot.demo.Filter_Demo.RateLimitFilter;
import com.example.springboot.demo.Filter_Demo.RateLimitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/***
 * @Date 2017/12/20
 *@Description 配置类  配置依赖Bean、Filter-->Filter_Demo
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
