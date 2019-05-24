package kafka.ProductAndConsumerTest.PatitionersImpl;

import kafka.utils.VerifiableProperties;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

public class HashPartitioner implements Partitioner {


 /* @Override
  public int partition(Object key, int numPartitions) {
    if ((key instanceof Integer)) {
      return Math.abs(Integer.parseInt(key.toString())) % numPartitions;
    }
    return Math.abs(key.hashCode() % numPartitions);
  }*/

  /**
   * Hash算法 相同Key的topic放入同一个分区
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

    if ((key instanceof Integer)) {
      return Math.abs(Integer.parseInt(key.toString())) % numPartitions;
    }
    return Math.abs(key.hashCode() % numPartitions);
  }

  @Override
  public void close() {

  }

  @Override
  public void configure(Map<String, ?> map) {

  }
}


