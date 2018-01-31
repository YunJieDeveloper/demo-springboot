package com.example.springboot;

import com.demo.configuration.DaoConfiguration;
import com.demo.configuration.Demo2Properties;
import com.demo.configuration.RedisConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.constraints.NotNull;

@SpringBootApplication //相当于@Configuration,@EnableAutoConfiguration,@ComponentScan
@Slf4j//lombok 启动日志注解
@Import(value = {DaoConfiguration.class,Demo2Properties.class, RedisConfiguration.class}) //引入配置类,一定要引全
@EnableSwagger2//启动swagger注解
public class DemoSpringbootServerApplication {

	public static void main(String[] args) {
		log.info("===============server start=====================");

		//方式一
		//SpringApplication.run(DemoSpringbootServerApplication.class,args);

		//方式二
		SpringApplication springApplication = new SpringApplication(new Object[]{DemoSpringbootServerApplication.class});
		springApplication.run(args);

	}
}
