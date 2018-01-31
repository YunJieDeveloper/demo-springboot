/**
 * Create By Hangzhou Moxie Data Technology Co. Ltd.
 */
package com.example.springboot.demo.kafka_demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * kafka消费处理基类
 *
 * @author zhanghesheng
 */
public class ConsumerCallable implements Callable<Boolean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerCallable.class);
    private String topic;
    private String value;
    private String key;

    public ConsumerCallable(String key, String value, String topic) {
        this.topic = topic;
        this.key = key;
        this.value = value;
    }

    /**
     * 后续,定义模型错误,针对错误码判断后,commit/seek
     *
     * @return
     * @throws Exception
     */
    @Override
    public Boolean call() throws Exception {

        List<Object> objectList = new ArrayList<>();
        try {
            LOGGER.info("topic:[{}],key:[{}],value:[{}]", topic, key,value);
            System.out.println("this kafka consumer" +"\t"+value+"\t"+"当前时间："+System.currentTimeMillis());
        } catch (Exception e) {
            LOGGER.error("topic:{},,key:[{}],value:[{}],数据失败,异常原因:", topic, key,value,e);
            return false;
        }
        return true;
    }
}
