package com.activemq.MessageDispatchAdvancedFeatures;

import com.activemq.QueueMessageBo;
import com.activemq.service.AMQQueueSenderService;
import com.activemq.service.AMQTopicSenderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description TODO
 * @Date 2019/5/17 0017 下午 3:50
 * @Created by Pengrenjun
 */
public class ProducerThread extends Thread {


    private AMQTopicSenderService amqTopicSenderService;


    private List<QueueMessageBo> queueMessageBoList;


    public ProducerThread(List<QueueMessageBo> queueMessageBoList,AMQTopicSenderService amqTopicSenderService) {
        this.queueMessageBoList = queueMessageBoList;
        this.amqTopicSenderService=amqTopicSenderService;

    }

    @Override
    public void run() {
        for(int i=0;i<queueMessageBoList.size();i++) {
            amqTopicSenderService.sendMsgto61616(queueMessageBoList.get(i));
        }
    }
}
