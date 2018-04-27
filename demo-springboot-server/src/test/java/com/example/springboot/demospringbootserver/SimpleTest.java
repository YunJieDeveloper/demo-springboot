package com.example.springboot.demospringbootserver;

import com.example.dao.springMongodb.model.User;
import com.example.springboot.service.MongoDbService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleTest extends BaseSpringTest {
     @Autowired
    com.example.springboot.demo.template.Test test;

     @Autowired
    MongoDbService mongoDbService;
    @Test
    public void test(){
        String property = test.property();
        System.out.println(property);
    }
    @Test
    public void testMongo(){
        User userByName = mongoDbService.getUserByName("zhangsan");
        System.out.println(userByName);
    }

}
