package com.activemq.MessageAdvancedFeatures;

import com.activemq.QueueMessageBo;

/**
 * @Description bo转换器消息接收的监听
 * @Date 2019/5/20 0020 下午 6:07
 * @Created by Pengrenjun
 */
public class Message61616BoConvertListener {

    public void receiveMessage(QueueMessageBo queueMessageBo) {
        System.out.println(queueMessageBo.toString());
    }

    public void receiveMessage(String message) {
        System.out.println("接收到一个纯文本消息，消息内容是：" + message);
    }

}
