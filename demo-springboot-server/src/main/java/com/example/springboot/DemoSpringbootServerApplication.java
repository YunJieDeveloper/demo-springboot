package com.example.springboot;

import com.demo.configuration.DaoConfiguration;
import com.demo.configuration.Demo2Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication //相当于@Configuration,@EnableAutoConfiguration,@ComponentScan
@Slf4j
@Import(value = {DaoConfiguration.class,Demo2Properties.class}) //引入配置类

public class DemoSpringbootServerApplication {

	public static void main(String[] args) {
		log.info("===============server start=====================");
		SpringApplication.run(DemoSpringbootServerApplication.class, args);

	}
}
