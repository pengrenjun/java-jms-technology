package com.activemq.MessageAdvancedFeatures;

import com.activemq.QueueMessageBo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.activemq.BlobMessage;

import javax.jms.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: ActiveMq与spring整合 消费端： BlobMessage消息监听器
 * @Author：pengrj
 * @Date : 2019/5/6 0006 22:43
 * @version:1.0
 */
public class Message61616BlobMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {

        if (message instanceof BlobMessage) {

                BlobMessage txtMsg = (BlobMessage) message;

                try {
                    InputStream inputStream=txtMsg.getInputStream();

                    byte[] bytes=new byte[inputStream.available()];

                    inputStream.read(bytes);

                    inputStream.close();
                    System.out.println("Message61616BlobMessageListener 接收到的文件内容："+new String(bytes));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            return;
        }



        else {
            throw new IllegalArgumentException("Message must be of type TextMessage or MapMesaage");
        }

    }
}
