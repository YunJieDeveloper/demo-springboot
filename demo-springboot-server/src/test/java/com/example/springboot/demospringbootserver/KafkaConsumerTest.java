package com.example.springboot.demospringbootserver;

import com.example.springboot.demo.template.JavaMailSenderDemo;
import com.example.springboot.demo.kafka.consumer.ConsumerThreadPool;
import com.example.springboot.demo.kafka.consumer.KafkaConsumerExecutorFactory;
import com.example.springboot.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class) //1.5.8 若无此注解，@Autowired 无法引入类实例 NullPointerException
@SpringBootTest //1.5.8 若无此注解,无法扫描到@Autowired引入的类 NoSuchBeanDefinitionException
//@SpringApplicationConfiguration(classes = DemoMicroService.class)
//@WebIntegrationTest({"server.port:8282", "service.tag:local"})// 使用0表示端口号随机，也可以具体指定如8888这样的固定端口
public class KafkaConsumerTest {

    @Autowired
    DemoService demoService;


    @Autowired
    KafkaConsumerExecutorFactory executorFactory;

    @Test
    public void contextLoads() {
        teststr("a", null);
    }

    public List<String> teststr(String... arg) {
        return Arrays.asList(arg);
    }

    @Test
    public void testService() {
        try {
            String b2 = demoService.getB2(1l);
            System.out.println(b2);
            Thread.sleep(100 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    /**
     *@author zhanghesheng
     *@Description kafka消费者 必须保证produce生产者处于运行状态，否则consumer消费者就会得不到数据，无法消费
     */
    public void consumer() {
        String groupId = "test1234";
        String[] topic = {"group2"};
        int numThreads = 3;
        String bootstrapList = "127.0.0.1:9092";
        ConsumerThreadPool consumerThreadPool = new ConsumerThreadPool(executorFactory, bootstrapList, groupId, topic, numThreads);
        consumerThreadPool.consume();
    }


}
