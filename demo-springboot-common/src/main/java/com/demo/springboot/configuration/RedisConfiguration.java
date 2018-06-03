package com.demo.springboot.configuration;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.*;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/***
 * @Date 2017/12/5
 *@Description  redis配置类，其中字段属性也可以单独提取出去当作一个属性类RedisProperties
 * @author zhanghesheng
 * */
@Configuration
@ConfigurationProperties(prefix = "demo.redis")

//制定配置文件名，默认application.properties／application.yml／bootstrap.properties／bootstrap.yml
//主要用作配置自定义文件
@PropertySource("classpath:application.properties")
@Data
public class RedisConfiguration {

    @NotNull
    @Value("10")
    private int maxIdle;

    @NotNull
    @Value("10")
    private int maxTotal;

    @NotNull
    @Value("1000")
    private long maxWaitMillis;

    @NotNull
    @Value("5")
    private int minIdle;

    private String password;

    @NotNull
    private List<String> addressList;

    @Value("0")
    private int dbIndex;

    @Bean( name = "jedisPool",
            destroyMethod = "close")
    @Primary
    public JedisPool getJedis() {

        if (addressList.size() > 1) {
            return null;
        }
        String hostport = addressList.get(0);
        String[] hp = hostport.split(":");

        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);

        if (StringUtils.isBlank(password)) {
            return new JedisPool(config, hp[0], Integer.parseInt(hp[1]), Protocol.DEFAULT_TIMEOUT, null, dbIndex);
        } else {
            return new JedisPool(config, hp[0], Integer.parseInt(hp[1]), Protocol.DEFAULT_TIMEOUT, password, dbIndex);
        }
    }

    @Bean(name = "jedisCluster",
            destroyMethod = "close")
    @Primary
    public JedisCluster getJedisCluster() {
        if (addressList.size() == 1) {
            return null;
        }
        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        for (String hostAndPort : addressList) {
            String[] hp = hostAndPort.split(":");

            jedisClusterNodes.add(new HostAndPort(hp[0], Integer.parseInt(hp[1])));
        }
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setMinIdle(minIdle);
        config.setTestWhileIdle(true);
        JedisCluster jc = new JedisCluster(jedisClusterNodes, Protocol.DEFAULT_TIMEOUT, config);
        return jc;
    }
}
