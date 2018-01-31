package com.example.springboot.demospringbootserver;


import com.example.springboot.DemoSpringbootServerApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//1.3.6版本
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoSpringbootServerApplication.class)
@WebIntegrationTest({"server.port:8132", "service.tag:local"})// 使用0表示端口号随机，也可以具体指定如8888这样的固定端口

public class BaseSpringTest {
}
