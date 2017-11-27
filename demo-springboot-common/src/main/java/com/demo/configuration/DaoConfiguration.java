package com.demo.configuration;


import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author zhanghesheng
 * @Description jdbc连接配置类.
 */

@Configuration  //配置类注解
@EnableTransactionManagement //spring事务
@ConditionalOnProperty("demo.mysql.url")
public class DaoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(DaoConfiguration.class);
    private Map<String, Object> datasourceMap;

    @Autowired
    //读取配置文件application.properties的类
    private Environment env;

    @Value("${demo.mysql.url:jdbc:mysql://127.0.0.1:3306/demo?zeroDateTimeBehavior=convertToNull&characterEncoding=UTF-8}")
    private String demoUrl;
    //从application.properties中获取demo.mysql.user的值，若没有，使用默认值test
    @Value("${demo.mysql.user:test}")
    private String demoUser;
    @Value("${demo.mysql.pass:mx}")
    private String demoPass;
    @Value("${demo.mysql.initial.size:20}")
    private String initialSize;
    @Value("${demo.mysql.max.active:50}")
    private String maxActive;

    /*
        //方式一：直接定义字段
        @Value("${demo2.mysql.url:jdbc:mysql://127.0.0.1:3306/demo?zeroDateTimeBehavior=convertToNull&characterEncoding=UTF-8}")
        private String demo2Url;
        //从application.properties中获取demo.mysql.user的值，若没有，使用默认值test2
        @Value("${demo2.mysql.user:test2}")
        private String demo2User;
        @Value("${demo2.mysql.pass:mx2}")
        private String demo2Pass;
        */

    //方式二：单独定义一个属性类
    @Autowired
    Demo2Properties demo2Properties;

    @PostConstruct
    public void init() {
        datasourceMap = new HashMap<String, Object>();
        datasourceMap.put("driverClassName", "com.mysql.jdbc.Driver");
        datasourceMap.put("initialSize", initialSize);
        datasourceMap.put("maxActive", maxActive);
        datasourceMap.put("minIdle", "1");
        datasourceMap.put("maxWait", "20000");
        datasourceMap.put("removeAbandoned", "true");
        datasourceMap.put("removeAbandonedTimeout", "180");
        datasourceMap.put("timeBetweenEvictionRunsMillis", "60000");
        datasourceMap.put("minEvictableIdleTimeMillis", "300000");
        datasourceMap.put("validationQuery", "SELECT 1");
        datasourceMap.put("testWhileIdle", "true");
        datasourceMap.put("testOnBorrow", "false");
        datasourceMap.put("testOnReturn", "false");
        datasourceMap.put("poolPreparedStatements", "true");
        datasourceMap.put("maxPoolPreparedStatementPerConnectionSize", "50");
        datasourceMap.put("initConnectionSqls", "SELECT 1");

        for (Iterator<PropertySource<?>> it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
            PropertySource<?> propertySource = it.next();
            this.getPropertiesFromSource(propertySource, datasourceMap);
        }

        //从application.properties中获取demo.mysql.pass的值，若没有，使用默认值"mx"
        //env.getProperty("demo.mysql.pass",String.class,"mx");
    }

    @Bean(name = "demoDataSource")
    @Primary //多数据源时使用 否则会报错：required a single bean, but 2 were found
     @Qualifier("demoDataSource")
    public DataSource dataSourcedemo() {
        LOGGER.info("初始化数据源");
        return this.getDataSource(demoUrl, demoUser, demoPass);
    }

    @Bean(name = "demoTemplate")
    public JdbcTemplate templatedemo() {
        return new JdbcTemplate(this.dataSourcedemo());
    }


    @Bean(name = "demo2DataSource")
   //@Qualifier("demo2DataSource") //连接另一个数据库
    public DataSource codeDataSource() {
        //return this.getDataSource(demo2Url, demo2User, demo2Pass);
        return  getDataSource(demo2Properties.getDemo2Url(),demo2Properties.getDemo2User(),demo2Properties.getDemo2Pass());
    }

    @Bean(name = "demo2JdbcTemplate")
    public JdbcTemplate codeJdbcTemplate() {
        LOGGER.info("初始化报告数据源codeJdbcTemplate");
        return new JdbcTemplate(codeDataSource());
    }


    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSourcedemo());
    }

    private DataSource getDataSource(String url, String user, String pass) {
        datasourceMap.put(DruidDataSourceFactory.PROP_URL, url);
        datasourceMap.put(DruidDataSourceFactory.PROP_USERNAME, user);
        datasourceMap.put(DruidDataSourceFactory.PROP_PASSWORD, pass);

        try {
            return DruidDataSourceFactory.createDataSource(datasourceMap);
        } catch (Exception e) {
            LOGGER.error("无法获得数据源[{}]:[{}]", url, ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("无法获得数据源.");
        }
    }

    private void getPropertiesFromSource(PropertySource<?> propertySource, Map<String, Object> map) {
        if (propertySource instanceof MapPropertySource) {
            for (String key : ((MapPropertySource) propertySource).getPropertyNames()) {
                map.put(key, propertySource.getProperty(key));
            }
        }
        if (propertySource instanceof CompositePropertySource) {
            for (PropertySource<?> s : ((CompositePropertySource) propertySource).getPropertySources()) {
                getPropertiesFromSource(s, map);
            }
        }
    }
}