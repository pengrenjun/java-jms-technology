package com.activemq.destinationAdvancedFeatures;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @Description 虚拟topic 消费端 第一次使用 需要先启动消费端对主题topic进行订阅
 * @Date 2019/5/14 0014 下午 3:53
 * @Created by Pengrenjun
 */
public class VirtualTopicConsumer {
    public static void main(String[] args) throws JMSException, InterruptedException {
        // 连接到ActiveMQ服务器
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://10.0.99.197:61616");
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        // 创建主题 topic->queue
        Queue topicA = session.createQueue("Consumer.A.VirtualTopic.TEST");
        Queue topicB = session.createQueue("Consumer.B.VirtualTopic.TEST");


        // 消费者A组创建订阅
        MessageConsumerAThread messageConsumerAThread1=new MessageConsumerAThread(session,topicA,"A1");
        MessageConsumerAThread messageConsumerAThread2=new MessageConsumerAThread(session,topicA,"A2");
        messageConsumerAThread2.run();
        messageConsumerAThread1.run();


        MessageConsumerBThread messageConsumerBThread1=new MessageConsumerBThread(session,topicB,"B1");
        MessageConsumerBThread messageConsumerBThread2=new MessageConsumerBThread(session,topicB,"B2");

        messageConsumerBThread1.run();
        messageConsumerBThread2.run();

//        session.close();
//        connection.close();
    }

    static class MessageConsumerAThread implements Runnable{

        private Session session;

        private Queue queue;

        private String name;

        public MessageConsumerAThread(Session session, Queue queue, String name) {
            this.session = session;
            this.queue = queue;
            this.name = name;
        }

        @Override
        public void run() {
            // 消费者A组创建订阅
            MessageConsumer consumerA1 = null;
            try {
                consumerA1 = session.createConsumer(queue);
            } catch (JMSException e) {
                e.printStackTrace();
            }
            try {
                consumerA1.setMessageListener(new MessageListener() {
                    // 订阅接收方法
                    public void onMessage(Message message) {
                        TextMessage tm = (TextMessage) message;
                        try {
                            System.out.println("Received message "+name+": " + tm.getText());
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    static class MessageConsumerBThread implements Runnable{

        private Session session;

        private Queue queue;

        private String name;

        public MessageConsumerBThread(Session session, Queue queue, String name) {
            this.session = session;
            this.queue = queue;
            this.name = name;
        }

        @Override
        public void run() {
            // 消费者B组创建订阅
            MessageConsumer consumerA1 = null;
            try {
                consumerA1 = session.createConsumer(queue);
            } catch (JMSException e) {
                e.printStackTrace();
            }
            try {
                consumerA1.setMessageListener(new MessageListener() {
                    // 订阅接收方法
                    public void onMessage(Message message) {
                        TextMessage tm = (TextMessage) message;
                        try {
                            System.out.println("Received message "+name+": " + tm.getText());

                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }


}
