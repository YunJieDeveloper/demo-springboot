package com.example.springboot.demo.template;

import com.demo.springboot.utils.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;

@Component
@Slf4j
public class RedisDemo {

    /**
     * 一定要在微服务启动入口DemoMicroService中引入common工程中的RedisConfiguration配置类
     */
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private JedisCluster jedisCluster;

    //string 存储
    public String setStringRedis(String key, String value) {
        JedisCommands commands = JedisUtils.getJedisCommands(jedisCluster, jedisPool);
        String s = null;
        try {
            /**
             * 往redis中放入字符串值
             */
            s = commands.set(key, value);
            //设置超时时间，单位为秒
            commands.expire(key, 10);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /**
             * 一定要关闭redis资源
             */
            JedisUtils.closeJedis(commands);
        }
        return s;
    }


    //string 取值
    public String getStringRedis(String key) {
        JedisCommands commands = JedisUtils.getJedisCommands(jedisCluster, jedisPool);
        String s = null;
        try {
            s = commands.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtils.closeJedis(commands);
        }
        return s;
    }
}
