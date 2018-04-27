package com.example.springboot.demo.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
@Data
@EqualsAndHashCode
@ToString
@Configuration
public class ConsulConfigProperties {
    @Value("${service.health.dependency.services:null}")
    private String healthDependServices;
    /**
     * service的名字
     */
    @Value("${service.name:carrier}")
    private String name;

    /**
     * Service 对外端口
     */
    @Value("${server.port:8080}")
    private int servicePort;

    /**
     * 主动汇报的间隔时间. 如果配置此项，则开启主动汇报模式
     */
    @Value("${consul.checks.ttl:30}")
    private int checkinTTL;


    @Value("${consul.checks.http.timeout:5}")
    private int httpTimeout;

    /**
     * Consul agent ping healthURL的间隔，以秒为单位
     */
    @Value("${consul.checks.http.interval:1}")
    private int httpInterval;

    /**
     * Consul agent 会来ping这个URL以确定service是否健康
     */
    @Value("${consul.checks.http.url:http://localhost:${server.port}/health}")
    private String httpUrl;

    /**
     * service对外暴露的端口号，如果不是在docker容器中，则取server.port
     * 如果在docker container中，则要填对外暴露的端口号
     */
    @Value("${external.port:${server.port}}")
    private int externalPort;


    @Value("${service.tag:local}")
    @NotNull
    private String serviceTag;

  //192.168.0.15 测试机器的consul
    @Value("${host.ip:192.168.0.15}")
    private String hostIp;


}
