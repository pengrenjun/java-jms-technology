package com.activemq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
