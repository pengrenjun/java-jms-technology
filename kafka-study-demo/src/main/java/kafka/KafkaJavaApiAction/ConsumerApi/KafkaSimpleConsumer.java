package kafka.KafkaJavaApiAction.ConsumerApi;

import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 低级消费者SimpleConsumer API使用
 * 低级消费者 API 虽然应用起来较为复杂，但允许客户端对消息进行灵活的控制，因此，在
 * 实际开发应用中也颇受欢迎 以下几种常见应用场景通过低级 API 来实现则更为方便。
 *   》支持消息重复消费
 *   》添加事务管理机制，保证消息被处理且仅被处理一次
 *   》只消费指定分区或者指定分区的某些片段。
 * @Date 2019/5/31 0031 上午 9:27
 * @Created by Pengrenjun
 */
public class KafkaSimpleConsumer {

    private static final Logger logger= LoggerFactory.getLogger(KafkaSimpleConsumer.class);

    /**指定 Kafka 集群代理列表 列表无需指定所有的代理地址
     只要保证能连上 Kafka 集群即可， 一般建议多个节点时至少写两个节点的地址*/
    public static final String BROKER_LIST = "10.0.99.197,10.0.99.171" ;

    /** broker 的端口*/
    public static final int PORT = 9092;

    /**连接超时时间设置为一分钟*/
    public static final int TIME_OUT = 60 * 1000;

    /**设置读取消息缓冲区大小*/
    public static final int BUFFER_SIZE = 1024 * 1024;

    /**设置每次获取消息的条数*/
    public static final int FETCH_SIZE = 100000;

    /**设置容忍发生错误 重试的最大次数*/
    public static final int MAX_ERROR_NUM = 3 ;

    /**clientId*/
    public static final String clientId= System.getProperty("user.name");

    /**起始偏移量*/
    public static final Long earlestTime=kafka.api.OffsetRequest.EarliestTime();

    /**最大偏移量*/
    public static final Long latestTime=kafka.api.OffsetRequest.LatestTime();

    /**
     * 获取指定主题相应分区元数据信息
     * @param brokerHostList  集群brokers的连接地址
     * @param port            Kafka服务的端口号 默认为9092
     * @param topic           主题名称
     * @param partitionid     分区Id
     * @return
     */
    public static PartitionMetadata  fetchPartitionMetadata(List<String> brokerHostList , int port, String topic , int partitionid){

        //获取元数据信息的执行者
        SimpleConsumer simpleConsumer=null;

        //获取主题元数据信息的请求
        TopicMetadataRequest metadataRequest = null ;

        //获取主题元数据信息的请求响应
        TopicMetadataResponse metadataResp = null ;

        //获取主题元数据列表
        List<TopicMetadata> topicsMetadata = null ;

        //返回的元数据信息
        PartitionMetadata partitionMetadata=null;

        /**
         * 为防止只进行一次连接请求而得不到元数据信息 ，这里在实现 通过轮询多个broker节点，
         * 若与某个节点创建连接时发生异常，则继续尝试与下一个代理节点创建连接，
         * 直到请求成功或者轮询完所配置的代理节点
         */
        try {
            for(String brokerHost:brokerHostList){
                //1. 构造一个消费者用于获取元数据信息的执行者
                simpleConsumer=new SimpleConsumer(brokerHost , port , TIME_OUT, BUFFER_SIZE ,clientId );

                //2. 构造请求主题的元数据的request
                metadataRequest=new TopicMetadataRequest(Arrays.asList(topic));

                //3.发送获取主题元数据的请求
                try {
                    metadataResp = simpleConsumer.send(metadataRequest);
                } catch (Exception e) {
                    e.printStackTrace();//有可能无法与当前的broker连接
                    continue;//尝试使用其他broker进行连接
                }

                //4.获取主题元数据列表
                topicsMetadata = metadataResp.topicsMetadata();

               //5.主题元数据列表中提取指定分区的元数据信息
                for(TopicMetadata topicMetadata:topicsMetadata){

                    for(PartitionMetadata partionItem:topicMetadata.partitionsMetadata()){
                        if(partionItem.partitionId()!=partitionid){
                            continue;
                        }
                        else {
                            partitionMetadata=partionItem;
                            return partitionMetadata;//直接返回数据
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("simple获取分区元信息 异常:"+e.toString());
        } finally {

            if(simpleConsumer!=null){
                simpleConsumer.close();
            }
        }
        return partitionMetadata;
    }


    public static SimpleConsumer  instanceSimpleConsumer(){
        SimpleConsumer simpleConsumer=new SimpleConsumer(StringUtils.split(BROKER_LIST,",")[0], PORT , TIME_OUT, BUFFER_SIZE ,clientId );
        return simpleConsumer;
    }

    /**
     * 获取消息偏移量方法
     * @param consumer
     * @param topic
     * @param partition
     * @param beginTime
     * @param clientName
     * @return
     */
    public static long  getLastOffset(SimpleConsumer consumer, String topic , int partition, long beginTime , String clientName){

        TopicAndPartition topicAndPartition = new TopicAndPartition(topic ,partition);

        Map<TopicAndPartition , PartitionOffsetRequestInfo> requestinfo = new HashMap<TopicAndPartition , PartitionOffsetRequestInfo>();

        //设置获取消息起始 offset
        requestinfo.put(topicAndPartition, new PartitionOffsetRequestInfo(beginTime , 1)) ;

        //构造获取 offset 请求
        OffsetRequest request =new OffsetRequest (requestinfo , kafka.api.OffsetRequest.CurrentVersion () , clientName) ;

        //获取offset请求的响应
        OffsetResponse response= consumer.getOffsetsBefore(request);

        if(response.hasError()){

            logger.error(" Fetch last off set occurs exception : " + response.errorCode(topic,partition) ) ;
            logger.error(response.toString());
            return -1 ;
        }

        long [ ] offsets= response.offsets(topic , partition);

        if (null == offsets || offsets. length == 0) {
            logger.error(" Fetch last offset occurs error,offses is null " ) ;
            return -1;
          }


        return offsets[0];

    }

    /**
     * 消费主题topic在某分区的消息 每一次执行都可以重复获取到数据 Kafka消费数据之后不会删除数据 而是通过偏移量记录已消费的数据
     * 这个方法每次从offset=0开始进行消费 所以会重复的消费之前的数据
     * 在该方法 并没有对代理失效及发生异常进行相应的处理，只是简单尝试再次根据当前分区的
     * Leader节点信息实例化一个消费者 直到失败次数达到能够容忍的最大值时 程序退出。
     * @param brokerList
     * @param port
     * @param topic
     * @param partitionid
     */
    public static void consumePartitionRecord(List<String> brokerList, int port , String topic, int partitionid)
            throws InterruptedException, UnsupportedEncodingException {

        SimpleConsumer consumer = null;

        try {
            //1.首先获取指定分区的元数据信息
            PartitionMetadata metadata = fetchPartitionMetadata(brokerList, port, topic, partitionid);

            if (metadata == null) {
                logger.error("Can't find metadata info 获取不到指定分区的元数据信息！");
                return;
            }
            if (metadata.leader()== null){
                logger.error("Can’t find the partition:"+ partitionid + "’s leader .") ;
                return;
            }

            String leadBrokerHost = metadata.leader().host();

            String clientid ="client-" + topic + "-"+ partitionid;

            // 2.创建一个消息者作为消费消息的真正执行者
            consumer=new SimpleConsumer(leadBrokerHost, port , TIME_OUT, BUFFER_SIZE, clientid);

            //设置时间为 kafka.api OffsetRequest.EarliestTirne （）从最新消息起始处开始
            long lastOffset = getLastOffset(consumer, topic, partitionid, earlestTime, clientid);

            int errorNurn = 0;
            kafka.api.FetchRequest fetchRequest = null;
            FetchResponse fetchResponse = null;

            while (lastOffset > -1){
                //当在循环过程中出错时将起始实例化的 consumer 关闭并设置为 null
                if (consumer == null){
                    consumer=new SimpleConsumer(leadBrokerHost, port , TIME_OUT, BUFFER_SIZE, clientid);
                }
                //3.构造获取消息的request
                fetchRequest =new FetchRequestBuilder().clientId(clientid).addFetch(topic,
                        partitionid, lastOffset, FETCH_SIZE).build() ;

                //4. 获取响应并处理
                fetchResponse = consumer.fetch(fetchRequest) ;

                //获取数据发生错误
                if (fetchResponse.hasError()){//获取数据有误
                    errorNurn++;
                    if (errorNurn > MAX_ERROR_NUM){//达到发生错误的最大次数时退出循环
                        logger.error("获取数据错误次数>"+MAX_ERROR_NUM);
                        break;
                    }

                    //获取错误码
                    short errorCode = fetchResponse.errorCode(topic, partitionid);

                    //offset 已无效 因为在获取 lastOffset 时设置为从最早开始时间 若是这种错误码，
                    //我们再将时间设置为从 Latest Time （）开始查找
                    if (ErrorMapping.OffsetOutOfRangeCode() == errorCode){
                        lastOffset = getLastOffset(consumer, topic, partitionid,
                                latestTime, clientid);

                        continue;
                    } else if(ErrorMapping.OffsetsLoadInProgressCode() == errorCode){
                        Thread.sleep(30000);// 若是这种异常则让线程休眠 30s
                        continue;
                    } else {
                        //这里只是简单地关闭当前分区 Leader 信息实例化的 Consumer,并没有对代理失效时进行相应处理
                        consumer.close();
                        consumer = null;
                        continue;
                    }

                }

                //获取的数据没有错误 进行偏移量的管理和数据的处理
                else {
                    errorNurn=0;
                    long fetchNum = 0 ;
                    for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(topic, partitionid)){

                        long currentOffset = messageAndOffset.offset();
                        if (currentOffset < lastOffset){
                            logger.error ("Fetch an old offset :" + currentOffset + "expect the offset is greater than "+ lastOffset);
                            continue ;
                        }
                        lastOffset = messageAndOffset . nextOffset();
                        ByteBuffer payload= messageAndOffset.message().payload() ;
                        byte[] bytes= new byte[payload . limit()];
                        payload.get(bytes);

                        //简单打印出消息及消息 offset
                        System.out.println(" 获取到的message :" + (new String(bytes , "UTF-8")) + ", offset :"+
                                messageAndOffset.offset());

                        fetchNum++;

                    }

                    if (fetchNum == 0) {//如果还没有消息，则让线程阻塞几秒
                        Thread .sleep(1000);
                        return;
                    }
                }

            }
        } catch (InterruptedException e) {
            logger.error("消费端获取消费数据异常："+e.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {

            if(consumer!=null){
                consumer.close();
            }
        }
    }





}
