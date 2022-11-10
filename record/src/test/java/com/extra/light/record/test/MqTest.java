package com.extra.light.record.test;

import com.extra.light.record.config.RabbitMqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MqTest {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void Producer_topics_springbootTest() {
        //使用rabbitTemplate发送消息
        String message = "send email message to user";
        rabbitTemplate.convertAndSend(RabbitMqConfig.TEST_QUEUE, "key", message);
    }
}
