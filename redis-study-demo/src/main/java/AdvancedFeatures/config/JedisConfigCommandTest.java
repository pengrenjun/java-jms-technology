package AdvancedFeatures.config;

import atguigu.redis.test.JedisPoolUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * 使用config set可以动态设置参数信息，服务器重启之后就失效了。
 *
 config set appendonly yes
 config set save "90 1 30 10 60 100"
 * 使用config rewrite命令对启动 Redis 服务器时所指定的 redis.conf 文件进行改写
 * (Redis 2.8 及以上版本才可以使用)，
 * 主要是把使用config set动态指定的命令保存到配置文件中。
 */
public class JedisConfigCommandTest {

    private static Jedis jedis=JedisPoolUtil.getJedisPoolInstance().getResource();

    public static void main(String[] args) {
        //查看当前连接的redis服务的redis.config内的配置参数信息
        getRedisConfigs();

        //修改服务器的配置信息
        //modifyRedisConfigs();
    }

    private static void modifyRedisConfigs() {
        //修改前 dbfilename dump6379.rdb
        //测试发现 执行修改之后 参数值设置修改查询也是修改后的结果 但是需要执行config rewrite才可以将修改的参数写入到配置文件中
        //java 客户端如何执行config rewrite？这个需要查找
        jedis.configSet("dbfilename","dump6379_test.rdb");
        getRedisConfigs();
    }

    private static void getRedisConfigs() {
        List<String> strings = jedis.configGet("*");

        for (String param:strings){
            System.out.println(param);
        }
    }
}
