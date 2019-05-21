package com.activemq.consumerAdvancedFeatures;

import com.activemq.MessageAdvancedFeatures.BlobMessageProducer;
import com.activemq.MessageDispatchAdvancedFeatures.ProducerThread;
import com.activemq.QueueMessageBo;
import com.activemq.service.AMQQueueSenderService;
import com.activemq.service.AMQTopicSenderService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 测试ActiveMQ consumer高级特性
 * @Author：pengrj
 * @Date : 2019/5/4 0004 14:36
 * @version:1.0
 */

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:consumer-Advanced-features.xml"})
public class TestConsumerAdvancedFeatures {


    @Autowired
    private AMQQueueSenderService amqQueueSenderService;

    @Resource(name="exclusiveQueueDestination")
    private ActiveMQQueue exclusiveQueueDestination;


    @Resource(name="groupQueueDestination")
    private ActiveMQQueue groupQueueDestination;






    /**
     * 测试向197 broker1 61616 queueu队列发送消息 这个队列设置了独享消费者
     * @throws InterruptedException
     */
    @Test
    public void testSendMessageExclusiveQueue() throws InterruptedException {


        for(int i=1;i<=10;i++){

            QueueMessageBo queueMessageBo=new QueueMessageBo(Integer.toString(i),"测试独享消费者 消息："+i,new Date());

            //使用了两个消息的监听器对消息进行消费 在Activemq中虽然注册了两个消费者 但只有一个消费者可以进行消息消费处理
            amqQueueSenderService.sendMsgTo61616(queueMessageBo,exclusiveQueueDestination);
        }

        Thread.sleep(10000);

        System.out.println("信息发送接收完毕");
    }

    /**
     * 测试向197 broker1 61616 queueu队列发送分组消息
     * @throws InterruptedException
     */
    @Test
    public void testSendGroupMessageQueue() throws InterruptedException {


        for(int i=1;i<=10;i++){

            QueueMessageBo queueMessageBoA=new QueueMessageBo(Integer.toString(i),"组A 测试分组消息 消息："+i,new Date());

            QueueMessageBo queueMessageBoB=new QueueMessageBo(Integer.toString(i),"组B 测试分组消息 消息："+i,new Date());

            //使用了三个消息的监听器对消息进行消费 在Activemq中虽然注册了三个个消费者 但只两个消费者对两个组里面的消息消费处理
            amqQueueSenderService.sendGroupMapMsgTo61616(queueMessageBoA,groupQueueDestination,"GroupA");
            amqQueueSenderService.sendGroupMapMsgTo61616(queueMessageBoB,groupQueueDestination,"GroupB");

            //如果不进行消息分组 三个监听器会轮流消费不同组里面的消息
//            amqQueueSenderService.sendMsgTo61616(queueMessageBoA,groupQueueDestination);
//            amqQueueSenderService.sendMsgTo61616(queueMessageBoB,groupQueueDestination);
        }

        Thread.sleep(10000);

        System.out.println("信息发送接收完毕");
    }









}
