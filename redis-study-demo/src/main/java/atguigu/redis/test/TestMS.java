package atguigu.redis.test;

import redis.clients.jedis.Jedis;

public class TestMS {
	public static void main(String[] args) {

		//����redis����֮�� ���Ӹ��ƿ���ͨ������ʵ��ָ��
		Jedis jedis_M = new Jedis(RedisConnectConf.redisHost,6379);
		Jedis jedis_S = new Jedis(RedisConnectConf.redisHost,6380);
		
		jedis_S.slaveof(RedisConnectConf.redisHost,6379);
		
		jedis_M.set("class","1122V2");
		
		String result = jedis_S.get("class");
		System.out.println(result);
	}
}
