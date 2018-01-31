package com.example.springboot.demospringbootserver;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleTest extends BaseSpringTest {
     @Autowired
    com.example.springboot.demo.for_learn.Test test;
    @Test
    public void test(){
        String property = test.property();
        System.out.println(property);
    }

}
