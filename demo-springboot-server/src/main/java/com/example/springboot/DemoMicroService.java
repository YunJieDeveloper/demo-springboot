package com.example.springboot;

import com.demo.springboot.configuration.DaoConfiguration;
import com.demo.springboot.configuration.Demo2Properties;
import com.demo.springboot.configuration.RedisConfiguration;
import com.example.springboot.demo.kafka.consumer.KafkaConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/***
 * @Date 2018/2/5
 * @Description  springBoot微服务启动入口
 * @author zhanghesheng
 * */

@SpringBootApplication //相当于@Configuration,@EnableAutoConfiguration,@ComponentScan
@Slf4j//lombok 启动日志注解
//@ComponentScan({"com.example"})//扫描引入的其他工程的包名。否则会出现注入失败异常,此注解也可用在配置类上
public class DemoMicroService {

	public static void main(String[] args) {
		log.info("===============server start=====================");

		//方式一
		//SpringApplication.run(DemoSpringbootServerApplication.class,args);

		//方式二
		SpringApplication springApplication = new SpringApplication(new Object[]{DemoMicroService.class});
		ApplicationContext act = springApplication.run(args);
        /**启动kafka消费线程*/
		KafkaConsumerService kafkaMicroService = (KafkaConsumerService) act.getBean("kafkaConsumerService");
		kafkaMicroService.startConsuming();

	}
}
