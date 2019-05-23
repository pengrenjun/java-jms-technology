package kafka.ProductAndConsumerTest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @Description TODO
 * @Date 2019/5/23 0023 下午 4:36
 * @Created by Pengrenjun
 */
public class ProducerTest {

    public static void send(){
        Properties props = new Properties();

        props.put("bootstrap.servers", "http://10.0.99.197:9092");//kafka集群
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("group.id", "test");

        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for (int i = 0; i < 10; i++){
            System.out.println(i);
            producer.send(new ProducerRecord<String, String>("topicTest", Integer.toString(i), Integer.toString(i)));

        }

        producer.close();
    }
    public static void main(String[] args){
        send();
    }
}
