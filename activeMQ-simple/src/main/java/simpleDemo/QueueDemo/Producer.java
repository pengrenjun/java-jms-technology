package simpleDemo.QueueDemo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;


import java.sql.Date;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

/**
 * @Description: ActiveMQ消息生产
 * @Author??pengrj
 * @Date : 2018/11/10 0010 19:20
 * @version:1.0
 */
public class Producer {


    public  void startProduce() throws JMSException {


        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://10.0.99.197:61616");

        Connection connection=connectionFactory.createConnection(ActiveMQConnectionFactory.DEFAULT_USER,ActiveMQConnectionFactory.DEFAULT_PASSWORD);
        connection.start();

        Session session=connection.createSession(true,AUTO_ACKNOWLEDGE);


        Destination destination=session.createQueue("TextQueue");

        Destination destination1=session.createQueue("MapQueue");



        MessageProducer messageProducer=session.createProducer(destination);

        MessageProducer messageProducer1=session.createProducer(destination1);

        //发送临时节点的数据
        messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        messageProducer1.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


        TextMessage textMessage=new ActiveMQTextMessage();
        for(int i=0;i<10;i++){
            textMessage.setText("Message>"+i+":"+String.valueOf(new Date(System.currentTimeMillis())));
            messageProducer.send(textMessage);
        }

        MapMessage mapMessage=new ActiveMQMapMessage();
        for(int i=0;i<10;i++){
            mapMessage.setString(String.valueOf(i),String.valueOf(System.currentTimeMillis()));
            messageProducer1.send(mapMessage);
        }


        session.commit();

        connection.close();

        System.out.println("Producers has sended Messages ok!");
    }

    public static void main(String[] args) throws JMSException {
        Producer producer=new Producer();
        producer.startProduce();
    }
}
