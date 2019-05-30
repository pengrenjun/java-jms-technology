package kafka.KafkaJavaApiAction.producerApi;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Objects;

/**
 * @Description 异步发送回调实现类 实现org.apache.kafka.clients.producer.Callback
 * @Date 2019/5/30 0030 下午 5:00
 * @Created by Pengrenjun
 */
public class ProducerSyncSendCallBackImpl implements Callback {
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {

        if(Objects.nonNull(e)){

            System.out.println("消息发送异常："+e.toString());
        }
        else {
            System.out.println("消息发送OK! 返回的信息->"+recordMetadata.toString()+" offset:"+recordMetadata.offset());
        }
    }
}
