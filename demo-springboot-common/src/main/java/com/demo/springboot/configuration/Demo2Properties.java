package com.demo.springboot.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "demo2.mysql") //application.properties文件中某配置信息前缀名
/**
 * matchIfMissing = false(默认):如果demo2.mysql.url存在,创建该类
 * matchIfMissing = true:如果demo2.mysql.url不存在,创建该类
 * */
//@ConditionalOnProperty(prefix = "demo2.mysql", name = "url" ,matchIfMissing = false)
@Data
public class Demo2Properties {

    //@Value("${demo2.mysql.url:jdbc:mysql://127.0.0.1:3306/demo?zeroDateTimeBehavior=convertToNull&characterEncoding=UTF-8}")
    @Value("${demo2.mysql.url}")
    private String demo2Url;
    //从application.properties中获取demo.mysql.user的值，若没有，使用默认值test2
    @Value("${demo2.mysql.user}")
    private String demo2User;
    @Value("${demo2.mysql.pass:111}")
    private String demo2Pass;


}
