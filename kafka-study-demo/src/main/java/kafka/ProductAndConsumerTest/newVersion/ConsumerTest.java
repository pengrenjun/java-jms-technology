package kafka.ProductAndConsumerTest.newVersion;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

/**
 * @Description Kafka 2.12_1.1.0 版本的消费测试
 * @Date 2019/5/23 0023 下午 4:37
 * @Created by Pengrenjun
 */
public class ConsumerTest {

    Logger logger = LoggerFactory.getLogger(ConsumerTest.class);

    public static void poll(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.0.99.197:9092");//Kafka集群
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("test1"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(10000);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("consumer接受的数据： Topic:%s, offset = %d, PartitionID:%s,key = %s, value = %s%n",
                        record.topic(),record.offset(),record.partition(), record.key(), record.value());
        }
    }
    public static void main(String[] args){
        poll();
    }
}
