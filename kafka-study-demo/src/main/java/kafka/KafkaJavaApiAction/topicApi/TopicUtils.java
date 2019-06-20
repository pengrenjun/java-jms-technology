package kafka.KafkaJavaApiAction.topicApi;

import com.google.common.base.Preconditions;
import kafka.admin.AdminUtils;
import kafka.admin.BrokerMetadata;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import org.apache.kafka.common.security.JaasUtils;
import org.springframework.util.ObjectUtils;
import scala.collection.Map;
import scala.collection.Seq;

import java.util.Objects;
import java.util.Properties;

/**
 * @Description Kafka主题管理工具类
 * @Date 2019/5/29 0029 下午 5:07
 * @Created by Pengrenjun
 */
public class TopicUtils {

    /**连接的zookeeper集群地址*/
    private static final String ZK_CONNECT ="10.0.99.197:2181,10.0.99.171:2181,10.0.99.177:2181";
    /**连接zookeeper的session过期时间*/
    private static final int SESSION_TIMEOUT =10000 ;
    /**连接zookeeper的session超时时间*/
    private static final int CONNECT_TIMEOUT =10000 ;


    private static ZkUtils getZkUtils() {
        return ZkUtils.apply(ZK_CONNECT, SESSION_TIMEOUT, CONNECT_TIMEOUT, JaasUtils.isZkSecurityEnabled());
    }

    /**
     * 创建主题 zookeeper注册元信息
     * @param topic      主题的名称
     * @param partition  分区数
     * @param repilca    副本数
     * @param properties 配置信息
     */
    public static void createTopic(String topic, int partition, int repilca, Properties properties) {

        ZkUtils zkUtils=getZkUtils();
        try {

            //主题是否存在
            if(!isTopicExist(topic)){

                AdminUtils.createTopic(zkUtils,topic,partition,repilca,
                        Objects.isNull(properties)?AdminUtils.createTopic$default$5():properties,
                        AdminUtils.createTopic$default$6());

                System.out.println("topic:"+topic+"创建OK！");
            }
            else {
                System.out.println("topic:"+topic+"已经存在！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
                zkUtils.close();
     }
    }

    /**
     * 校验主题是否存在
     * @param topic
     * @return
     */
    public static boolean isTopicExist(String topic) {
        ZkUtils zkUtils=getZkUtils();
        return AdminUtils.topicExists(zkUtils,topic);
    }

    /**
     * 修改主题级别配置
     * @param topic      topic 名称
     * @param properties 配置信息
     */
    public static void modifyTopicConfig(String topic,Properties properties){

        ZkUtils zkUtils=getZkUtils();
        try {
            //获取当前主题已有的配置 这里是查询主题级别的配置，因此指定配置类型为 Topic
            Properties curProp = getTopicProperties(topic);
            // 添加新修改的配置
            curProp.putAll(properties);
            AdminUtils.changeTopicConfig(zkUtils,topic,curProp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zkUtils.close();
        }
    }

    /**
     * 获取当前主题的配置信息
     * @param topic
     * @return
     */
    public static Properties getTopicProperties(String topic) {
        return AdminUtils.fetchEntityConfig (getZkUtils(), ConfigType.Topic(), topic);
    }

    /**
     * 为主题增加分区 并指定副本的分区策略
     * @param topic
     * @param sumPartitions 分区总数
     * @param replicaPolicyExpression 副本分配策略
     */
    public static void addPartitions(String topic ,Integer sumPartitions,String replicaPolicyExpression){

        ZkUtils zkUtils=getZkUtils();

        try {
            AdminUtils.addPartitions(zkUtils,topic,sumPartitions,replicaPolicyExpression,
                    true,AdminUtils.addPartitions$default$6());


            System.out.println("topic："+topic+" 已新增了分区并指定了副本的分区策略！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zkUtils.close();
        }
    }

    /**
     * 删除主题
     * @param topics
     */
    public static void deleteTopic(String ...topics){
        ZkUtils zkUtils=getZkUtils();
        try {
            for (String topic:topics){
                AdminUtils.deleteTopic(zkUtils,topic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            zkUtils.close();
        }

    }

    /**
     * 修改主题的分区总数及副本数量
     * @param topic
     * @param sumPartitions
     * @param sumReplicas
     */
    public static void creatOrUpdateTopicPartitionAndReplica(String topic,Integer sumPartitions,Integer sumReplicas){

        ZkUtils zkUtils=getZkUtils();

        try {
            //生成分区及副本的分配方案
            Map<Object, Seq<Object>> objectSeqMap = getTopicPartitionAssignmentSeqMap(sumPartitions, sumReplicas, zkUtils);
            //修改分区及副本的分配方案
            AdminUtils.createOrUpdateTopicPartitionAssignmentPathInZK(zkUtils,topic,objectSeqMap,null,true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            zkUtils.close();
        }

    }



    private static Map<Object, Seq<Object>> getTopicPartitionAssignmentSeqMap(Integer sumPartitions, Integer sumReplicas, ZkUtils zkUtils) {
        //获取元信息
        Seq<BrokerMetadata> brokerMetadatas = AdminUtils.getBrokerMetadatas(zkUtils, AdminUtils.getBrokerMetadatas$default$2(), AdminUtils.getBrokerMetadatas$default$3());
        //生成分区及副本的分配方案
        return AdminUtils.assignReplicasToBrokers(brokerMetadatas, sumPartitions, sumReplicas,
                AdminUtils.assignReplicasToBrokers$default$4(), AdminUtils.assignReplicasToBrokers$default$5());
    }



}