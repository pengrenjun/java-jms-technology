package com.activemq.MessageAdvancedFeatures;

import com.oracle.jrockit.jfr.Producer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.io.File;
import java.sql.Date;

import static javax.jms.Session.AUTO_ACKNOWLEDGE;

/**
 * @Description: ActiveMQ BlobMessage消息生产
 * @Author??pengrj
 * @Date : 2018/11/10 0010 19:20
 * @version:1.0
 */
public class BlobMessageProducer {

    private File file;

    public BlobMessageProducer(File file) {
        this.file = file;
    }

    public  void startProduce() throws JMSException {


        //使用jms-file-server 模块的内置jetty文件服务器实现大文件的传输和读取
        ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://10.0.99.197:61616?jms.blobTransferPolicy.uploadUrl=http://localhost:8080/fileserver/");

        Connection connection=connectionFactory.createConnection(ActiveMQConnectionFactory.DEFAULT_USER,ActiveMQConnectionFactory.DEFAULT_PASSWORD);
        connection.start();

        ActiveMQSession session=(ActiveMQSession)connection.createSession(true,AUTO_ACKNOWLEDGE);


        Destination destination=session.createTopic("test-BlobMessageTopic");



        MessageProducer messageProducer1=session.createProducer(destination);


        messageProducer1.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


        BlobMessage blobMessage=session.createBlobMessage(file);

        blobMessage.setName(file.getName());

        messageProducer1.send(blobMessage);


        session.commit();

        connection.close();

        System.out.println("Producers has sended blob Messages ok!");
    }


}
