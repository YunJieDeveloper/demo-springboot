/**
 * Create By Hangzhou Moxie Data Technology Co. Ltd.
 */
package com.example.springboot.demo.kafka.consumer;

import com.example.springboot.demo.kafka.consumer.kafa_executor.MessageExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * kafka消费处理的线程基类
 *
 * @author zhanghesheng
 */
public class ConsumerCallable implements Callable<Boolean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerCallable.class);
    private String topic;
    private MessageExecutor messageExecutor;
    private List<String> messageDataList;

    public ConsumerCallable(MessageExecutor messageExecutor,List<String> messageDataList, String topic) {
        this.topic = topic;
        this.messageExecutor = messageExecutor;
        this.messageDataList = messageDataList;
    }

    /**
     *
     * 对信息消费的具体操作
     *
     * 后续,定义模型错误,针对错误码判断后,commit/seek
     *
     * @return
     * @throws Exception
     */
    @Override
    public Boolean call() throws Exception {

            List<Object> objectList = new ArrayList<>();
            try {
                Object objectData = messageExecutor.getObjectClass();
                for (String messageData : messageDataList) {
                    //将json串转换为Object
                    objectData = new ObjectMapper().readValue(messageData, objectData.getClass());
                    //Map<String,Object> map  = objectMapper.readValue(messageData,new TypeReference<HashMap<String,Object>>(){});
                    //objectData = BeanToMapUtil.convertMap(objectData.getClass(), map);
                    objectList.add(objectData);
                }
            } catch (Exception e) {
                LOGGER.error("topic:{},报文信息转换错误,异常原因:", topic, e);
                return true;
            }
            try {
                messageExecutor.execute(objectList);
            } catch (Exception e) {
                LOGGER.error("topic:{},数据存储失败,异常原因:", topic, e);
                return false;
            }
            return true;
        }
}
