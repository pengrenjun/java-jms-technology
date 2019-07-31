package AdvancedFeatures.counter;

import AdvancedFeatures.User;
import atguigu.redis.test.JedisPoolUtil;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * 用户维度计数（动态数、关注数、粉丝数、喜欢商品数、发帖数 等）
 * 用户维度计数同商品维度计数都采用 Hash. 为User定义个key user:，
 * 为每种数值定义hashkey, 譬如关注数follow
 */
public class UserDemo {

    public static void main(String[] args) {

        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();

        User user1=new User("1001","user1");

        //用户的热点数据存入缓存中
        String key=user1.getId()+":"+user1.getUserName();
        //Hash 动态数量
        jedis.hset(key,"dynamic","1000");
        //关注数
        jedis.hset(key,"attention","1999");

        //增加动态数量及关注数
        for(int i=0;i<100;i++){
            jedis.hincrBy(key,"dynamic",1);
            jedis.hincrBy(key,"attention",2);
        }

        //获取用户所有的数据
        Map<String, String> stringStringMap = jedis.hgetAll(key);

        System.out.println(stringStringMap.toString());


    }
}
