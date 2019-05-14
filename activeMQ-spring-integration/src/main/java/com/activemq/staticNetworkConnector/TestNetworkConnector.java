package com.activemq.staticNetworkConnector;

import com.activemq.QueueMessageBo;
import com.activemq.service.AMQQueueSenderService;
import com.activemq.service.AMQTopicSenderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.UUID;

/**
 * @Description: 测试ActiveMQd静态网络连接消息的两个Broker的集群模式消息发送和接收
 * @Author：pengrj
 * @Date : 2019/5/4 0004 14:36
 * @version:1.0
 */

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:amq-network.xml"})
public class TestNetworkConnector {


    @Autowired
    private AMQQueueSenderService amqQueueSenderService;

    @Autowired
    private AMQTopicSenderService amqTopicSenderService;


    /**
     * 测试向broker2发送消息 broker2是指向broker1的单向桥接模式
     * @throws InterruptedException
     */
    @Test
    public void testSendMessageToBroker2() throws InterruptedException {

        for(int i=1;i<=10;i++){
            QueueMessageBo queueMessageBo=new QueueMessageBo(UUID.randomUUID().toString(),"produce message to 61617 ："+i,new Date());
            amqQueueSenderService.sendMsgTo61617(queueMessageBo);
            //Thread.sleep(1000);
        }
//
    }


    /**
     * 测试向broker1发送消息
     * @throws InterruptedException
     */
    @Test
    public void testSendMessageToBroker1() throws InterruptedException {

        for(int i=1;i<=9;i++){
            QueueMessageBo queueMessageBo=new QueueMessageBo(UUID.randomUUID().toString(),"produce message to 61616 ："+i,new Date());
            amqQueueSenderService.sendMsgTo61616(queueMessageBo);
            //Thread.sleep(1000);
        }
//
    }

}
