package com.activemq.service;

import com.activemq.QueueMessageBo;
import com.alibaba.fastjson.JSONObject;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * @Description: ActiveMQ topic发布服务
 * @Author：pengrj
 * @Date : 2019/5/4 0004 15:08
 * @version:1.0
 */
@Service
public class AMQTopicSenderService {

    @Resource(name="jmsTopicTemplate")
    private JmsTemplate jmsTopicTemplate;

    //向特定的队列发送消息
    public void sendMsg(QueueMessageBo queueMessageBo) {

        final String msg =JSONObject.toJSONString(queueMessageBo);
        try {
            //向jmsTopicTemplate配置的默认的spring-topic 中存放消息数据
            jmsTopicTemplate.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {

                    return session.createTextMessage(msg);
                }
            });
        } catch (JmsException e) {
            e.printStackTrace();
        }

        System.out.println(msg+"消息发布完毕");


    }

}
