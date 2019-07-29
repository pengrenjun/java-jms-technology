package AdvancedFeatures.sort;

import atguigu.redis.test.JedisPoolUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * sort命令可以对列表类型，集合类型和有序集合类型进行排序
 */
public class SortDemo {

    public static void main(String[] args) {

        Jedis jedis=JedisPoolUtil.getJedisPoolInstance().getResource();
        jedis.lpush("userIds", "1", "3", "5", "2", "9");

        // list of numbers ordered from the smallest to the biggest number
        List<String> userIds = jedis.sort("userIds");
        System.out.println(userIds.toString());



    }
}
