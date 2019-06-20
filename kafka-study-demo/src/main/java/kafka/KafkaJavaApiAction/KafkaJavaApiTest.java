package kafka.KafkaJavaApiAction;

import kafka.KafkaJavaApiAction.ConsumerApi.ConsumerRebalanceListenerImpl;
import kafka.KafkaJavaApiAction.ConsumerApi.KafkaConsumerUtil;
import kafka.KafkaJavaApiAction.ConsumerApi.KafkaConumerThread;
import kafka.KafkaJavaApiAction.ConsumerApi.KafkaSimpleConsumer;
import kafka.KafkaJavaApiAction.producerApi.KafkaProducerUtil;
import kafka.KafkaJavaApiAction.producerApi.ProducerSyncSendCallBackImpl;
import kafka.KafkaJavaApiAction.topicApi.TopicUtils;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.properties.KafkaConsumerPropertiesUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
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

//        Properties properties=new Properties();
//        properties.put("bootstrap.servers","10.10.30.248:30299");

        TopicUtils.createTopic("testkafka",1,1, null);
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

            kafkaProduceUtil.syncSendRecord("testkafka",stockQuotationlnfo.getTradeTime(),
                    stockQuotationlnfo.getStockCode(),stockQuotationlnfo.toString(),null);
        }

    }


    @Test
    public void syncCallBackSendRecord(){


        for(int i=0;i<10;i++){
            //模拟获取股票信息
            StockQuotationlnfo stockQuotationlnfo=StockQuotationlnfo.imitateCreateQuotationinfo();

            KafkaProducerUtil<String,String> kafkaProduceUtil=new KafkaProducerUtil();

            kafkaProduceUtil.syncCallBackSendRecord("topicAD",stockQuotationlnfo.getTradeTime(),
                    stockQuotationlnfo.getStockCode(),stockQuotationlnfo.toString(),new ProducerSyncSendCallBackImpl(),null);
        }

    }

    @Test
    public void syncThreadSendRecord() throws InterruptedException {

        ExecutorService executorService=Executors.newCachedThreadPool();


        for(int i=0;i<3;i++){
            //模拟获取股票信息
            StockQuotationlnfo stockQuotationlnfo=StockQuotationlnfo.imitateCreateQuotationinfo();

            KafkaProducerUtil<String,String> kafkaProduceUtil=new KafkaProducerUtil();

            kafkaProduceUtil.syncThreadSendRecord("stock-quotation",stockQuotationlnfo.getTradeTime(),
                    stockQuotationlnfo.getStockCode(),stockQuotationlnfo.toString(),new ProducerSyncSendCallBackImpl(),executorService,null);
        }

        Thread.sleep(10000);

      }

    /**
     * 测试获取分区元信息
     */
     @Test
      public void testFetchPartitionMetadate(){

         PartitionMetadata partitionMetadata = KafkaSimpleConsumer.fetchPartitionMetadata(
                 Arrays.asList(StringUtils.split(KafkaSimpleConsumer.BROKER_LIST, ",")),
                 KafkaSimpleConsumer.PORT, "stock-quotation", 1);

         System.out.println(partitionMetadata);
     }

    /**
     * 获取偏移量
     */
    @Test
     public void testGetOffset(){
         //获取197里面分区副本记录的偏移量
         SimpleConsumer simpleConsumer=KafkaSimpleConsumer.instanceSimpleConsumer();

          //如果要查询的partion位于177上，但是连接的host是197 会报错
         // Fetch last off set occurs exception : 3  error: org.apache.kafka.common.errors.UnknownTopicOrPartitionException
         long lastOffset = KafkaSimpleConsumer.getLastOffset(simpleConsumer, "stock-quotation", 0,
                 KafkaSimpleConsumer.latestTime, KafkaSimpleConsumer.clientId);

         System.out.println(lastOffset);

     }

     @Test
     public void testConsume() throws UnsupportedEncodingException, InterruptedException {

        KafkaSimpleConsumer.consumePartitionRecord(Arrays.asList(StringUtils.split(KafkaSimpleConsumer.BROKER_LIST,",")),
                KafkaSimpleConsumer.PORT,"topicAD",1);

     }

     @Test
    public void testInstanceKafkaConsumer(){
         KafkaConsumerUtil<String,String> kafkaConsumerUtil=new KafkaConsumerUtil<>();

         KafkaConsumer<String, String> stringStringKafkaConsumer =
                 kafkaConsumerUtil.instanceKafkaConsumer(null);

         System.out.println(stringStringKafkaConsumer);
     }

     @Test
     public void testSubscribeTopic() throws InterruptedException {

         KafkaConsumerUtil<String,String> kafkaConsumerUtil=new KafkaConsumerUtil<>();

         KafkaConsumer<String, String> kafkaConsumer =
                 kafkaConsumerUtil.instanceKafkaConsumer(null);

         List<String> toplicList=new ArrayList<>();

         toplicList.add("stock-quotation");

         ConsumerRebalanceListener consumerRebalanceListener=new ConsumerRebalanceListenerImpl(kafkaConsumer);

         kafkaConsumerUtil.subscribTopic(toplicList,kafkaConsumer,consumerRebalanceListener);

     }

     @Test
     public void testAssignTopics(){

         List<TopicPartition> topicPartitions=new ArrayList<>();

         TopicPartition topicPartition=new TopicPartition("stock-quotation",0);

         topicPartitions.add(topicPartition);

         KafkaConsumerUtil<String,String> kafkaConsumerUtil=new KafkaConsumerUtil<>();

         KafkaConsumer<String, String> kafkaConsumer =
                 kafkaConsumerUtil.instanceKafkaConsumer(null);

         kafkaConsumerUtil.assignTopicPartitions(topicPartitions,kafkaConsumer);

     }

     @Test
     public void testPoll() throws InterruptedException {

         KafkaConsumerUtil<String,String> kafkaConsumerUtil=new KafkaConsumerUtil<>();

         KafkaConsumer<String, String> kafkaConsumer =
                 kafkaConsumerUtil.instanceKafkaConsumer(null);

         List<String> toplicList=new ArrayList<>();

         toplicList.add("topicAD");

         ConsumerRebalanceListener consumerRebalanceListener=new ConsumerRebalanceListenerImpl(kafkaConsumer);

         //订阅主题
         kafkaConsumerUtil.subscribTopic(toplicList,kafkaConsumer,consumerRebalanceListener);

         //长轮询拉取消息
         while(true){
             //poll消费数据
             kafkaConsumerUtil.pollRecordAndAutoManageOffset(kafkaConsumer,3000);
         }
     }

    /**
     * poll获取消息并且结合业务逻辑手动提交偏移量 异步回调偏移量的提交结果
     */
    @Test
    public void testPollRecordAndCommitAsyn() throws InterruptedException {

        //TopicUtils.createTopic("topicAD",2,1, null);

        KafkaConsumerUtil<String,String> kafkaConsumerUtil=new KafkaConsumerUtil<>();

        Properties properties=new Properties();
        //consumer开启手动提交偏移量的方式
        properties.put("enable.auto.commit",false);

        KafkaConsumer<String, String> kafkaConsumer = kafkaConsumerUtil.instanceKafkaConsumer(properties);

        //订阅主题
        kafkaConsumer.subscribe(Arrays.asList("topicAD"));


        //计数器
        int count=0;

        //最少10条数据处理成功后 手动提交偏移量
        //轮询获取生产段的消息
        while(true){


            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(2000);

            for(ConsumerRecord consumerRecord:consumerRecords){

                //模拟业务处理
                System.out.println("poll->获取到的信息:"+consumerRecord.toString() +" 进行业务处理1");
                Thread.sleep(1000);
                System.out.println("poll->获取到的信息:"+consumerRecord.toString() +" 进行业务处理2");
                Thread.sleep(1000);
                count++;
            }

            //处理了10条数据之后 手动提交偏移量（异步方式）
            if(count>=10){

                kafkaConsumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {

                        if(Objects.nonNull(exception)){
                            System.out.println("手动异步提交偏移量 异常："+exception.getMessage());
                        }
                        else {

                            System.out.println("手动提交偏移量ok:"+offsets.toString());

                        }
                    }
                });
            }
        }
    }

    @Test
    public void testTimetoSearchRecords(){
        KafkaConsumerUtil<String,String> kafkaConsumerUtil=new KafkaConsumerUtil<>();

        KafkaConsumer<String,String> kafkaConsumer=kafkaConsumerUtil.instanceKafkaConsumer(null);

        //待查询的主题某些分区的数据
        List<TopicPartition> topicPartitions=new ArrayList<>();

        TopicPartition topicPartition1=new TopicPartition("topicAD",0);
        TopicPartition topicPartition2=new TopicPartition("topicAD",1);

        topicPartitions.add(topicPartition1);
        topicPartitions.add(topicPartition2);

        //获取topicAD 0，1两个分区12小时之前的数据
        ConsumerRecords<String,String> consumerRecords = kafkaConsumerUtil.timeStampsToSearchRecords(kafkaConsumer, topicPartitions, 12 * 3600 * 1000);

        for(ConsumerRecord consumerRecord:consumerRecords){
            System.out.println(""+consumerRecord.toString());
        }

    }

    /**
     * 多线程消费者消费数据
     */
    @Test
    public void testConsumerThreads() throws InterruptedException {
        List<String> topics=Arrays.asList("kafkaStream-test1");

        //topicAD共有2个分区 这里就用两个线程消费数据
        ExecutorService service=Executors.newFixedThreadPool(2);

        Properties propertie=KafkaConsumerPropertiesUtils.getProducerProperties(null);


        //启动两个消费线程 每个分区由固定的线程进行数据处理
        service.submit(new KafkaConumerThread(propertie,topics));

        service.submit(new KafkaConumerThread(propertie,topics));

        Thread.sleep(100000);

    }

}
