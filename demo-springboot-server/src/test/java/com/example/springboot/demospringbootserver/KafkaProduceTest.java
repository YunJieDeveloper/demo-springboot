package com.example.springboot.demospringbootserver;


import com.example.springboot.DemoSpringbootServerApplication;
import com.example.springboot.demo.kafka_demo.ConsumerThreadPool;
import com.example.springboot.demo.kafka_demo.KafkaProduce;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @Before：初始化方法 对于每一个测试方法都要执行一次（注意与BeforeClass区别，后者是对于所有方法执行一次）
 * @After：释放资源 对于每一个测试方法都要执行一次（注意与AfterClass区别，后者是对于所有方法执行一次）
 * @Test：测试方法，在这里可以测试期望异常和超时时间
 * @Test(expected=ArithmeticException.class)检查被测方法是否抛出ArithmeticException异常
 * @Ignore：忽略的测试方法
 * @BeforeClass：针对所有测试，只执行一次，且必须为static void
 * @AfterClass：针对所有测试，只执行一次，且必须为static void
 * 一个JUnit4的单元测试用例执行顺序为：
 * @BeforeClass -> @Before -> @Test -> @After -> @AfterClass;
 * 每一个测试方法的调用顺序为：
 * @Before -> @Test -> @After;
 */

//1.3.6版本
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoSpringbootServerApplication.class)
@WebIntegrationTest({"server.port:8132", "service.tag:local"})// 使用0表示端口号随机，也可以具体指定如8888这样的固定端口

public class KafkaProduceTest {

    private static final String brokerList = "127.0.0.1:9092";
    private static KafkaProduce kafkaProduce;

    @BeforeClass//针对所有测试，只执行一次，且必须为static void
    public static void beforeClass() {
        kafkaProduce = new KafkaProduce(brokerList);
    }

    @Test
    /**
    *@author zhanghesheng
    *@Description kafka生产者 必须保证程序不结束，否则consumer消费者就会得不到数据，无法消费
    */
    public void produce() {
        kafkaProduce.writeObject("group2", "this a kafka message", "group1_key");
    }


}
