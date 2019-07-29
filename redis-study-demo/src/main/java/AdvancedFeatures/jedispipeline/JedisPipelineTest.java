package AdvancedFeatures.jedispipeline;

import atguigu.redis.test.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * redis的pipeline(管道)功能在命令行中没有，
 * 但是redis是支持管道的，在java的客户端(jedis)中是可以使用的
 */
public class JedisPipelineTest {

    public static void main(String[] args) {

        //不使用管道
        testA();
        //使用管道
        testB();
    }

    private static void testB() {
        // 测试管道

        long currentTimeMillis = System.currentTimeMillis();
        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
        Pipeline pipelined = jedis.pipelined();
        for (int i = 0; i < 1000; i++) {
            pipelined.set("bb" + i, i + "bb");
        }
        pipelined.sync();
        long endTimeMillis = System.currentTimeMillis();
        System.out.println(endTimeMillis - currentTimeMillis);


    }

    private static void testA() {

            // 测试不使用管道
            long currentTimeMillis = System.currentTimeMillis();
            Jedis jedis =JedisPoolUtil.getJedisPoolInstance().getResource();

            for (int i = 0; i < 1000; i++) {
                jedis.set("test" + i, "test" + i);
            }
            long endTimeMillis = System.currentTimeMillis();
            System.out.println(endTimeMillis - currentTimeMillis);
    }

}
