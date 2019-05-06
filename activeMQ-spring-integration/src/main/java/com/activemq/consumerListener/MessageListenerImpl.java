package com.activemq.consumerListener;

import com.activemq.QueueMessageBo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import javax.jms.*;

/**
 * @Description: ActiveMq与spring整合 消费端： message消息监听器
 * @Author：pengrj
 * @Date : 2019/5/6 0006 22:43
 * @version:1.0
 */
public class MessageListenerImpl implements MessageListener {
    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            try {
                TextMessage txtMsg = (TextMessage) message;
                String msgText = txtMsg.getText();
                //实际项目中拿到String类型的message(通常是JSON字符串)之后，
                //会进行反序列化成对象，做进一步的处理
                QueueMessageBo queueMessageBo = JSON.parseObject(msgText, new TypeReference<QueueMessageBo>() {});
                System.out.println("MessageListenerImpl监听器receive txt msg===" + queueMessageBo.toString());
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }

        if (message instanceof MapMessage) {
            try {
                MapMessage txtMsg = (MapMessage) message;
                String msgText = txtMsg.getString("info");
                //实际项目中拿到String类型的message(通常是JSON字符串)之后，
                //会进行反序列化成对象，做进一步的处理
                QueueMessageBo queueMessageBo = JSON.parseObject(msgText, new TypeReference<QueueMessageBo>() {});
                System.out.println("MessageListenerImpl监听器receive map msg===" + queueMessageBo.toString());


            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }



        else {
            throw new IllegalArgumentException("Message must be of type TextMessage or MapMesaage");
        }

    }
}
