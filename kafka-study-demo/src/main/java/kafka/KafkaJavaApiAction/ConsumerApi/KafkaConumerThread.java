package kafka.KafkaJavaApiAction.ConsumerApi;

import kafka.properties.KafkaConsumerPropertiesUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * 消费者消费线程
 * 一个消费组的消费者 若消费者的个数<=分区数 则消费者线程可以满足同时工作 若消费者线程数>分区数 则有空闲的消费线程
 * 一般的设计是一个分区对应一个消费则 分区的最小的划分单元 若多个消费者同时消费同一个分区的数据会有偏移量的问题
 */
public class KafkaConumerThread implements Runnable {

    //每个线程拥有私有的consumer实例
    private KafkaConsumer kafkaConsumer;

    //消费者的配置信息
    private Properties properties;

    //消费的主题
    private List<String> topics;

    public KafkaConumerThread(Properties properties, List<String>  topics) {
        this.properties = properties;
        this.topics = topics;
        //每个线程实例化各自的consumer
        Properties props=KafkaConsumerPropertiesUtils.getProducerProperties(properties);
        KafkaConsumerUtil<String,String> kafkaConsumerUtil=new KafkaConsumerUtil<>();
        this.kafkaConsumer=kafkaConsumerUtil.instanceKafkaConsumer(props);
        //订阅主题
        kafkaConsumer.subscribe(topics);
    }

    @Override
    public void run() {

        try {
            while(true){

                ConsumerRecords<String ,  String> records= kafkaConsumer.poll(1000);
                for  (ConsumerRecord<String ,  String> record  :  records){
                    System.out.println("消费者线程"+Thread.currentThread().getName()+" 获取到的数据："+
                    record.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(Objects.nonNull(kafkaConsumer)){
                kafkaConsumer.close();
            }

        }

    }
}
