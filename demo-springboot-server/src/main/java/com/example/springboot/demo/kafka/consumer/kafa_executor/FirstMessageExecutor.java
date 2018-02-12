package com.example.springboot.demo.kafka.consumer.kafa_executor;

import com.example.springboot.demo.kafka.consumer.kafa_dto.FirstBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component//加入扫描
@Slf4j
public class FirstMessageExecutor implements MessageExecutor{
    @Override
    public Object getObjectClass() {
        return new FirstBean();
    }

    @Override
    public String getTopic() {
        return "first-topic";
    }

    @Override
    public Object execute(Object message) {
        log.debug("开始消费first-topic信息:[{}]", message);
        List<FirstBean> messageData = (List<FirstBean>) message;
        try {
            /**此处近视具体的service -> dao等一系列操作*/
            // todo firstService.add(messageData);
            return message;
        } catch (Exception e) {
            log.error("数据存储异常,请求报文:{},异常原因", messageData, e);
            return null;
        }
    }
}
