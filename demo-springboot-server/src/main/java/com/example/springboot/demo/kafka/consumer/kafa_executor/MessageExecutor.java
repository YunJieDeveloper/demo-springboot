/**
 * Create By Hangzhou Moxie Data Technology Co. Ltd.
 */
package com.example.springboot.demo.kafka.consumer.kafa_executor;

/**
 *@Description kafka 具体的消息处理父接口
 */
public interface MessageExecutor<T> {

    T getObjectClass();

    String getTopic();

    T execute(T message);
}
