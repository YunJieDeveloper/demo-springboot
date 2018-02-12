/**
 * Create By Hangzhou Moxie Data Technology Co. Ltd.
 */
package com.example.springboot.demo.kafka.consumer;

import com.example.springboot.demo.kafka.consumer.kafa_executor.FirstMessageExecutor;
import com.example.springboot.demo.kafka.consumer.kafa_executor.MessageExecutor;
import com.example.springboot.demo.kafka.consumer.kafa_executor.SecondMessageExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 *@Description kafka 消费信息工厂类
 *             所有具体的消费处理类都在此类初始化
 */
@Service
public class KafkaConsumerExecutorFactory {

    @Autowired
    private FirstMessageExecutor firstMessageExecutor;

    @Autowired
    private SecondMessageExecutor secondMessageExecutor;

    /** 服务容器 */
    private Map<String, MessageExecutor> serverRoute = new HashMap<>();

    /**
     * 初始化处理器容器
     */
    @PostConstruct
    public void init() {
        serverRoute.put(firstMessageExecutor.getTopic(), firstMessageExecutor);
        serverRoute.put(secondMessageExecutor.getTopic(), secondMessageExecutor);
        //todo 新的信息处理类在此处初始化
    }

    /**
     * 根据路由类型获取服务
     *
     * @param topic
     * @return
     */
    public MessageExecutor getMessageExecutor(String topic) {
        return serverRoute.get(topic);
    }

}
