package com.activemq;

import com.activemq.service.AMQQueueSenderService;
import com.activemq.service.AMQTopicSenderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.UUID;

/**
 * @Description: 测试ActiveMQ与spring整合配置的JmsTemplate
 * @Author：pengrj
 * @Date : 2019/5/4 0004 14:36
 * @version:1.0
 */

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:spring-ActiveMQ.xml"})
public class TestJmsTemplate {


    @Autowired
    private AMQQueueSenderService amqQueueSenderService;

    @Autowired
    private AMQTopicSenderService amqTopicSenderService;

    @Test
    public void testSendMessage() throws InterruptedException {

        QueueMessageBo queueMessageBoA=new QueueMessageBo(UUID.randomUUID().toString(),"测试发送queue消息A",new Date());

        amqQueueSenderService.sendMsgTo61616(queueMessageBoA);

        Thread.sleep(10000);

        QueueMessageBo queueMessageBoB=new QueueMessageBo(UUID.randomUUID().toString(),"测试发送queue消息B",new Date());

        amqQueueSenderService.sendMsgTo61616(queueMessageBoB);



    }

    @Test
    public void testPubTopicMessage(){

        QueueMessageBo queueMessageBo=new QueueMessageBo(UUID.randomUUID().toString(),"测试发布Topic消息",new Date());

        amqTopicSenderService.sendMsgto61616(queueMessageBo);

    }
}
