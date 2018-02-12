package com.example.springboot.demo.kafka.consumer.kafa_executor;

import com.example.springboot.demo.kafka.consumer.kafa_dto.SecondBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component//加入扫描
@Slf4j
public class SecondMessageExecutor implements MessageExecutor{
    @Override
    public Object getObjectClass() {
        return new SecondBean();
    }

    @Override
    public String getTopic() {
        return "second-topic";
    }

    @Override
    public Object execute(Object message) {
        log.debug("开始消费second-topic信息:[{}]", message);
        List<SecondBean> messageData = (List<SecondBean>) message;
        try {
            /**此处进行具体的service -> dao等一系列操作*/
            // todo secondService.add(messageData);
            return message;
        } catch (Exception e) {
            log.error("数据存储异常,请求报文:{},异常原因", messageData, e);
            return null;
        }
    }
}
