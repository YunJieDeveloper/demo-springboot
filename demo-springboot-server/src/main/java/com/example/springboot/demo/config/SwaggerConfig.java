package com.example.springboot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/***
 * @Date 2018/4/27
 * @Description   swagger配置类
 *                todo : 不能加@EnableWebMvc 不能继承WebMvcConfigurerAdapter
 *                todo :  因此需从GateWayConfiguration中分离出来，通过import方式整合
 * @author zhanghesheng
 * */

@Configuration
//开启swagger注解
@EnableSwagger2

public class SwaggerConfig {

    @Bean
    public Docket config() {
        return new Docket(DocumentationType.SWAGGER_2)
                //非必需
                .apiInfo(apiInfo())
                .select()
                //basePackage是去掉默认带的测试的Basic Error Controller
                .apis(RequestHandlerSelectors.basePackage("com.example.springboot.controller"))
                .build();

    }
    //非必需
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 任意，请稍微规范点
                .title("接口列表 v1.1.0")
                // 任意，请稍微规范点
                .description("接口测试")
                .version("1.1.0")
                .build();
    }

}
