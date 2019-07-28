package atguigu.redis.test;

import redis.clients.jedis.Jedis;

public class TestPing {
	public static void main(String[] args) 
	{
		Jedis jedis = new Jedis(RedisConnectConf.redisHost,RedisConnectConf.redisPort,10*1000);
		System.out.println(jedis.ping());
	}
}
