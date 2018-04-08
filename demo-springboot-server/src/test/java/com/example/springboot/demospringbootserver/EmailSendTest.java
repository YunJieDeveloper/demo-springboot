package com.example.springboot.demospringbootserver;


import com.example.springboot.demo.template.JavaMailSenderDemo;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.util.Map;

public class EmailSendTest extends BaseSpringTest {


    @Autowired
    JavaMailSenderDemo javaMailSenderDemo;

    @Test
    public void testEmail() {


        Map<String, Object> maps = Maps.newHashMap();
        String[] messages = { "", "163.com", "qq.com", "Harmadik" };
        String subject = "";
        String[] to = {"1829546441@qq.com"};
        String html = "<html><head><META http-equiv=Content-Type content='text/html; charset=UTF-8'><title>自动发送邮件测试</title></head><body>亲，这是一封测试邮件，请不要在意，么么哒。<br/><a href='www.baidu.com' >度娘</a><br>快快点击链接去嗨皮一下吧！</body><html>";
        maps.put("messages", messages);
        try {
            // javaMailSenderDemo.sendMail("自动发送邮件测试", html,false, to);

            javaMailSenderDemo.sendMailwithTemplate("自动发送邮件测试", "email", maps, true, to);
        } catch (MessagingException e) {
            System.out.println("发送邮件失败！");
            //e.printStackTrace();
        }
    }

}
