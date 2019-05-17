package com.activemq.MessageDispatchAdvancedFeatures;

import com.activemq.QueueMessageBo;
import com.activemq.service.AMQQueueSenderService;
import com.activemq.service.AMQTopicSenderService;
import com.oracle.jrockit.jfr.Producer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @Description: 测试ActiveMQ 消息分发的高级特性
 * @Author：pengrj
 * @Date : 2019/5/4 0004 14:36
 * @version:1.0
 */

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:message-dispatch-Advanced-features.xml"})
public class TestMessageDispatch {


    @Autowired
    private AMQTopicSenderService amqTopicSenderService;







    /**
     * 测试向197 broker1 61616发送消息
     * @throws InterruptedException
     */
    @Test
    public void testSendMessageToBroker1() throws InterruptedException {


        List<QueueMessageBo> queueMessageBoListA=new ArrayList<>();
        for(int i=1;i<=5;i++){

            QueueMessageBo queueMessageBo=new QueueMessageBo(Integer.toString(i),"生产者1 消息："+i,new Date());

            queueMessageBoListA.add(queueMessageBo);

        }

        List<QueueMessageBo> queueMessageBoListB=new ArrayList<>();
        for(int i=1;i<=5;i++){

            QueueMessageBo queueMessageBo=new QueueMessageBo(Integer.toString(i),"生产者2 消息："+i,new Date());

            queueMessageBoListB.add(queueMessageBo);

        }

        List<QueueMessageBo> queueMessageBoListC=new ArrayList<>();
        for(int i=1;i<=5;i++){

            QueueMessageBo queueMessageBo=new QueueMessageBo(Integer.toString(i),"生产者3 topic消息："+i,new Date());

            queueMessageBoListC.add(queueMessageBo);

        }

        ProducerThread producerThreadA=new ProducerThread(queueMessageBoListA,amqTopicSenderService);

        ProducerThread producerThreadB=new ProducerThread(queueMessageBoListB,amqTopicSenderService);
        ProducerThread producerThreadC=new ProducerThread(queueMessageBoListC,amqTopicSenderService);




        producerThreadA.start();
        producerThreadB.start();
        producerThreadC.start();

        Thread.sleep(10000);

        System.out.println("信息发送接收完毕");

    }



}
