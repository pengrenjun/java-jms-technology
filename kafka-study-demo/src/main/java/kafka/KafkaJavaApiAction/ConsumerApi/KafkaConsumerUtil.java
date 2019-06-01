package kafka.KafkaJavaApiAction.ConsumerApi;

import kafka.properties.KafkaConsumerPropertiesUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.util.*;

/**
 * 新版KafkaConsumer使用工具类
 * k:key的数据类型 v:value的数据类型
 */
public class KafkaConsumerUtil<K,V> {

    /**
     * 实例化Kafka 消费者
     * @param properties 自定义的消费端的配置信息
     * @return
     */
    public  KafkaConsumer<K,V> instanceKafkaConsumer(Properties properties){

        Properties consumerProps=KafkaConsumerPropertiesUtils.getProducerProperties(properties);

        KafkaConsumer<K,V> kvKafkaConsumer=new KafkaConsumer<K, V>(consumerProps);

        return kvKafkaConsumer;
    }

    /**
     * 订阅主题
     * @param topics 主题集合
     * @param kafkaConsumer 实例化的消费者
     * @param consumerRebalanceListener 消费者平衡偏移量的回调监听
     */
    public KafkaConsumer subscribTopic(List<String> topics, KafkaConsumer kafkaConsumer,
                              ConsumerRebalanceListener consumerRebalanceListener){

        //subscribe方法具有消费者自动均衡消费的功能 多线程的情况下 会根据分区的分配策略自动分配消费者线程和分区的关系
        kafkaConsumer.subscribe(topics,consumerRebalanceListener);

        return kafkaConsumer;
    }

    /**
     * 订阅主题的特定分区消息
     * @param topicPartitions 主题及分区集合
     * @param kafkaConsumer 实例化的消费者
     */
    public KafkaConsumer assignTopicPartitions(List<TopicPartition> topicPartitions, KafkaConsumer kafkaConsumer){

        //assign方法不具有自动均衡的功能
        kafkaConsumer.assign(topicPartitions);
        return kafkaConsumer;
    }

    /**
     * KafkaConsume拉取消费消息并且自动提交偏移量
     * @param kafkaConsumer  
     * @param timeout
     */
    public void pollRecordAndAutoManageOffset(KafkaConsumer kafkaConsumer,long timeout){

        try {
            //自动提交偏移量 客户端只关注业务处理
            ConsumerRecords<K,V> records = kafkaConsumer.poll(timeout);

            for(ConsumerRecord<K,V> record:records){

                System.out.println("poll->获取到的信息:"+record.toString() +" 进行业务处理");

               // Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找距离当前时间之前一段时间的消息数据 按时间戳进行数据查找 从之前的某个时间段开始消费数据
     * 消费主题的某些分区
     * @param kafkaConsumer
     * @param timeBeforCurrentTime
     */
    public ConsumerRecords timeStampsToSearchRecords(KafkaConsumer kafkaConsumer,List<TopicPartition> topicPartitions,long timeBeforCurrentTime){

        //订阅主题的某些分区数据
        kafkaConsumer.assign(topicPartitions);

        //待查询分区Map
        Map<TopicPartition,Long> timestampsToSearch = new HashMap<TopicPartition , Long>() ;

        //构造待查询分区及其偏移量
        for(TopicPartition topicPartition:topicPartitions){
            timestampsToSearch.put(topicPartition,System.currentTimeMillis()-timeBeforCurrentTime);
        }

        //返回各个待查询分区的timeBeforCurrentTime的第一个时间偏移量
        Map<TopicPartition, OffsetAndTimestamp>  offsetMap  = kafkaConsumer.offsetsForTimes( timestampsToSearch);

        OffsetAndTimestamp  offsetTimestamp = null ;

        for  ( Map.Entry<TopicPartition,OffsetAndTimestamp>  entry :offsetMap.entrySet ()){
            //若查询的时间大于时间戳索引文件的最大的记录时间,则返回的value为空 即待查询的时间点之后没有新的消息
            offsetTimestamp =  entry.getValue() ;
            if(Objects.nonNull(offsetTimestamp)){
                //重置消息起始的偏移量
                kafkaConsumer.seek(entry.getKey(),entry.getValue().offset());
            }
        }

        //拉取消息
        ConsumerRecords<String, String>  records=  kafkaConsumer.poll(1000);
        return  records;

    }

}
