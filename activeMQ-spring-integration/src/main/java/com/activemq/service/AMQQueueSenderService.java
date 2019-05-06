package com.activemq.service;

import com.activemq.QueueMessageBo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @Description: ActiveMQ Queue消息发送服务
 * @Author：pengrj
 * @Date : 2019/5/4 0004 15:08
 * @version:1.0
 */
@Service
public class AMQQueueSenderService  {

    @Resource(name="jmsQueueTemplate")
    private JmsTemplate jmsQueueTemplate;

    //向特定的队列发送消息
    public void sendMsg(QueueMessageBo queueMessageBo) {

        final String msg =JSONObject.toJSONString(queueMessageBo);
        try {
            //向jmsQueueTemplate配置的默认的spring-queue 中存放消息数据
            jmsQueueTemplate.send(new MessageCreator() {
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

        System.out.println(msg+"消息发送完毕");


    }

}
