package com.example.springboot.controller;


import com.example.springboot.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @Autowired
    DemoService demoService;


    //@GetMapping("/getbean/{id}")//高版本新增注解
    @RequestMapping("/getbean/{id}")
    public String getBean(@PathVariable String id) {
        Long beanId = Long.valueOf(id);
        return demoService.getB2(beanId);
    }
}
