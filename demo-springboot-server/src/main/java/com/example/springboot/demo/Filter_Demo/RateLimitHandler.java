package com.example.springboot.demo.Filter_Demo;

import com.demo.utils.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * @Date 2018/1/24
 * @Description 限流请求处理类
 * @author zhanghesheng
 * */
@Component
@Slf4j
public class RateLimitHandler {

    @Autowired
    @Qualifier("jedisPool")
    private JedisPool jedisPool;

    @Autowired
    @Qualifier("jedisCluster")
    private JedisCluster jedisCluster;
    /**
     * 匹配请求Url的正则
     */
    private static final Pattern TASK_URI = Pattern.compile(".*/carrier/.*/tasks/(.*?)/.*");
    /**
     * 存入redis的初始key
     */
    private static final String REQUEST_REDIS_KEY = "";
    /**
     * 存入redis的有效期默认值，合理的操作应是实现可配置化(通过配置文件读取)
     */
    private static final int EXPIRE_KEY_TIME = 10;
    /**
     * 单位时间内请求数量最大限定值，合理的操作应是实现可配置化(通过配置文件读取)
     */
    private static final long PERIOD_MAX_COUNT = 1000L;

    public void recordRate(String uri) throws Exception {
        //记录时间
        long startTime = System.currentTimeMillis();
        String requestkey = null;
        /*匹配需要限流的url请求*/
        Matcher matcher = TASK_URI.matcher(uri);
        if (matcher.find()) {
            if (matcher.groupCount() == 1) {
                requestkey = matcher.group(1);
            }
        }
        long requestCount = upgradeKeyRateCount(REQUEST_REDIS_KEY, requestkey, EXPIRE_KEY_TIME);
        /**当单位时间内请求数量超过限定值时，抛出超限异常*/
        if (requestCount > PERIOD_MAX_COUNT){
            throw new Exception("Too many request, Please try again after a minute");
        }
    }


    /**
     * 请求数存入redis
     */
    public long upgradeKeyRateCount(String key, String value, int expireTime) {
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        JedisCommands commands = null;
        String rkey = String.format("%s:%s", key, value);
        long count = 0;
        try {
            commands = JedisUtils.getJedisCommands(jedisCluster, jedisPool);
            if (!commands.exists(rkey)) {
                count = commands.incr(rkey);
                commands.expire(rkey, expireTime);
            } else {
                count = commands.incr(rkey);
                if (commands.ttl(rkey) == -1) {
                    commands.expire(rkey, expireTime);
                }
            }
            log.info("action=upgradeKeyRateCount key:{},count={}", rkey, count);
            return count;
        } catch (JedisException e) {
            log.error("action=upgradeKeyRateCount add key:{} to redis failed:error:{}", rkey, e);
        } finally {
            JedisUtils.closeJedis(commands);
        }
        return 0;
    }

}
