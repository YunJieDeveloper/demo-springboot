package com.example.springboot.demo.config;

import com.demo.springboot.exception.DemoException;
import com.ecwid.consul.v1.ConsistencyMode;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/***
 * @Date 2018/4/22
 * @Description  ecwid.consul
 * @author zhanghesheng
 * */

@Slf4j
public class ConsulReader {

    private String CONSUL_TOKEN = "consul.agent.token";
    private ConsulClient consul;

    private ConsulConfigProperties properties;

    private String keyPrefix;

    protected ConcurrentHashMap<String, String> configMap = new ConcurrentHashMap<>();

    protected ConcurrentHashMap<String, Long> modifyMap = new ConcurrentHashMap<>();

    private EventBus eventBus;

    private class DeadEventHandler {
        @Subscribe
        public void handleDeadEvent(DeadEvent event) {
        }
    }

    public void addEventHandler(Object subscriber) {
        if (subscriber != null && eventBus != null) {
            eventBus.register(subscriber);
        }
    }

    public ConsulReader(ConsulClient consul, ConsulConfigProperties prop) {
        this(consul, prop, String.format("service/%s/%s/", prop.getName(), prop.getServiceTag()));
    }

    public ConsulReader(ConsulClient consul, ConsulConfigProperties properties, String keyPrefix) {
        this.consul = consul;
        this.properties = properties;
        this.keyPrefix = keyPrefix;
        eventBus = new AsyncEventBus("kvChangeEventBus", Executors.newSingleThreadExecutor());
        eventBus.register(new DeadEventHandler());
    }

    @Scheduled(fixedRate = 5000)
    public void pollConfiguration() {
        Set<String> unusedKeys = new HashSet<>();
        for (String key : configMap.keySet()) {
            try {
                readFromConsul(key);
            } catch (Exception e) {
                unusedKeys.add(key);
            }
        }

        for (String k : unusedKeys) {
            configMap.remove(k);
            modifyMap.remove(k);
        }
    }


    public String readConfig(String keyName) throws DemoException {
        String key = keyPrefix + keyName;
        String value = configMap.get(key);
        if (value == null) {
            value = readFromConsul(key);
        }
        return value;
    }

    private String readFromConsul(String keyName) throws DemoException {
        String token = System.getProperty(CONSUL_TOKEN);
        Response<GetValue> response = consul.getKVValue(keyName, token, new QueryParams(ConsistencyMode.DEFAULT));
        if (response.getValue() != null) {
            GetValue value = response.getValue();
            Long modifyIndex = modifyMap.get(keyName);
            String content;
            if (modifyIndex != null && value.getModifyIndex() == modifyIndex) {
                content = configMap.get(keyName);
            } else {
                if (modifyIndex != null) {
                    log.info("config file {} on consul is modified, will reload it", keyName);
                }
                content = value.getDecodedValue();
                configMap.put(keyName, content);
                modifyMap.put(keyName, value.getModifyIndex());
                eventBus.post(keyName);
            }
            return content;
        } else {
            throw new DemoException("cannot find key " + keyName);
        }
    }

}
