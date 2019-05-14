package com.activemq.destinationAdvancedFeatures;

import com.activemq.QueueMessageBo;
import com.activemq.service.AMQQueueSenderService;
import com.activemq.service.AMQTopicSenderService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.UUID;

/**
 * @Description: 测试ActiveMQ 的Destination高级特性
 * @Author：pengrj
 * @Date : 2019/5/4 0004 14:36
 * @version:1.0
 */

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:destination-Advanced-features.xml"})
public class TestDestinationAdvancedFeatures {


    @Autowired
    private AMQQueueSenderService amqQueueSenderService;

    @Autowired
    private AMQTopicSenderService amqTopicSenderService;

    @Resource(name = "compositeQueue")
    private ActiveMQQueue compositeAMQQueues;

    @Resource(name="compositeQueueAndTopic")
    private ActiveMQQueue compositeAMQQueueAndTopic;

    @Resource(name="virtualTopic")
    private ActiveMQTopic virtualTopic;

    @Resource(name="virtualTopicCustomerQueues")
    private ActiveMQQueue virtualTopicCustomerQueues;

    @Resource(name="jmsQueueTemplate61616")
    private JmsTemplate jmsQueueTemplate61616;



    /**
     * 测试向61616发送组合队列 queues
     * @throws InterruptedException
     */
    @Test
    public void testSendToCompositQueues() throws InterruptedException {

        for(int i=1;i<=10;i++){
            QueueMessageBo queueMessageBo=new QueueMessageBo(UUID.randomUUID().toString(),"composite queue test ："+i,new Date());
            amqQueueSenderService.sendMsgTo61616(queueMessageBo,compositeAMQQueues);
            //Thread.sleep(1000);
        }
//
    }


    /**
     *  测试向61616  发送组合队列 queue+topic
     * @throws InterruptedException
     */
    @Test
    public void testSendToCompositQueueAndTopic() throws InterruptedException {

        for(int i=1;i<=9;i++){
            QueueMessageBo queueMessageBo=new QueueMessageBo(UUID.randomUUID().toString(),"composite queueTopic test ： ："+i,new Date());
            amqQueueSenderService.sendMsgTo61616(queueMessageBo,compositeAMQQueueAndTopic);

            //amqTopicSenderService.sendMsgto61616(queueMessageBo,compositeAMQQueueAndTopic);

            //Thread.sleep(1000);
        }
//
    }

    /**
     *  测试向61616  发送虚拟队列
     * @throws InterruptedException
     */
    @Test
    public void testSendToVirtualTopic() throws InterruptedException {
       /* QueueMessageBo queueMessageBo=new QueueMessageBo(UUID.randomUUID().toString(),"testSendToVirtualTopic",new Date());


        amqQueueSenderService.sendMsgTo61616(queueMessageBo,virtualTopicCustomerQueues);*/

       //先启动VirtualTopicConsumer类,再执行消息发送
       for(int i=1;i<=9;i++){
            QueueMessageBo queueMessageBoB=new QueueMessageBo(UUID.randomUUID().toString(),"虚拟队列消息 test ： ："+i,new Date());
            amqTopicSenderService.sendMsgto61616(queueMessageBoB,virtualTopic);
            //Thread.sleep(1000);
        }
//
    }




}
