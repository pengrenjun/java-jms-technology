package kafka.properties;

import kafka.ProductAndConsumerTest.PatitionersImpl.HashPartitioner;

import java.util.Properties;

/**
 * @Description Kafka消费者配置信息工具类
 * @Date 2019/5/24 0024 上午 11:30
 * @Created by Pengrenjun
 */
public class KafkaConsumerPropertiesUtils {

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
         * high
         * 关键字key的反序列化类，实现以下接口： org.apache.kafka.common.serialization.StringDeserializer 接口
         */
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        /**
         * high
         * 值的反序列化类，实现以下接口： org.apache.kafka.common.serialization.StringDeserializer 接口
         */
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        /**
         * high
         * 消费者所属的消费组
         */
        props.put("group.id","KafkaConsumer-test");

        /**
         * medium default:true
         * 是否自动提交消费的偏移量
         */
        props.put("enable.auto.commit",true);

        /**
         *自动提交偏移量的间隔时间 默认：5000
         */
        props.put("auto.commit.interval.ms",5000);

        /**
         * 客户端标识id
         */
        props.put("client.id",System.getProperty("user.name"));


        //添加覆盖配置的参数
        if(customerProperties!=null){
            props.putAll(customerProperties);
        }

        return  props;

    }
}
