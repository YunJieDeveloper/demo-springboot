package com.example.springboot.demospringbootserver;

import com.example.springboot.demo.for_learn.RedisDemo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisTest extends BaseSpringTest {

    @Autowired
    RedisDemo redisDemo;

    @Test
    public  void stringRedis(){

        redisDemo.setStringRedis("stringKey","stringValue");
        System.out.println(redisDemo.getStringRedis("stringKey"));
    }

}
