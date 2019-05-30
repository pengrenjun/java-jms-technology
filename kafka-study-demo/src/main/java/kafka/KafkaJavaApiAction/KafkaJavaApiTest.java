package kafka.KafkaJavaApiAction;

import kafka.KafkaJavaApiAction.topicApi.TopicUtils;
import org.junit.Test;

import java.util.Properties;

/**
 * @Description Kafka java api测试
 * @Date 2019/5/29 0029 下午 5:23
 * @Created by Pengrenjun
 */
public class KafkaJavaApiTest {

    //创建主题
    @Test
    public void testCreateTopic(){

        TopicUtils.createTopic("topicAC",2,1, null);

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
}
