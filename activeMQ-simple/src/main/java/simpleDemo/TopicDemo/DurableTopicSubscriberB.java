package simpleDemo.TopicDemo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

/**
 * @Description: ActiveMq 持久化的Topic消息 订阅者
 * @Author pengrj
 * @Date : 2018/11/10 0010 19:20
 * @version:1.0
 */
public class DurableTopicSubscriberB {


    //注意 持久化的订阅消费模式 订阅者需要先启动一次 在jms中先进行注册 这样发送者才会根据客户端的Id标识实现消息的存储发送

    public  void startCustom() throws JMSException, InterruptedException {


        Connection connection= null;
        Session session= null;
        try {
            ConnectionFactory connectionFactory=
                    new ActiveMQConnectionFactory("tcp://10.0.99.197:61616");

            connection = connectionFactory.
                    createConnection(ActiveMQConnectionFactory.DEFAULT_USER,
                            ActiveMQConnectionFactory.DEFAULT_PASSWORD);
            //设置订阅者ID 用来识别不同的订阅者 id相同则订阅者相同 不重复创建
            connection.setClientID("client1");

            session = connection.createSession(true,AUTO_ACKNOWLEDGE);

            Topic destination=session.createTopic("Default-TestMessageDispatch-Topic");

            //创建持久化的订阅
            TopicSubscriber topicSubscriber=session.createDurableSubscriber(destination,"SubscriberA");

            connection.start();


            TextMessage textMessage= (TextMessage) topicSubscriber.receive();

            while(textMessage!=null){

                System.out.println("SubscriberA获取的topic信息："+textMessage.getText());
                textMessage= (TextMessage) topicSubscriber.receive(200);
            }


        } catch (JMSException e) {
            System.out.println("SubscriberA已获取到了全部的消息！");
        } finally {
            //提交之后消息需要确认接受被消费了,否则下次还能获取到消息
            session.commit();
            System.out.println("SubscriberA has customed Messages ok!");
            session.close();
            connection.close();
        }

    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        DurableTopicSubscriberB customer=new DurableTopicSubscriberB();
        customer.startCustom();
    }
}
