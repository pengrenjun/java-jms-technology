package kafka.ProductAndConsumerTest.PatitionersImpl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import kafka.utils.VerifiableProperties;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

public class RoundRobinPartitioner implements Partitioner {
  
  private static AtomicLong next = new AtomicLong();


  //只需要重写partition方法实现我们自己的分区逻辑

  /**
   * 实现按照分区的数量平均按次序进行消息发送
   * @param topic
   * @param key
   * @param bytes
   * @param value
   * @param bytes1
   * @param cluster
   * @return
   */
  @Override
  public int partition(String topic, Object key, byte[] bytes, Object value, byte[] bytes1, Cluster cluster) {
    //从clusten中获取这个topicn分区信息，在此我门建立了一个partition为5的topic
    List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
    //获取分区总个数
    int numPartitions = partitions.size();
    long nextIndex = next.incrementAndGet();

    return (int)nextIndex % numPartitions;
  }

  @Override
  public void close() {

  }

  @Override
  public void configure(Map<String, ?> map) {

  }
}


