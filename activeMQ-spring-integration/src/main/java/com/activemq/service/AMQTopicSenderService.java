package com.activemq.service;

import com.activemq.QueueMessageBo;
import com.alibaba.fastjson.JSONObject;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @Description: ActiveMQ topic发布服务
 * @Author：pengrj
 * @Date : 2019/5/4 0004 15:08
 * @version:1.0
 */
@Service
public class AMQTopicSenderService {

    @Resource(name="jmsTopicTemplate61617")
    private JmsTemplate jmsTopicTemplate61617;

    @Resource(name="jmsTopicTemplate61616")
    private JmsTemplate jmsTopicTemplate61616;

    @Resource(name="userMessageConverter")
    private MessageConverter userMessageConverter;


    //向默认的队列发送消息
    public void sendMsgto61616(QueueMessageBo queueMessageBo) {

        final String msg =JSONObject.toJSONString(queueMessageBo);
        try {
            //向jmsTopicTemplate配置的默认的spring-topic 中存放消息数据
            jmsTopicTemplate61616.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {

                    return session.createTextMessage(msg);
                }
            });
        } catch (JmsException e) {
            e.printStackTrace();
        }

        System.out.println("61616topic消息发布完毕 内容："+queueMessageBo.getContent());


    }

    //向特定的队列发送消息
    public void sendMsgto61616(QueueMessageBo queueMessageBo, Destination destination) {

        final String msg =JSONObject.toJSONString(queueMessageBo);
        jmsTopicTemplate61616.setDefaultDestination(destination);
        try {
            //向jmsTopicTemplate配置的默认的spring-topic 中存放消息数据
            jmsTopicTemplate61616.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {

                    return session.createTextMessage(msg);
                }
            });
        } catch (JmsException e) {
            e.printStackTrace();
        }

        System.out.println("61616topic消息发布完毕");
    }


    //向队列发送消息,定时发送消息
    public void sendSheduledMsgto61616(QueueMessageBo queueMessageBo,Destination destination) {

        final String msg =JSONObject.toJSONString(queueMessageBo);
        jmsTopicTemplate61616.setDefaultDestination(destination);



        try {
            //向jmsTopicTemplate配置的默认的spring-topic 中存放消息数据
            jmsTopicTemplate61616.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {

                    //测试定时延迟发送消息
                    TextMessage textMessage = session.createTextMessage(msg);

                    //设置定时参数 注意类型一定要正确 否则不会执行
                    //延迟3s发送 long类型
                    textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,10*1000);
                    //重复发送的时间间隔 long类型
                    textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD,2*1000);
                    //发送的次数 int类型
                    textMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT,10);

                    return textMessage;
                }
            });
        } catch (JmsException e) {
            e.printStackTrace();
        }

        System.out.println("61616topic消息发布完毕 内容："+queueMessageBo.getContent());
    }

    //向队列发送blobmessage消息 本地文件
    public void sendBlobMsgto61616(final File file, Destination destination) {

        jmsTopicTemplate61616.setDefaultDestination(destination);
        try {
            //向jmsTopicTemplate配置的默认的spring-topic 中存放消息数据
            jmsTopicTemplate61616.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {

                    //这么写 现在到这块转换是报错的
                    //java.lang.ClassCastException:
                    // org.apache.activemq.jms.pool.PooledSession cannot be cast to org.apache.activemq.ActiveMQSession
                    ActiveMQSession activeMQSession=(ActiveMQSession) session;


                    //测试发送BlobMessage消息
                    BlobMessage blobMessage = activeMQSession.createBlobMessage(file);

                    return blobMessage;
                }
            });
        } catch (JmsException e) {
            e.printStackTrace();
        }

        System.out.println("61616topic发送文件信息完毕 内容："+file.toString());
    }


    //向队列直接发送对象 通过转换器对其进行转化
    public void convertAndSendBoTo61616(QueueMessageBo queueMessageBo,Destination destination) {

        jmsTopicTemplate61616.setDefaultDestination(destination);
        jmsTopicTemplate61616.setMessageConverter(userMessageConverter);

        //这种方式很简洁
        jmsTopicTemplate61616.convertAndSend(queueMessageBo);


        System.out.println("61616topic发送转换的消息完毕 内容："+queueMessageBo.getContent());
    }



}
