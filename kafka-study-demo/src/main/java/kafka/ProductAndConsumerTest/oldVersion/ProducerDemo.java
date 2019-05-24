package kafka.ProductAndConsumerTest.oldVersion;

import java.util.Properties;

import kafka.ProductAndConsumerTest.PatitionersImpl.HashPartitioner;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

/**
 * 基于0.8.1版本的写法
 */
public class ProducerDemo {

  static private final String TOPIC = "test1";
  static private final String ZOOKEEPER = "10.0.99.197:2181";
  static private final String BROKER_LIST = "10.0.99.197:9092";
//  static private final int PARTITIONS = TopicAdmin.partitionNum(ZOOKEEPER, TOPIC);
  static private final int PARTITIONS = 3;



  public static void main(String[] args) throws Exception {
    Producer<String, String> producer = initProducer();
    sendOne(producer, TOPIC);
  }

  /**
   * 生产者支持泛型 指定消息的key-value的类型
   * @return
   */
  private static Producer<String, String> initProducer() {
    Properties props = new Properties();
    //指定Broker（kafka服务地址） 单机测试可以为一个即可
    props.put("metadata.broker.list", BROKER_LIST);
    // props.put("serializer.class", "kafka.serializer.StringEncoder");
    //指定如何将消息装换为字节数组的方式
    props.put("serializer.class", StringEncoder.class.getName());
    props.put("partitioner.class", HashPartitioner.class.getName());//Key相同的数据会发送到相同的Partition
    // props.put("partitioner.class", "kafka.producer.DefaultPartitioner");
//    props.put("compression.codec", "0");
    //发送类型：sync 同步 async 异步
    props.put("producer.type", "async");
    //后面这几个参数是异步发送需要配置的参数 同步发送不需要配置
    props.put("batch.num.messages", "3");
    props.put("queue.buffer.max.ms", "10000000");
    props.put("queue.buffering.max.messages", "1000000");
    props.put("queue.enqueue.timeout.ms", "20000000");

    ProducerConfig config = new ProducerConfig(props);
    Producer<String, String> producer = new Producer<String, String>(config);
    return producer;
  }

  public static void sendOne(Producer<String, String> producer, String topic) throws InterruptedException {
    KeyedMessage<String, String> message1 = new KeyedMessage<String, String>(topic, "31", "test 31");
    producer.send(message1);
    //Thread.sleep(5000);
    KeyedMessage<String, String> message2 = new KeyedMessage<String, String>(topic, "31", "test 32");
    producer.send(message2);
    //Thread.sleep(5000);
    KeyedMessage<String, String> message3 = new KeyedMessage<String, String>(topic, "31", "test 33");
    producer.send(message3);
    //Thread.sleep(5000);
    KeyedMessage<String, String> message4 = new KeyedMessage<String, String>(topic, "31", "test 34");
    producer.send(message4);
    //Thread.sleep(5000);
    KeyedMessage<String, String> message5 = new KeyedMessage<String, String>(topic, "31", "test 35");
    producer.send(message5);
    //Thread.sleep(5000);
    producer.close();

    System.out.println("发送OK!");
  }

}
