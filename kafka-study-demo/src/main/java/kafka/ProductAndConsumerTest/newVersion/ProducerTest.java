package kafka.ProductAndConsumerTest.newVersion;

import kafka.ProductAndConsumerTest.PatitionersImpl.HashPartitioner;
import kafka.ProductAndConsumerTest.KafkaProducerUtils;
import org.apache.kafka.clients.producer.*;
import org.junit.Test;

import java.util.Properties;

/**
 * @Description Kafka 2.12_1.1.0 版本的生产测试
 * @Date 2019/5/23 0023 下午 4:36
 * @Created by Pengrenjun
 */
public class ProducerTest {

    /**
     * 采用轮询分发的策略进行消息的发送 RoundRobinPartitioner
     */
    @Test
    public  void sendByRoundRobinPartitioner(){

        Properties props = KafkaProducerUtils.getProducerProperties(null);

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for (int i = 1; i <= 10; i++){
            System.out.println(i);
            producer.send(new ProducerRecord<String, String>("test1", Integer.toString(i), Integer.toString(i)));

        }
        producer.close();

        /** 消费端的数据：
         *  Topic:test1, offset = 32, PartitionID:0,key = 3, value = 3
         *  Topic:test1, offset = 33, PartitionID:0,key = 6, value = 6
         *  Topic:test1, offset = 34, PartitionID:0,key = 9, value = 9
         *  Topic:test1, offset = 32, PartitionID:2,key = 2, value = 2
         *  Topic:test1, offset = 33, PartitionID:2,key = 5, value = 5
         *  Topic:test1, offset = 34, PartitionID:2,key = 8, value = 8
         *  Topic:test1, offset = 26, PartitionID:1,key = 1, value = 1
         *  Topic:test1, offset = 27, PartitionID:1,key = 4, value = 4
         *  Topic:test1, offset = 28, PartitionID:1,key = 7, value = 7
         *  Topic:test1, offset = 29, PartitionID:1,key = 10, value = 10
         */
    }

    /**
     * 采用Hash的策略进行消息的发送 HashPartitioner
     * 相同的Key放在同一个分区
     */
    @Test
    public  void sendByHashPartitioner(){
        Properties properties=new Properties();

        properties.put("partitioner.class", HashPartitioner.class);

        Properties props = KafkaProducerUtils.getProducerProperties(properties);

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for (int i = 1; i <= 10; i++){
            System.out.println(i);
            //异步发送数据
            producer.send(new ProducerRecord<String, String>("test1", Integer.toString(i % 2),
                    Integer.toString(i)), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if(e != null) {
                        e.printStackTrace();
                    } else {
                        System.out.println("The offset of the record we just sent is: " + recordMetadata.offset());
                    }
                }
            });

        }
        producer.close();

        /** 消费端的数据：
         *  Topic:test1, offset = 35, PartitionID:0,key = 0, value = 2
         *  Topic:test1, offset = 36, PartitionID:0,key = 0, value = 4
         *  Topic:test1, offset = 37, PartitionID:0,key = 0, value = 6
         *  Topic:test1, offset = 38, PartitionID:0,key = 0, value = 8
         *  Topic:test1, offset = 39, PartitionID:0,key = 0, value = 10
         *  Topic:test1, offset = 30, PartitionID:1,key = 1, value = 1
         *  Topic:test1, offset = 31, PartitionID:1,key = 1, value = 3
         *  Topic:test1, offset = 32, PartitionID:1,key = 1, value = 5
         *  Topic:test1, offset = 33, PartitionID:1,key = 1, value = 7
         *  Topic:test1, offset = 34, PartitionID:1,key = 1, value = 9
         */
    }




}
