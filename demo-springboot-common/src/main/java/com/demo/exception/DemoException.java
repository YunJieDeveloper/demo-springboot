package com.demo.exception;

/***
 * @Date 2017/11/26
 *@Description 自定义异常类
 * @author zhanghesheng
 * */
public class DemoException extends Exception {
    public DemoException(String message){
        super(message);
    }

    public DemoException(String message,Throwable e){
        super(message,e);
    }

    public DemoException(Throwable e){
        super(e);
    }
}
