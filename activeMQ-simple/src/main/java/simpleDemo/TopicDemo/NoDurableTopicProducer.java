package simpleDemo.TopicDemo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.sql.Date;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

/**
 * @Description: ActiveMQ 非持久化的Topic消息生产者
 * @Author pengrj
 * @Date : 2018/11/10 0010 19:20
 * @version:1.0
 */
public class NoDurableTopicProducer {


    public  void startProduce() throws JMSException {


        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://10.0.99.197:61616");

        Connection connection=connectionFactory.createConnection(ActiveMQConnectionFactory.DEFAULT_USER,ActiveMQConnectionFactory.DEFAULT_PASSWORD);
        connection.start();

        Session session=connection.createSession(true,AUTO_ACKNOWLEDGE);


        Destination destination=session.createTopic("testA-topic");

        MessageProducer messageProducer=session.createProducer(destination);

        //发送临时节点的数据 非持久的
        messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        TextMessage textMessage=new ActiveMQTextMessage();
        for(int i=1;i<=10;i++){
            textMessage.setText("top-Message>"+i);
            messageProducer.send(textMessage);
        }



        session.commit();

        connection.close();

        System.out.println("top-Producers has sended Messages ok!");
    }

    public static void main(String[] args) throws JMSException {
        NoDurableTopicProducer producer=new NoDurableTopicProducer();
        producer.startProduce();
    }
}
