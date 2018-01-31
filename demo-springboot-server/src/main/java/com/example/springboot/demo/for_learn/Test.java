package com.example.springboot.demo.for_learn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component//不开启此扫描的话，类中@AutoWired属性无法注入
public class Test {

    @Autowired
    Environment environment;

    public String property(){
        return environment.getProperty("demo2.mysql.pass",String.class,"123");
    }
}
