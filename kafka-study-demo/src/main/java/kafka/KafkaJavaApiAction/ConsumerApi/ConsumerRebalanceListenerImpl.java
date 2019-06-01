package kafka.KafkaJavaApiAction.ConsumerApi;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

/**
 * 消费者平衡偏移量的监听实现
 */
public class ConsumerRebalanceListenerImpl implements ConsumerRebalanceListener {

    private KafkaConsumer kafkaConsumer;

    public ConsumerRebalanceListenerImpl(KafkaConsumer kafkaConsumer) {
        this.kafkaConsumer = kafkaConsumer;
    }

    /**
     * 消费者平衡操作之前 消费者停止拉取消息之后被调用
     * @param collection
     */
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {

        for(TopicPartition partition: collection){

            System.out.println(" saving offsets in a custom store -> kafkaConsumer postion ："+
                    kafkaConsumer.position(partition));

        }

        //提交偏移量 防止消息的重复消费
        kafkaConsumer.commitSync();

    }

    /**
     * 平衡后 消费者开始拉取消息之前调用
     * @param collection
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {

        //重置各消费者的偏移量
        long committedOffset=-1;
        for(TopicPartition topicPartition:collection){
            //获取该分区已经消费的偏移量
            committedOffset=kafkaConsumer.committed(topicPartition).offset();
            //重置偏移量到上一次提交的偏移量的下一个位置处开始消费
            kafkaConsumer.seek(topicPartition,committedOffset+1);
        }
    }
}
