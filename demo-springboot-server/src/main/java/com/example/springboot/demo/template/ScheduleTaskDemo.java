package com.example.springboot.demo.template;

import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/***
 * @Date 2017/11/29
 *@Description 定时任务案例
 * @author zhanghesheng
 * */
@Component  //开启扫描
//暂时注释掉，使用时打开
//@EnableScheduling //开启对定时任务的支持
public class ScheduleTaskDemo {

    @Scheduled(fixedRate = 5000) //通过@Scheduled声明该方法是计划任务，使用fixedRate属性每隔固定时间执行
    public  void reportCurrentTime(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("每隔5秒执行一次 "+new DateTime(new Date()).toString("yyyy-MM-dd HH:mm:ss"));
    }

    @Scheduled(cron = "0 07 20 ? * *" ) //使用cron属性可按照指定时间执行，本例指的是每天20点07分执行；
    //cron是UNIX和类UNIX(Linux)系统下的定时任务
    public void fixTimeExecution(){
        System.out.println("在指定时间 "+new DateTime(new Date()).toString("yyyy-MM-dd HH:mm:ss")+" 执行");
    }

    @Scheduled(fixedDelay = 5000 ) //该定时任务结束后每隔固定时间执行
    public void fixDelayExecution(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("在指定时间 "+new DateTime(new Date()).toString("yyyy-MM-dd HH:mm:ss")+" 执行");
    }

}
