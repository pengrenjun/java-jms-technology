package AdvancedFeatures.pubsub;

import atguigu.redis.test.JedisPoolUtil;
import redis.clients.jedis.Jedis;

/**
 * 订阅者
 */
public class RedisPublish {

    public static void main(String[] args) {

        Jedis jedis=JedisPoolUtil.getJedisPoolInstance().getResource();

        //发布频道 "ch1" 和消息 "hello redis"
        for(int i=0;i<10;i++){
            jedis.publish("ch1", "hello redis"+i);
        }
        //关闭连接
        jedis.close();
    }

}
