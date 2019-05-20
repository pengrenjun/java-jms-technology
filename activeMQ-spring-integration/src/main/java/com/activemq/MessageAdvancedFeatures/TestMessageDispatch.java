package com.activemq.MessageAdvancedFeatures;

import com.activemq.MessageDispatchAdvancedFeatures.ProducerThread;
import com.activemq.QueueMessageBo;
import com.activemq.service.AMQTopicSenderService;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.jms.JMSException;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 测试ActiveMQ message高级特性
 * @Author：pengrj
 * @Date : 2019/5/4 0004 14:36
 * @version:1.0
 */

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:message-Advanced-features.xml"})
public class TestMessageDispatch {


    @Autowired
    private AMQTopicSenderService amqTopicSenderService;

    @Resource(name="ScheduledTopic")
    private ActiveMQTopic activeMQScheduledTopic;

    @Resource(name="BlobMessageTopic")
    private ActiveMQTopic blobMessageTopic;


    /**
     * 测试向197 broker1 61616发送topic消息 并且监听advisory topic 的系统记录的消息
     * @throws InterruptedException
     */
    @Test
    public void testSendMessageToBroker1() throws InterruptedException {


        List<QueueMessageBo> queueMessageBoListA=new ArrayList<>();
        for(int i=1;i<=5;i++){

            QueueMessageBo queueMessageBo=new QueueMessageBo(Integer.toString(i),"生产者1 消息："+i,new Date());

            queueMessageBoListA.add(queueMessageBo);

        }

//        List<QueueMessageBo> queueMessageBoListB=new ArrayList<>();
//        for(int i=1;i<=5;i++){
//
//            QueueMessageBo queueMessageBo=new QueueMessageBo(Integer.toString(i),"生产者2 消息："+i,new Date());
//
//            queueMessageBoListB.add(queueMessageBo);
//
//        }
//
//        List<QueueMessageBo> queueMessageBoListC=new ArrayList<>();
//        for(int i=1;i<=5;i++){
//
//            QueueMessageBo queueMessageBo=new QueueMessageBo(Integer.toString(i),"生产者3 topic消息："+i,new Date());
//
//            queueMessageBoListC.add(queueMessageBo);
//
//        }

        /*经过测试发现 多个线程的消息发送者发送消息 两个监听者消费消息 可以保证消息的顺序消费*/
        ProducerThread producerThreadA=new ProducerThread(queueMessageBoListA,amqTopicSenderService);

//        ProducerThread producerThreadB=new ProducerThread(queueMessageBoListB,amqTopicSenderService);
//        ProducerThread producerThreadC=new ProducerThread(queueMessageBoListC,amqTopicSenderService);




        producerThreadA.start();
//        producerThreadB.start();
//        producerThreadC.start();

        Thread.sleep(10000);

        System.out.println("信息发送接收完毕");

    }


    //测试Activemq定时任务
    @Test
    public void testScheduldMessageTopic() throws InterruptedException {


            QueueMessageBo queueMessageBo=new QueueMessageBo("1","定时消息测试发送",new Date());

            amqTopicSenderService.sendSheduledMsgto61616(queueMessageBo,activeMQScheduledTopic);

            Thread.sleep(30*1000);

    }

    //测试向61616发送Blob Message
    @Test
    public void testSendBlobMessageTo61616() throws InterruptedException, JMSException {

          File file=new File("pom.xml");

          amqTopicSenderService.sendBlobMsgto61616(file,blobMessageTopic);

//          BlobMessageProducer blobMessageProducer=new BlobMessageProducer(file);
//
//          blobMessageProducer.startProduce();

          Thread.sleep(10*1000);


    }





}
