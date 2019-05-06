package com.activemq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description: 测试ActiveMQ与spring整合配置的JmsTemplate
 * @Author：pengrj
 * @Date : 2019/5/4 0004 14:36
 * @version:1.0
 */

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:spring-ActiveMQ.xml"})
public class TestJmsTemplate {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void testSendMessage(){



    }




}
