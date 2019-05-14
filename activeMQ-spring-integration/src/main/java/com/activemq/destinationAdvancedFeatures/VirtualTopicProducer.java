package com.activemq.destinationAdvancedFeatures;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Description 虚拟topic 发送端
 * @Date 2019/5/14 0014 下午 3:53
 * @Created by Pengrenjun
 */
public class VirtualTopicProducer {

    public static void main(String[] args) throws JMSException {
        // 连接到ActiveMQ服务器
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://10.0.99.197:61616");
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        // 创建主题
        Topic topic = session.createTopic("VirtualTopic.TEST");
        MessageProducer producer = session.createProducer(topic);
        // NON_PERSISTENT 非持久化 PERSISTENT 持久化,发送消息时用使用持久模式
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        for(int i=0;i<10;i++){
            TextMessage message = session.createTextMessage();

            message.setText("topic 消息:"+i);
            // 发布主题消息
            producer.send(message);
            System.out.println("Sent message: " + message.getText());
        }
        session.close();
        connection.close();
    }
}
