package AdvancedFeatures.pubsub;

import atguigu.redis.test.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * 发布者
 */
public class RedisSubcribe {

    public static void main(String[] args) {

        Jedis jedis=JedisPoolUtil.getJedisPoolInstance().getResource();

        JedisPubSub jedisPubSub = new JedisPubSub() {

            // 当向监听的频道发送数据时，这个方法会被触发
            @Override
            public void onMessage(String channel, String message) {
                System.out.println("收到消息" + message);
                //当收到 "unsubscribe" 消息时，调用取消订阅方法
                if ("unsubscribe".equals(message)) {
                    this.unsubscribe();
                }
            }

            // 当取消订阅指定频道的时候，这个方法会被触发
            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                System.out.println("取消订阅频道" + channel);
            }

        };
        // 订阅之后，当前进程一致处于监听状态，当被取消订阅之后，当前进程会结束
        jedis.subscribe(jedisPubSub, "ch1");
    }
}
