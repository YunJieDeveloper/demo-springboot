package com.example.springboot.demo.httpclient.asyn.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/***
 * @Date 2018/4/3
 * @Description  url包装类
 * @author zhanghesheng
 * */
@Data
public class UrlEntity {

    private String method;

    private String url;

    private String postBody;

    private Map<String, String> heads = new HashMap<>();
}
