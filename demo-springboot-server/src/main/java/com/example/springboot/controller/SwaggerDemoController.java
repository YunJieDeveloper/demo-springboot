package com.example.springboot.controller;

import com.demo.springboot.exception.DemoException;
import com.example.springboot.service.DemoService;
import io.swagger.annotations.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/***
 * @Date 2017/12/6
 *@Description swagger－ui案例  访问方式：http://127.0.0.1:8182/swagger-ui.html
 * @author zhanghesheng
 * */

/*
说明：
@Api：用在类上，说明该类的作用
@ApiOperation：用在方法上，说明方法的作用
@ApiImplicitParams：用在方法上包含一组参数说明
@ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面
        paramType：参数放在哪个地方
        header-->请求参数的获取：@RequestHeader
query-->请求参数的获取：@RequestParam
path（用于restful接口）-->请求参数的获取：@PathVariable
body（不常用）
        form（不常用）
        name：参数名
        dataType：参数类型
        required：参数是否必须传
        value：参数的意思
        defaultValue：参数的默认值
@ApiResponses：用于表示一组响应
@ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
        code：数字，例如400
        message：信息，例如"请求参数没填好"
        response：抛出异常的类
@ApiModel：描述一个Model的信息（这种一般用在post创建的时候，使用@RequestBody这样的场景，请求参数无法使用@ApiImplicitParam注解进行描述的时候）
@ApiModelProperty：描述一个model的属性
*/

@RestController
@RequestMapping("/swagger")
@Api("swaggerDemoController相关api")
public class SwaggerDemoController {
    private static final String TOKEN = "afddeeg12daaf32";
    private static final String USER_NAME = "zhansan";

    private static final String PASSWORD = "123456";

    @Autowired
    DemoService demoService;


    @ApiOperation("获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true, value = "令牌", defaultValue = "afddeeg12daaf32"),
            @ApiImplicitParam(paramType = "path", name = "username", dataType = "String", required = true, value = "用户的姓名"),
            @ApiImplicitParam(paramType = "query", name = "password", dataType = "Long", required = true, value = "用户的密码")
    })
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没填好", response = DemoException.class),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对", response = DemoException.class)
    })
    //@GetMapping("/getbean/{username}")//高版本新增注解
    @RequestMapping("/getbean/{username}")
    public String getBean1(@RequestHeader("token") String token, @PathVariable("username") String username, @RequestParam("password") Long password) {
        if (TOKEN.equalsIgnoreCase(token)
                && USER_NAME.equalsIgnoreCase(username)
                &&PASSWORD.equalsIgnoreCase(password.toString())) {
            return "身份信息校验成功";
        }
        return "身份信息校验失败";
    }

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "token", dataType = "String", required = true,
                    value = "令牌", defaultValue = "afddeeg12daaf32")})
    @ApiOperation(value = "身份校验")
    @RequestMapping(value = "/getbean", method = RequestMethod.POST)
    @ApiResponses(value = {@ApiResponse(code = 401, message = "请求未通过认证.", response = DemoException.class)})
    public String getBean1(@RequestHeader("token") String token,@RequestBody Bean bean) {
        if (TOKEN.equalsIgnoreCase(token)
                && USER_NAME.equalsIgnoreCase(bean.getUserName())
                &&PASSWORD.equalsIgnoreCase(bean.getPassword().toString())) {
            return "身份信息校验成功";
        }
        return "身份信息校验失败";
    }

}

@Data
class Bean {
    String userName;
    Long password;
}
