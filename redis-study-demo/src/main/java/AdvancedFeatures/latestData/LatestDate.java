package AdvancedFeatures.latestData;

import atguigu.redis.test.JedisPoolUtil;
import redis.clients.jedis.Jedis;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Redis使用的是常驻内存的缓存，速度非常快。
 * LPUSH用来插入一个内容ID，作为关键字存储在列表头部。
 * LTRIM用来限制列表中的项目数最多为5000。
 * 如果用户需要的检索的数据量超越这个缓存容量，这时才需要把请求发送到数据库。
 */
public class LatestDate {
    public static void main(String[] args) throws InterruptedException {

        Jedis jedis = JedisPoolUtil.getJedisPoolInstance().getResource();
        jedis.select(1);

        //模拟数据库
        LinkedList<String> hotNews=new LinkedList<>();

        jedis.lpush("hotnews","");

        for(int i=0;i<500;i++){
            //向数据库中先存入原始数据
            hotNews.add("hostnew"+i);

            jedis.lpush("hotnews","hostnew"+i);
            //限定list的长度 使其最多存储10个元素
            jedis.ltrim("hotnews",0,10);
        }

        //做个模拟 查看热点数据
        for(;;){
            Random random=new Random();
            int count=random.nextInt(20);

            if(count<=10){
                //查看的热点数据<=10 从缓存中获取
                List<String> hotnews = jedis.lrange("hotnews", 0, count);
                System.out.println("缓存中获取的热点数据:"+hotnews.toString());
            }
            else {
                //数据从数据中获取
                List<String> strings = hotNews.subList(hotNews.size()-count,hotNews.size());
                System.out.println("从数据库中获取热点数据:"+strings.toString());
            }

            Thread.sleep(1000);
        }


    }
}
