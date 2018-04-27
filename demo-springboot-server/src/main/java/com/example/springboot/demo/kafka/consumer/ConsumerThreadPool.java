/**
 * Create By Hangzhou Moxie Data Technology Co. Ltd.
 */
package com.example.springboot.demo.kafka.consumer;

import com.example.springboot.demo.kafka.consumer.kafa_executor.MessageExecutor;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author zhanghesheng
 * @Description 消费者处理线程
 */
public class ConsumerThreadPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerThreadPool.class);


    private final String[] topic;
    private final KafkaConsumer<String, String> consumer;
    private ExecutorService threadPool;
    private KafkaConsumerExecutorFactory executorFactory;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    private Properties createProperties(String groupId, String bootstrapList) {
        Properties props = new Properties();

        //brokerServer(kafka)ip地址,不需要把所有集群中的地址都写上，可是一个或一部分
        props.put("bootstrap.servers", bootstrapList);

        //设置consumer group name,必须设置 值随意
        props.put("group.id", groupId);

        //enable.auto.commit  true：自动提交偏移量；false：手动提交偏移量
        props.put("enable.auto.commit", "true");

        //偏移量(offset)提交频率
        props.put("auto.commit.interval.ms", "1000");

        props.put("session.timeout.ms", "30000");

        //设置key以及value的解析（反序列）类
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        /*设置使用最开始的offset偏移量为该group.id的最早。如果不设置，则会是latest即该topic最新一个消息的offset
        如果采用latest，消费者只能得道其启动后，生产者生产的消息 */
        //props.put("auto.offset.reset", "earliest");
        return props;
    }

    /**
     * 获取KafkaConsumer对象
     */
    public KafkaConsumer<String, String> getConsumer(String groupId, String bootstrapList) {
        Properties properties = createProperties(groupId, bootstrapList);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        return consumer;
    }

    public ConsumerThreadPool(final KafkaConsumerExecutorFactory executorFactory,  final String bootstrapList,
                              final String groupId, final String[] topic, final int numThreads) {
        this.executorFactory = executorFactory;
        this.consumer = getConsumer(groupId, bootstrapList);
        this.threadPool = Executors.newFixedThreadPool(numThreads);
        this.topic = topic;
    }

    /**
     * 消息处理过程失败,offset提交失败,修正后继续执行
     */
    public void consume() {

        try {
            //订阅主题
            consumer.subscribe(Lists.newArrayList(topic));
            boolean isRunning = true;
            while (isRunning) {
                if (closed.get()) {
                    consumer.close();
                }
                //每次取100条信息
                ConsumerRecords<String, String> records = consumer.poll(1000*20);

                Map<String, List<String>> messageDataMap = new HashMap<>();
                for (ConsumerRecord<String, String> record : records) {
                    LOGGER.error("record:[{}]",record);
                     /**将消费信息按topic分组，放入messageDataMap中*/
                    if (!messageDataMap.containsKey(record.topic())) {
                        List<String> messageDataList = new ArrayList<>();
                        messageDataList.add(record.value());
                        messageDataMap.put(record.topic(), messageDataList);
                    } else {
                        messageDataMap.get(record.topic()).add(record.value());
                    }
                }
                for (Map.Entry<String, List<String>> messageDateEntry : messageDataMap.entrySet()) {
                    String topic = messageDateEntry.getKey();
                    List<String> value = messageDateEntry.getValue();
                    MessageExecutor messageExecutor = executorFactory.getMessageExecutor(topic);
                    if (null == messageExecutor) {
                        throw new Exception("topic[" + topic + "]未定义实现类");
                    }
                    Callable callable = new ConsumerCallable(messageExecutor, value, topic);
                    threadPool.submit(callable);
                }
            }
        } catch (Exception e) {
            // 当Kafka队列无消息时,休眠10秒钟
            try {
                LOGGER.info("kafka receive message is null, thread sleep 10s.");
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException ie) {
                LOGGER.error("Interrupted sleep");
            }
        }
    }


    public void shutdown() {
        LOGGER.info("Kafka链接和消息处理消息线程池关闭前销毁");
        if (consumer != null) consumer.close();
        if (threadPool != null) threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                LOGGER.debug("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted during shutdown, exiting uncleanly");
        }
    }
}
