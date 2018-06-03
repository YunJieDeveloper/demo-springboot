package com.demo.springboot.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 *
 **/

@Slf4j
public class SpringContextUtils {
    private static ApplicationContext applicationContext;

    public static <T> T  getBean(Class tClass){
        return (T)applicationContext.getBean(tClass);
    }

    public static <T> T  getBean(String className){
        T bean = null;
        try{
            bean = (T)applicationContext.getBean(className);
        }catch (Exception e){
            log.error(" get bean error {},errorMsg:{}",className,e.getMessage(),e);
        }
        return bean;
    }

    public static Map<String,Object> getBeansWithAnnotation(Class tClass){
        return applicationContext.getBeansWithAnnotation(tClass);
    }

    public static void setApplicationContext(ApplicationContext context){
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * 获取所有带某注解类的方法
     * @param annotationClass 注解类
     * @return
     */
    public static List<Method> getClassMethodByAnnotation(Class annotationClass){
        List<Method> result = Lists.newArrayList();
        Map<String,Object> beansMap = getBeansWithAnnotation(annotationClass);
        if(beansMap!=null && beansMap.size()>0){
            for(Object value:beansMap.values()){
                Class valueClass = value.getClass();
                Method[] methodArray = valueClass.getDeclaredMethods();
                if(methodArray!=null && methodArray.length>0){
                    for(Method method:methodArray){
                        Object annotation = method.getAnnotation(annotationClass);
                        if(annotation!=null){
                            result.add(method);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static void registrationBean(Class targetClass){
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(targetClass);
        defaultListableBeanFactory.registerBeanDefinition(getBeanRegistrationName(targetClass.getSimpleName()), beanDefinitionBuilder.getBeanDefinition());
    }

    public static String getBeanRegistrationName(String className){
        if(StringUtils.isBlank(className)){
            return className;
        }
        String firstChar = className.substring(0,1);
        return new StringBuffer().append(firstChar.toLowerCase()).append(className.substring(1,className.length())).toString();
    }
}
