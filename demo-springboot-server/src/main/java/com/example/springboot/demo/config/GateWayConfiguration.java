package com.example.springboot.demo.config;

import com.example.springboot.demo.Interceptor.DemoInterceptorV1;
import com.example.springboot.demo.filter.GZipResponseFilter;
import com.example.springboot.demo.filter.RateLimitFilter;
import com.example.springboot.demo.filter.RateLimitHandler;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/***
 * @Date 2017/12/20
 *@Description 配置类 :
 *             配置依赖Bean、Filter-->自定义filter、 Interceptor->自定义interceptor(需要继承WebMvcConfigurerAdapter)
 * @author zhanghesheng
 * */
@Configuration
public class GateWayConfiguration  extends WebMvcConfigurerAdapter {

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


    /**
     * 配置静态资源
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //(默认)
      //  registry.addResourceHandler("/static/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/static/");
       // registry.addResourceHandler("/templates/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX+"/templates/");

       //方式一：通过addResourceHandler添加映射路径，通过addResourceLocations指定外部的目录
        registry.addResourceHandler("/my/elephant.jpg").addResourceLocations(ResourceUtils.FILE_URL_PREFIX+"E:/my/");

       //方式二：通过addResourceHandler添加映射路径，通过addResourceLocations来指定路径
        registry.addResourceHandler(new String[]{"swagger-ui.html"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/"});
        super.addResourceHandlers(registry);
    }

    /**添加自定义拦截器**/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截规则：除了login，其他都拦截判断
        registry.addInterceptor(new DemoInterceptorV1()).addPathPatterns("/**").excludePathPatterns("/login");
        super.addInterceptors(registry);
    }
}
