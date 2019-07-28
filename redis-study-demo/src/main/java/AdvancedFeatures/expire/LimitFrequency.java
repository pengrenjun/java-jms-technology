package AdvancedFeatures.expire;

import AdvancedFeatures.User;
import atguigu.redis.test.JedisPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 应用场景：限制网站访客访问频率（例如：30s最多访问10次）
 * redis中键的生存时间（expire）
 * 过期时间可以设置为秒或者毫秒精度
 */
public class LimitFrequency {


    public static void main(String[] args) throws InterruptedException {

        JedisPool jedisPool = JedisPoolUtil.getJedisPoolInstance();

        //模拟两个用户进行1分钟的多次操作
        User user1 = new User("user1", "xiaoming");

        //用户登录 key:用户id value:登录次数
        Jedis jedis = jedisPool.getResource();

        //模拟用户一直登录
        while (true) {

            String s = jedis.get(user1.getId());
            Integer count = 0;
            if (s == null) {
                //用户30s登录次数重新计算
                System.out.println("30s登录次数开始计数！");
                jedis.getSet(user1.getId(), "0");
                jedis.expire(user1.getId(), 30);
            } else {
                count = Integer.valueOf(s);
            }


            if (0 < count && count <= 10) {
                System.out.println("登录ok 30s内登录次数->" + count);

            }
            if (count > 10) {
                System.out.println("30s内登录次数已经超过了10次！操作过于频繁");
            }
            //计数
            jedis.incr(user1.getId());
            //2s登录一次
            Thread.sleep(2 * 1000);
        }

    }


}
