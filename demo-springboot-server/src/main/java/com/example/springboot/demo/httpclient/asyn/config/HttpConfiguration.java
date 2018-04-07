package com.example.springboot.demo.httpclient.asyn.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties(prefix = "moxie.gecko")
public class HttpConfiguration {

    @NotNull
    @Value("10000")
    private Integer crawlSoTimeout; //Millisecond

    @NotNull
    @Value("50")
    private Integer loginMaxConnections;

    @NotNull
    @Value("200")
    private Integer loginMaxConnectionsPerHost;

    @NotNull
    @Value("10000")
    private Integer connectionTimeout;//Millisecond

    @NotNull
    @Value("10000")
    private Integer readTimeout; //Millisecond

    @NotNull
    @Value("60000")
    private Integer RequestTimeout; //Millisecond



    @NotNull
    @Value("3")
    private Integer crawlRetryTimes;

    @NotNull
    @Value("3")
    private Integer loginRetryTimes;

    @NotNull
    @Value("30000")
    private Integer waitTimeForVisitTooOften;//Millisecond

    @NotNull
    @Value("300")
    private Integer crawlSleepBaseTime;//Millisecond
}
