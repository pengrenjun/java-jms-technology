package kafka.properties;

import kafka.ProductAndConsumerTest.PatitionersImpl.HashPartitioner;
import kafka.ProductAndConsumerTest.PatitionersImpl.RoundRobinPartitioner;

import java.util.Properties;

/**
 * @Description Kafka生产者配置信息工具类
 * @Date 2019/5/24 0024 上午 11:30
 * @Created by Pengrenjun
 */
public class KafkaProducerPropertiesUtils {

    /**
     * 获取发送方的配置文件
     * @return
     */
    public static Properties getProducerProperties(Properties customerProperties){

        Properties props = new Properties();


        /**
         * 	high list
         * 	这是一个用于建立初始连接到kafka集群的"主机/端口对"配置列表。
         *   不论这个参数配置了哪些服务器来初始化连接，客户端都是会均衡地与集群中的所有服务器建立连接。
         *   —配置的服务器清单仅用于初始化连接，以便找到集群中的所有服务器。
         *   配置格式： host1:port1,host2:port2,.... 由于这些主机是用于初始化连接，以获得整个集群（集群是会动态变化的），
         *    因此这个配置清单不需要包含整个集群的服务器。（当然，为了避免单节点风险，这个清单最好配置多台主机）
         */
        props.put("bootstrap.servers", "10.0.99.197:9092,10.0.99.171:9092");//kafka集群
        /**
         * high [all, -1, 0, 1] default:1
         * 此配置是 Producer 在确认一个请求发送完成之前需要收到的反馈信息的数量。 这个参数是为了保证发送请求的可靠性
         * acks=0 如果设置为0，则 producer 不会等待服务器的反馈。该消息会被立刻添加到 socket buffer 中并认为已经发送完成。在这种情况下，服务器是否收到请求是没法保证的，并且参数retries也不会生效（因为客户端无法获得失败信息）。每个记录返回的 offset 总是被设置为-1。
         * acks=1 如果设置为1，leader节点会将记录写入本地日志，并且在所有 follower 节点反馈之前就先确认成功。在这种情况下，如果 leader 节点在接收记录之后，并且在 follower 节点复制数据完成之前产生错误，则这条记录会丢失。
         * acks=all 如果设置为all，这就意味着 leader 节点会等待所有同步中的副本确认之后再确认这条记录是否发送完成。只要至少有一个同步副本存在，记录就不会丢失。这种方式是对请求传递的最有效保证。acks=-1与acks=all是等效的
         */
        props.put("acks", "all");

        /**
         * high [0,...,2147483647] default：0
         * 若设置大于0的值，则客户端会将发送失败的记录重新发送，尽管这些记录有可能是暂时性的错误。
         * 请注意，这种 retry 与客户端收到错误信息之后重新发送记录并无区别。
         * 允许 retries 并且没有设置max.in.flight.requests.per.connection 为1时，记录的顺序可能会被改变。
         * 比如：当两个批次都被发送到同一个 partition ，第一个批次发生错误并发生 retries 而第二个批次已经成功，
         * 则第二个批次的记录就会先于第一个批次出现
         */
        props.put("retries", 0);

        /**
         * high
         * 关键字的序列化类，实现以下接口： org.apache.kafka.common.serialization.Serializer 接口
         */
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        /**
         * high
         * 值的序列化类，实现以下接口： org.apache.kafka.common.serialization.Serializer 接口
         */
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        /**
         * high  [0,...]  default:33554432
         * Producer 用来缓冲等待被发送到服务器的记录的总字节数。
         * 如果记录发送的速度比发送到服务器的速度快， Producer 就会阻塞，
         * 如果阻塞的时间超过 max.block.ms 配置的时长 默认：60000，则会抛出一个异常。
         * 这个配置与 Producer 的可用总内存有一定的对应关系，但并不是完全等价的关系，因为 Producer 的可用内存并不是全部都用来缓存。
         * 一些额外的内存可能会用于压缩(如果启用了压缩)，以及维护正在运行的请求
         */
        props.put("buffer.memory", 33554432);
        /**
         * medium  [0,...] default:16384
         * 当将多个记录被发送到同一个分区时， Producer 将尝试将记录组合到更少的请求中。
         * 这有助于提升客户端和服务器端的性能。这个配置控制一个批次的默认大小（以字节为单位）。
         * 当记录的大小超过了配置的字节数， Producer 将不再尝试往批次增加记录。
         *
         * 发送到 broker 的请求会包含多个批次的数据，每个批次对应一个 partition 的可用数据
         *
         * 小的 batch.size 将减少批处理，并且可能会降低吞吐量(如果 batch.size = 0的话将完全禁用批处理)。
         * 很大的 batch.size 可能造成内存浪费，因为我们一般会在 batch.size 的基础上分配一部分缓存以应付额外的记录
         *
         */
        props.put("batch.size", 16384);
        /**
         * medium  [0,...]  default:0
         * producer 会将两个请求发送时间间隔内到达的记录合并到一个单独的批处理请求中。
         * 通常只有当记录到达的速度超过了发送的速度时才会出现这种情况。
         * 然而，在某些场景下，即使处于可接受的负载下，客户端也希望能减少请求的数量。
         * 这个设置是通过添加少量的人为延迟来实现的&mdash；
         * 即，与其立即发送记录， producer 将等待给定的延迟时间，以便将在等待过程中到达的其他记录能合并到本批次的处理中。
         * 这可以认为是与 TCP 中的 Nagle 算法类似。
         * 这个设置为批处理的延迟提供了上限:一旦我们接受到记录超过了分区的 batch.size ，Producer 会忽略这个参数，立刻发送数据。
         * 但是如果累积的字节数少于 batch.size ，那么我们将在指定的时间内“逗留”(linger)，以等待更多的记录出现。
         * 这个设置默认为0(即没有延迟)。例如：如果设置linger.ms=5 ，则发送的请求会减少并降低部分负载，但同时会增加5毫秒的延迟
         */
        props.put("linger.ms", 1);

        /**
         * medium default：org.apache.kafka.clients.producer.internals.DefaultPartitioner
         * 指定计算分区的类 分发策略 如何将数据存放如不同的分区的算法
         * 为了更好的实现负载均衡和消息的顺序性，Kafka Producer可以通过分发策略发送给指定的Partition
         * 实现 org.apache.kafka.clients.producer.Partitioner 接口。
         */
        props.put("partitioner.class", HashPartitioner.class);

        //添加覆盖配置的参数
        if(customerProperties!=null){
            props.putAll(customerProperties);
        }

        return  props;

    }
}
