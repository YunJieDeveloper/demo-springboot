package com.example.springboot.controller;


import com.example.springboot.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @Autowired
    DemoService demoService;


    @GetMapping("/getbean/{id}")
    public String getBean(@PathVariable String id) {
        Long beanId = Long.valueOf(id);
        return demoService.getB1(beanId);
    }
}
