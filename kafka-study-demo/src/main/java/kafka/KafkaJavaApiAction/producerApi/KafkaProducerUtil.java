package kafka.KafkaJavaApiAction.producerApi;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import kafka.properties.KafkaProducerPropertiesUtils;
import org.apache.kafka.clients.producer.*;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Description Kafka生产者工具类 发送数据 K:消息key的数据类型 V：消息value的数据类型
 * @Date 2019/5/30 0030 下午 3:59
 * @Created by Pengrenjun
 */
public class KafkaProducerUtil<K, V> {


    /**
     * 异步方式发送数据
     * @param topicName
     * @param timestamp
     * @param key
     * @param value
     * @param producerProperties
     */
    public  void syncSendRecord(@NotNull String topicName, Long timestamp, K key, V value,@Nullable  Properties producerProperties){

//        //topic不存在 则默认创建一个topic
//        if(!TopicUtils.isTopicExist(topicName)){
//            TopicUtils.createTopic(topicName,2,1,null);
//        }

        Properties properties= KafkaProducerPropertiesUtils.getProducerProperties(producerProperties);

        Producer<K, V> producer = null;

        try {
            producer = new KafkaProducer<K, V>(properties);

            ProducerRecord<K, V> producerRecord = new ProducerRecord<K, V>(topicName, null, timestamp, key, value);

            //采用默认的异步发送
            Future<RecordMetadata> sendResult = producer.send(producerRecord);

            //通过get 阻塞线程 即为同步操作
//            RecordMetadata recordMetadata = sendResult.get();
//
//            System.out.println(recordMetadata);


            System.out.println("k:"+key+" v:"+value+" 发送OK!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }


    /**
     * 异步方式发送数据(带有回调处理)
     * @param topicName
     * @param timestamp
     * @param key
     * @param value
     * @param producerProperties
     */
    public  void syncCallBackSendRecord(@NotNull String topicName, Long timestamp, K key, V value,
                                        Callback callback,  @Nullable  Properties producerProperties){

//        //topic不存在 则默认创建一个topic
//        if(!TopicUtils.isTopicExist(topicName)){
//            TopicUtils.createTopic(topicName,2,1,null);
//        }

        Properties properties= KafkaProducerPropertiesUtils.getProducerProperties(producerProperties);

        Producer<K, V> producer = null;

        try {
            producer = new KafkaProducer<K, V>(properties);

            ProducerRecord<K, V> producerRecord = new ProducerRecord<K, V>(topicName, null, timestamp, key, value);

            producer.send(producerRecord,callback);

            System.out.println("k:"+key+" v:"+value+" 发送OK!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

    /**
     * 异步方式发送数据(带有回调处理) 启用多线程发送数据
     * @param topicName
     * @param timestamp
     * @param key
     * @param value
     * @param producerProperties
     */
    public  void syncThreadSendRecord(@NotNull String topicName, Long timestamp, K key, V value,
                                        Callback callback, ExecutorService executorService, @Nullable  Properties producerProperties){

//        //topic不存在 则默认创建一个topic
//        if(!TopicUtils.isTopicExist(topicName)){
//            TopicUtils.createTopic(topicName,2,1,null);
//        }

        Properties properties= KafkaProducerPropertiesUtils.getProducerProperties(producerProperties);

        Producer<K, V> producer = null;

        try {
            producer = new KafkaProducer<K, V>(properties);

            ProducerRecord<K, V> producerRecord = new ProducerRecord<K, V>(topicName, null, timestamp, key, value);


            ProducerSendThread producerSendThread=new ProducerSendThread(producer,producerRecord,callback);

            executorService.submit(producerSendThread);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            //多线程发送数据 不在这个地方关闭连接 在线程执行之后关闭连接
            //producer.close();
        }
    }


}
