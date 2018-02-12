/**
 * Create By Hangzhou Moxie Data Technology Co. Ltd.
 */
package com.example.springboot.demo.kafka.consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

/**
 *kafka 消费信息service类
 *     通过微服务启动入口DemoMicroService调用该service线程
 */
@Service
@Slf4j
public class KafkaConsumerService {

    @Autowired
    private KafkaConsumerExecutorFactory executorFactory;

    private ConsumerThreadPool consumerThreadPool;

    public void startConsuming() {
        /**实际生产环境中，须通过配置类从配置文件中获取 groupId、topic、numThreads、bootstrapList等属性值*/
        String groupId = "groupId";
        String[] topic = {"group2"};
        int numThreads = 3;
        String bootstrapList="127.0.0.1:9092";
        consumerThreadPool = new ConsumerThreadPool(executorFactory,bootstrapList, groupId, topic, numThreads);
        consumerThreadPool.consume();
    }

    @PreDestroy
    public void shutdown(){
        consumerThreadPool.shutdown();
    }
}
