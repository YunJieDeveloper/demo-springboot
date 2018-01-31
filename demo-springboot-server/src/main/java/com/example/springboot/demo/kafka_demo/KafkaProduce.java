package com.example.springboot.demo.kafka_demo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/***
 * @Date 2017/12/13
 *@Description kafka 生产者类
 * @author zhanghesheng
 * */
public class KafkaProduce {
    private static final Logger log = LoggerFactory.getLogger(KafkaProduce.class);
    private static final int DEFAULT_BUFFER_MEM_SIZE = 33554432;
    private final Producer<String, String> producer;

    public static Properties defaultProperties() {
        Properties props = new Properties();
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("max.block.ms", "1000");
        props.put("acks", "1");
        props.put("num.partitions", Integer.valueOf(3));
        props.put("buffer.memory", DEFAULT_BUFFER_MEM_SIZE);
        props.put("retries", Integer.valueOf(0));
        //zookeeper
        props.put("zookeeper.connect", "127.0.0.1:2181");

        return props;
    }

    public KafkaProduce(String brokerList) {
        Properties props = defaultProperties();
        props.put("bootstrap.servers", brokerList);
        this.producer = new KafkaProducer(props);
    }

    public KafkaProduce(Properties prop) {
        this.producer = new KafkaProducer(prop);
    }

    //消息发送方法
    public void writeObject(String topic, Object value, String key) {
        int i=10000;
        try {
            while (true) {
                i++;
                ProducerRecord<String, String> data = new ProducerRecord(topic, key, value.toString()+i);
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
                System.out.println("topic:[" + topic + "]" + "value:[" + value + "]"+"计数:"+i+"\t"+"当前时间"+ System.currentTimeMillis());
                    TimeUnit.SECONDS.sleep(2L);

                    if(i>= 10040) break;
            }



        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.producer.close();
        }
    }

    //关闭Kafka生产者
    public void stop() throws IOException {
        this.producer.close();
    }

}
