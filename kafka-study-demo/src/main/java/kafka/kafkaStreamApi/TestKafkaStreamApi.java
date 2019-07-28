package kafka.kafkaStreamApi;

import kafka.KafkaJavaApiAction.producerApi.KafkaProducerUtil;
import kafka.KafkaJavaApiAction.topicApi.TopicUtils;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.junit.Test;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * @Description 测试kafka stream api
 * @Date 2019/6/20 0020 下午 5:37
 * @Created by Pengrenjun
 */
public class TestKafkaStreamApi {

    private final String topicName="kafkaStream-test1";

    @Test
    public void creatTopic(){
        TopicUtils.createTopic(topicName,1,1, null);
    }

    @Test
    public void sendMessage(){

        KafkaProducerUtil<String,String> producerUtil=new KafkaProducerUtil<>();
        for(int i=0;i<10;i++){
            producerUtil.syncSendRecord(topicName,System.currentTimeMillis(),"kafka-stream",String.valueOf(i),null);
        }
    }

    @Test
    public void testKstreamPrint() throws InterruptedException {

        KafkaStreamApiUtils<String,String> kafkaStreamApiUtils=new KafkaStreamApiUtils<String,String>("appId123");

        // java.lang.NoClassDefFoundError: org/apache/kafka/common/metrics/Sensor$RecordingLevel
        // kafka的版本要与kafka stream的版本一致
        Map<String, Object> topicKstream = kafkaStreamApiUtils.getTopicKstream(topicName);

        KStream kStream=(KStream)topicKstream.get("KStream");

        KafkaStreams kafkaStreams=(KafkaStreams)topicKstream.get("KafkaStreams");
        kafkaStreams.start();

        //数据只能消费一次
        kStream.print();

        Thread.sleep(10000);
        kafkaStreams.close();

    }

    @Test
    public void testKTablePrint() throws InterruptedException {

        KafkaStreamApiUtils<String,String> kafkaStreamApiUtils=new KafkaStreamApiUtils<String,String>("appId123");

        Map<String, Object> topicKTable = kafkaStreamApiUtils.getTopicKTable(topicName,"kafkaStream-store");

        KTable kTable=(KTable)topicKTable.get("KTable");

        KafkaStreams kafkaStreams=(KafkaStreams)topicKTable.get("KafkaStreams");
        kafkaStreams.start();

        //数据只能消费一次
        kTable.print();

        Thread.sleep(10000);
        kafkaStreams.close();

    }
}
