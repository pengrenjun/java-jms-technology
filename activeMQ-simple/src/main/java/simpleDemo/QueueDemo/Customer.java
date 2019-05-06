package simpleDemo.QueueDemo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

/**
 * @Description: ActiveMq消费
 * @Author��pengrj
 * @Date : 2018/11/10 0010 19:20
 * @version:1.0
 */
public class Customer {

    public  void startCustom() throws JMSException, InterruptedException {


        Connection connection= null;
        Session session= null;
        try {
            ConnectionFactory connectionFactory=
                    new ActiveMQConnectionFactory("tcp://10.0.99.197:61616");

            connection = connectionFactory.
                    createConnection(ActiveMQConnectionFactory.DEFAULT_USER,
                            ActiveMQConnectionFactory.DEFAULT_PASSWORD);
            connection.start();


            session = connection.createSession(true,AUTO_ACKNOWLEDGE);


            Destination destination=session.createQueue("TextQueue");

            Destination destination1=session.createQueue("MapQueue");


            MessageConsumer messageConsumer=session.createConsumer(destination);
            MessageConsumer messageConsumer1=session.createConsumer(destination1);


            //receive： 同步消费的方式 当消息中间件中没有可获取的消息时 会一直阻塞 直到获取到消息
            // receive(long timeout)：设定阻塞的时间
            TextMessage textMessage= (TextMessage) messageConsumer.receive();

            MapMessage mapMessage= (MapMessage) messageConsumer1.receive();



            System.out.println("TextQueue->"+textMessage.getText());

            System.out.println("MapQueue->"+mapMessage.getMapNames().toString());

            //提交之后消息需要确认接受被消费了,否则下次还能获取到消息
            session.commit();
            System.out.println("Customer has customed Messages ok!");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
        }
        session.close();
        connection.close();
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        Customer customer=new Customer();
        customer.startCustom();
    }
}
