package com.example.springboot.demo.consul;


import com.example.springboot.demo.config.ConsulConfigProperties;
import com.orbitz.consul.*;
import com.orbitz.consul.model.health.ServiceHealth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

/***
 * @Date 2018/4/22
 * @Description consul service 案例   com.orbitz.consul
 * @author zhanghesheng
 *
 *
 *
 * swagger：http://localhost:8080/swagger-ui.html
   consul UI：http://192.168.21.110:8500/ui/
 * */

@Service
public class ConsulService {

    @Autowired
    static  ConsulConfigProperties properties;
    //建立consul实例
    @Autowired
    static Consul consul ;

    /**
     * 注册服务
     * 并对服务进行健康检查
     * servicename唯一
     * serviceId:没发现有什么作用
     */
    public void registerService(String serviceName, String serviceId) {
        AgentClient agentClient = consul.agentClient();        //建立AgentClient

        try {
            /**
             * 注意该注册接口：
             * 需要提供一个健康检查的服务URL，以及每隔多长时间访问一下该服务（这里是3s）
             */
            agentClient.register(properties.getServicePort(), URI.create(properties.getHttpUrl()).toURL(), 3, serviceName, serviceId, "dev");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //        try {
        //            agentClient.pass(serviceId);//健康检查
        //        } catch (NotRegisteredException e) {
        //            e.printStackTrace();
        //        }
    }

    /**
     * 发现可用的服务
     */
    public List<ServiceHealth> findHealthyService(String servicename) {
        Consul consul = Consul.builder().build();

        HealthClient healthClient = consul.healthClient();//获取所有健康的服务
        return healthClient.getHealthyServiceInstances(servicename).getResponse();//寻找passing状态的节点
    }

    /**
     * 存储KV
     */
    public void storeKV(String key, String value) {
        KeyValueClient kvClient = consul.keyValueClient();
        kvClient.putValue(key, value);//存储KV
    }

    /**
     * 根据key获取value
     */
    public String getKV(String key) {
        KeyValueClient kvClient = consul.keyValueClient();
        return kvClient.getValueAsString(key).get();
    }

    /**
     * 找出一致性的节点（应该是同一个DC中的所有server节点）
     */
    public List<String> findRaftPeers() {
        StatusClient statusClient = consul.statusClient();
        return statusClient.getPeers();
    }

    /**
     * 获取leader
     */
    public String findRaftLeader() {
        StatusClient statusClient = consul.statusClient();
        return statusClient.getLeader();
    }

}
