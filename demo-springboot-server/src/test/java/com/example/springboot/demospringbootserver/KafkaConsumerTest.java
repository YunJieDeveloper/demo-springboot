package com.example.springboot.demospringbootserver;

import com.example.springboot.DemoSpringbootServerApplication;
import com.example.springboot.demo.for_learn.JavaMailSenderDemo;
import com.example.springboot.demo.kafka_demo.ConsumerThreadPool;
import com.example.springboot.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
//RunWith(SpringRunner.class) //1.5.8 若无此注解，@Autowired 无法引入类实例 NullPointerException
//@SpringBootTest //1.5.8 若无此注解,无法扫描到@Autowired引入的类 NoSuchBeanDefinitionException
@SpringApplicationConfiguration(classes = DemoSpringbootServerApplication.class)
@WebIntegrationTest({"server.port:8282", "service.tag:local"})// 使用0表示端口号随机，也可以具体指定如8888这样的固定端口
public class KafkaConsumerTest {

	@Autowired
	DemoService demoService;

	@Autowired
	JavaMailSenderDemo javaMailSenderDemo;

	@Test
	public void contextLoads() {
		teststr("a",null);
	}

	public List<String> teststr(String...arg){
		return Arrays.asList(arg);
	}

	@Test
	public void testService() {
		try {
			String b2 = demoService.getB2(1l);
			System.out.println(b2);
			Thread.sleep(100*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testEmail() {
		Map<String, Object> maps=null;
		String subject="";
		String[] to={"1829546441@qq.com","2519016990@qq.com","1793588856@qq.com","514859324@qq.com"};
		String html= "<html><head><META http-equiv=Content-Type content='text/html; charset=UTF-8'><title>自动发送邮件测试</title></head><body>亲，这是一封测试邮件，请不要在意，么么哒。<br/><a href='www.baidu.com' >度娘</a><br>快快点击链接去嗨皮一下吧！</body><html>";

		try {
			javaMailSenderDemo.sendMail("自动发送邮件测试", html,true, to);
		} catch (MessagingException e) {
			System.out.println("发送邮件失败！");
			//e.printStackTrace();
		}
	}


	@Test
	/**
	 *@author zhanghesheng
	 *@Description kafka消费者 必须保证produce生产者处于运行状态，否则consumer消费者就会得不到数据，无法消费
	 */
	public void consumer() {
		String [] topics=new String[]{"group2"};
		String bootstrapList="127.0.0.1:9092";
		ConsumerThreadPool pool =new ConsumerThreadPool("test1234",bootstrapList,topics,2);
		pool.consume();
	}



}
