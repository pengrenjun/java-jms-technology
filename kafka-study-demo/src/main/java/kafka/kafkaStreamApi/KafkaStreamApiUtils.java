package kafka.kafkaStreamApi;

import kafka.consumer.KafkaStream;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description Kafka Stream javaApi 工具类
 * @Date 2019/6/20 0020 下午 5:05
 * @Created by Pengrenjun
 */
public class KafkaStreamApiUtils<K,V> {

    // KafkaStreams对象配置
    private  Properties properties=new Properties();

    private final String bootstrapServers="10.0.99.197:9092,10.0.99.191:9092";

    private KStreamBuilder streamsBuilder=new KStreamBuilder();

    /**
     * @param kafkaStreamAppId 指定的流处理应用的 id
     */
    public KafkaStreamApiUtils(String kafkaStreamAppId) {
        //指定流处理应用的 id,该配置必须指定
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,kafkaStreamAppId);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        //key序列化与反序列化
        properties.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //value序列化与反序列化
        properties.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());
    }

    /**
     * 构造某一个主题的记录流Kstream
     * @param topicId 主题id
     * @return
     */
    public Map<String,Object>  getTopicKstream(String topicId){
        // 也可以同时获取多个topic的记录流
        // public synchronized <K, V> KStream<K, V> stream(final Collection<String> topics) {
        //        return stream(topics, Consumed.<K, V>with(null, null, null, null));
        //    }

        KStream<K,V> kvkStream=streamsBuilder.stream(topicId);
        //KafkaStreams 建立与topic的数据传输连接
        KafkaStreams kvKafkaStreams=new KafkaStreams(streamsBuilder,properties);
        HashMap hashMap=new HashMap();
        hashMap.put("KStream",kvkStream);
        hashMap.put("KafkaStreams",kvKafkaStreams);
        return  hashMap;
    }

    /**
     * 创建某一个主题的日志记录流 key值对应的value是最新的
     * @param topicId
     * @param storeName the state store name used if this KTable is materialized, can be null if materialization not expected
     * @return
     */
    public Map<String,Object>  getTopicKTable(String topicId,String storeName){

        KTable<Object, Object> table = streamsBuilder.table(topicId, storeName);
        //KafkaStreams 建立与topic的数据传输连接
        KafkaStreams kvKafkaStreams=new KafkaStreams(streamsBuilder,properties);
        HashMap hashMap=new HashMap();
        hashMap.put("KTable",table);
        hashMap.put("KafkaStreams",kvKafkaStreams);
        return  hashMap;
    }



}
