package com.example.springboot.demospringbootserver;

import com.example.springboot.demo.template.ScheduleTaskDemo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class ScheduleTaskTest  extends BaseSpringTest{
    @Autowired
    ScheduleTaskDemo scheduleTaskDemo;

    @Test
    public  void testSchedule(){
        scheduleTaskDemo.fixDelayExecution();
        try {
            TimeUnit.SECONDS.sleep(20l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
