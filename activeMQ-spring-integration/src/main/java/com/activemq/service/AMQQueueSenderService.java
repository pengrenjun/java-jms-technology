package com.activemq.service;

import com.activemq.QueueMessageBo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * @Description: ActiveMQ Queue消息发送服务
 * @Author：pengrj
 * @Date : 2019/5/4 0004 15:08
 * @version:1.0
 */
@Service
public class AMQQueueSenderService  {

    @Resource(name="jmsQueueTemplate61616")
    private JmsTemplate jmsQueueTemplate61616;

    @Resource(name="jmsQueueTemplate61617")
    private JmsTemplate jmsQueueTemplate61617;





    //向默认的队列发送消息(xml中配置)
    public void sendMsgTo61616(final QueueMessageBo queueMessageBo) {


        final String msg =JSONObject.toJSONString(queueMessageBo);
        try {

            //向jmsQueueTemplate配置的默认的spring-queue 中存放消息数据
            jmsQueueTemplate61616.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {

                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("info",msg);
                    System.out.println("向61616消息发送完毕 内容："+queueMessageBo.getContent());
                    return mapMessage;
                }
            });
        } catch (JmsException e) {
            e.printStackTrace();
        }


    }

    //向特定的队列发送消息 指定目标
    public void sendMsgTo61616(QueueMessageBo queueMessageBo, Destination destination) {


        final String msg =JSONObject.toJSONString(queueMessageBo);
        try {
            jmsQueueTemplate61616.setDefaultDestination(destination);

            //向jmsQueueTemplate配置的默认的spring-queue 中存放消息数据
            jmsQueueTemplate61616.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {

                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("info",msg);
                    return mapMessage;
                }
            });
        } catch (JmsException e) {
            e.printStackTrace();
        }

        System.out.println("向61616消息发送完毕 内容："+queueMessageBo.getContent());
    }

    //向特定的队列发送消息
    public void sendMsgTo61617(QueueMessageBo queueMessageBo) {

        final String msg =JSONObject.toJSONString(queueMessageBo);
        try {
            //向jmsQueueTemplate配置的默认的spring-queue 中存放消息数据
            jmsQueueTemplate61617.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {

                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("info",msg);
                    return mapMessage;
                }
            });
        } catch (JmsException e) {
            e.printStackTrace();
        }

        System.out.println("向61617消息发送完毕");
    }

}
