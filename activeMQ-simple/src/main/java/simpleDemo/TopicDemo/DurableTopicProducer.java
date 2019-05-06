package simpleDemo.TopicDemo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

/**
 * @Description: ActiveMQ 持久化的Topic消息 发送者
 * @Author pengrj
 * @Date : 2018/11/10 0010 19:20
 * @version:1.0
 */
public class DurableTopicProducer {


    public  void startProduce() throws JMSException {


        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://10.0.99.197:61616");

        Connection connection=connectionFactory.createConnection(ActiveMQConnectionFactory.DEFAULT_USER,ActiveMQConnectionFactory.DEFAULT_PASSWORD);


        Session session=connection.createSession(true,AUTO_ACKNOWLEDGE);


        Destination destination=session.createTopic("durable-topic");

        MessageProducer messageProducer=session.createProducer(destination);

        //发送数据 持久的
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

        //设置DeliveryMode 之后再启动
        connection.start();

        TextMessage textMessage=new ActiveMQTextMessage();
        for(int i=1;i<=10;i++){
            textMessage.setText("durable-Message>"+i);
            messageProducer.send(textMessage);
        }



        session.commit();

        connection.close();

        System.out.println("durable-top-Producers has sended Messages ok!");
    }

    public static void main(String[] args) throws JMSException {
        DurableTopicProducer producer=new DurableTopicProducer();
        producer.startProduce();
    }
}
