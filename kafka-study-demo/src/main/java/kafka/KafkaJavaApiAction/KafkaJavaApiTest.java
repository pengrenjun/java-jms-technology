package kafka.KafkaJavaApiAction;

import kafka.KafkaJavaApiAction.producerApi.KafkaProducerUtil;
import kafka.KafkaJavaApiAction.producerApi.ProducerSyncSendCallBackImpl;
import kafka.KafkaJavaApiAction.topicApi.TopicUtils;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description Kafka java api测试
 * @Date 2019/5/29 0029 下午 5:23
 * @Created by Pengrenjun
 */
public class KafkaJavaApiTest {

    //创建主题
    @Test
    public void testCreateTopic(){

       TopicUtils.createTopic("stock-quotation",2,1, null);
    }

    //修改主题的配置信息
    @Test
    public void modifyTopicProperties(){
        Properties topicAC = TopicUtils.getTopicProperties("topicAC");
        System.out.println(topicAC);

        Properties properties=new Properties();
        properties.put("max.message.bytes","404800");

        TopicUtils.modifyTopicConfig("topicAC",properties);

        Properties modifytopicAC = TopicUtils.getTopicProperties("topicAC");
        System.out.println(modifytopicAC);

    }
    @Test
    public void addPartitions(){

        //TopicUtils.createTopic("topicAD",2,2, null);

        //AD:原先设定了两个分区 每个分区有连个副本 现在修改为 增加两个分区共4个分区
        // 新增的两个分区的对应各自副本分别放在broker1 ,2上面
        TopicUtils.addPartitions("topicAD",4,"1:2,1:2,1:2,1:2");

        /**
         *分区0，1分配到了broker1,2上面 新增的分区2，3分配到了broker1上面 各自分区的副本replica分配到broker1,2上面
         Partition	Replicas
         0		1	(1,2)
         1		2	(2,1)
         2		1	(1,2)
         3		1	(1,2)
         */
    }

    @Test
    public void creatOrUpdateTopicPartitionAndReplica(){
       // TopicUtils.createTopic("topicAE",2,2, null);

        // 分区的的副本数量<=brokers的数量
        //org.apache.kafka.common.errors.InvalidReplicationFactorException:
        // replication factor: 4 larger than available brokers: 2
        //TopicUtils.creatOrUpdateTopicPartitionAndReplica("topicAE",6,4);
        TopicUtils.creatOrUpdateTopicPartitionAndReplica("topicAE",6,1);
    }

    @Test
    public void testKafkaSendRecord(){

        for(int i=0;i<10;i++){
            //模拟获取股票信息
            StockQuotationlnfo stockQuotationlnfo=StockQuotationlnfo.imitateCreateQuotationinfo();

            KafkaProducerUtil<String,String> kafkaProduceUtil=new KafkaProducerUtil();

            kafkaProduceUtil.syncSendRecord("stock-quotation",stockQuotationlnfo.getTradeTime(),
                    stockQuotationlnfo.getStockCode(),stockQuotationlnfo.toString(),null);
        }

    }


    @Test
    public void syncCallBackSendRecord(){


        for(int i=0;i<10;i++){
            //模拟获取股票信息
            StockQuotationlnfo stockQuotationlnfo=StockQuotationlnfo.imitateCreateQuotationinfo();

            KafkaProducerUtil<String,String> kafkaProduceUtil=new KafkaProducerUtil();

            kafkaProduceUtil.syncCallBackSendRecord("stock-quotation",stockQuotationlnfo.getTradeTime(),
                    stockQuotationlnfo.getStockCode(),stockQuotationlnfo.toString(),new ProducerSyncSendCallBackImpl(),null);
        }

    }

    @Test
    public void syncThreadSendRecord() throws InterruptedException {

        ExecutorService executorService=Executors.newCachedThreadPool();


        for(int i=0;i<10;i++){
            //模拟获取股票信息
            StockQuotationlnfo stockQuotationlnfo=StockQuotationlnfo.imitateCreateQuotationinfo();

            KafkaProducerUtil<String,String> kafkaProduceUtil=new KafkaProducerUtil();

            kafkaProduceUtil.syncThreadSendRecord("stock-quotation",stockQuotationlnfo.getTradeTime(),
                    stockQuotationlnfo.getStockCode(),stockQuotationlnfo.toString(),new ProducerSyncSendCallBackImpl(),executorService,null);
        }

        Thread.sleep(10000);

      }

    }
