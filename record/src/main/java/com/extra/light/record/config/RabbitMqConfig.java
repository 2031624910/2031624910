package com.extra.light.record.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 林树毅
 */
@Configuration
public class RabbitMqConfig {
    public final static String EXCHANGE_TOPICS_INFORM = "EXCHANGE_TOPICS_INFORM";
    public final static String TEST_QUEUE = "testQueue";
    /**
     * 目前这个key构成情况问题未知
     */
    public final static String ROUTING_TEST_KEY = "key.#";

    /**
     * 创建交换机
     *
     * @return
     */
    //@Bean
    public Exchange exchangeTopicsInform() {
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    /**
     * 创建测试队列
     *
     * @return
     */
    //@Bean
    public Queue testQueue() {
        return new Queue(TEST_QUEUE);
    }

    /**
     * 绑定队列
     *
     * @param exchangeTopicsInform
     * @param testQueue
     * @return
     */
    //@Bean
    public Binding testBinding(Exchange exchangeTopicsInform, Queue testQueue) {
        return BindingBuilder.bind(testQueue).to(exchangeTopicsInform).with(ROUTING_TEST_KEY).noargs();
    }

    //@RabbitListener(queues = {TEST_QUEUE})
    public void receiveTest(Object msg, Message message, Channel channel){
        System.out.println("QUEUE_INFORM_SMS msg"+msg);
        System.out.println("QUEUE_INFORM_SMS msg"+message);
        System.out.println("QUEUE_INFORM_SMS msg"+channel);
    }

}
