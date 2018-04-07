package com.example.springboot.demo.kafka.produce;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Date 2017/12/13
 * @Description kafka 生产者类
 * @author zhanghesheng
 */
public class KafkaProduce {

    private static final Logger log = LoggerFactory.getLogger(KafkaProduce.class);

    /**
     * -------key---------------
     */
    private static final String BUFFER_MEMORY = "buffer.memory";
    private static final String KEY_SERIALIZER = "key.serializer";
    private static final String VALUE_SERIALIZER = "value.serializer";
    private static final String MAX_BLOCK_MS = "max.block.ms";
    private static final String ACKS = "acks";
    private static final String NUM_PARTITIONS = "num.partitions";
    private static final String RETRIES = "retries";
    private static final String ZOOKEEPER_CONNECT = "zookeeper.connect";
    private static final String BOOTSTRAP_SERVERS = "bootstrap.servers";

    /**
     * -------value-------------
     */
    private static final int DEFAULT_BUFFER_MEM_SIZE = 33554432;
    private static final String KEY_SERIALIZER_VALUE = "org.apache.kafka.common.serialization.StringSerializer";
    private static final String VALUE_SERIALIZER_VALUE = "org.apache.kafka.common.serialization.StringSerializer";
    private static final String MAX_BLOCK_MS_VALUE = "1000";
    private static final String ACKS_VALUE = "1";
    private static final Integer NUM_PARTITIONS_VALUE = 3;
    private static final Integer RETRIES_VALUE = 0;

    private final Producer<String, String> producer;

      /**基本属性装配 建议使用配置方式*/
    public static Properties defaultProperties() {
        Properties props = new Properties();
        props.put(KEY_SERIALIZER, KEY_SERIALIZER_VALUE);
        props.put(VALUE_SERIALIZER, VALUE_SERIALIZER_VALUE);
        props.put(MAX_BLOCK_MS, MAX_BLOCK_MS_VALUE);
        props.put(ACKS, ACKS_VALUE);
        props.put(NUM_PARTITIONS, NUM_PARTITIONS_VALUE);
        props.put(BUFFER_MEMORY, DEFAULT_BUFFER_MEM_SIZE);
        props.put(RETRIES, RETRIES_VALUE);
        //zookeeper
        props.put(ZOOKEEPER_CONNECT, "127.0.0.1:2181");

        return props;
    }

    public KafkaProduce(String brokerList) {
        Properties props = defaultProperties();
        props.put(BOOTSTRAP_SERVERS, brokerList);
        this.producer = new KafkaProducer(props);
    }

    public KafkaProduce(Properties props) {
        this.producer = new KafkaProducer(props);
    }

    /**
     * 消息发送方法
     */
    public void writeObject(String topic, Object value, String key) {
        int i = 10000;
        try {
            //此处使用循环做测试，具体用法按实际生产环境实现
            while (true) {
                i++;
                 /**
                  * 具体的发送消息操作
                  *  key 作为参数属性，无实际用处
                  * */
                ProducerRecord<String, String> data = new ProducerRecord(topic, key, value.toString() + i);
                Future<RecordMetadata> metadataFuture = this.producer.send(data, (metadata, exception) -> {
                    if (exception != null) {
                        if (metadata != null) {
                            log.error("kafka write exception.topic - {}[{}]", new Object[]{metadata.topic(), metadata.partition(), exception});
                        } else {
                            log.error("kafka write exception.topic", exception);
                        }
                    }
                    log.info("topic:[{}],key:[{}],value:[{}]", topic, key, value.toString());
                });

                // for test
                System.out.println("topic:[" + topic + "]" + "value:[" + value + "]" + "计数:" + i + "\t" + "当前时间" + System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(2L);
                if (i >= 10040) break;
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.producer.close();
        }
    }

    /**
     * 关闭Kafka生产者
     */
    public void stop() throws IOException {
        this.producer.close();
    }

}
