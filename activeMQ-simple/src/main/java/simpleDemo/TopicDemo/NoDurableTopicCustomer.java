package simpleDemo.TopicDemo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

/**
 * @Description: ActiveMq 非持久化的Topic消息 消费者
 * @Author pengrj
 * @Date : 2018/11/10 0010 19:20
 * @version:1.0
 */
public class NoDurableTopicCustomer {

    //注意：Pub-sub非订阅模式（非持久）一定要先启动customer再启动producer 否则customer端接收不到信息

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


            Destination destination=session.createTopic("testA-topic");

            MessageConsumer messageConsumer=session.createConsumer(destination);

            //receive： 同步消费的方式 当消息中间件中没有可获取的消息时 会一直阻塞 直到获取到消息
            // receive(long timeout)：设定阻塞的时间
            TextMessage textMessage= (TextMessage) messageConsumer.receive();

            while(textMessage!=null){

                System.out.println("获取的topic信息："+textMessage.getText());
                textMessage= (TextMessage) messageConsumer.receive(200);
            }


        } catch (JMSException e) {
            System.out.println("已获取到了全部的消息！");
        } finally {
            //提交之后消息需要确认接受被消费了,否则下次还能获取到消息
            session.commit();
            System.out.println("topic-Customer has customed Messages ok!");
            session.close();
            connection.close();
        }

    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        NoDurableTopicCustomer customer=new NoDurableTopicCustomer();
        customer.startCustom();
    }
}
