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
     * 须引入common工程中的RedisConfiguration配置类
     */
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private JedisCluster jedisCluster;

     /**
     * string 存储
     */
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

    /**
     * string 取值
     */
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


    /**
     * string 存储
     * @param key   键
     * @param value 值
     * @param nxxx  只能是"NX"或"XX"
     *              如果取NX，则只有当key不存在是才进行set，如果取XX，则只有当key已经存在时才进行set

     * @param expx  只能是"EX"或"PX"
     *              代表数据过期时间的单位，EX代表秒，PX代表毫秒。
     * @param time  有效期时间
     */
    public String setStringRedis(String key, String value,String nxxx,String expx,long time) {
        JedisCommands commands = JedisUtils.getJedisCommands(jedisCluster, jedisPool);
        String s = null;
        try {
            /**
             * 往redis中放入字符串值
             */
            s =  commands.set(key, value, nxxx, expx, time);
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

}
