package AdvancedFeatures.TaskQueue;

import atguigu.redis.test.JedisPoolUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Redis的list类型提供了队列的特定操作
 * 任务队列：使用lpush和rpop(brpop)可以实现普通的任务队列。
 *
 *  brpop是列表的阻塞式(blocking)弹出原语。
 *
 * 它是 RPOP命令的阻塞版本，当给定列表内没有任何元素可供弹出的时候，连接将被 BRPOP命令阻塞，
 * 直到等待超时或发现可弹出元素为止。
 *
 * 当给定多个 key 参数时，按参数 key 的先后顺序依次检查各个列表，弹出第一个非空列表的尾部元素。
 *
 *  
 *
 * 优先级队列：
 *
 * brpop key1 key2 key3 timeout
 *
 */
public class RedisQueueDemo {

    private static  Jedis jedis=JedisPoolUtil.getJedisPoolInstance().getResource();;

    private static String[] testData=new String[]{"1","2","3","4","5","6","7","8","9","10"};
    private static String[] testData2=new String[]{"100","101","102"};


    public static void main(String[] args) throws InterruptedException {
        //testA();

        testB();
    }

    /**
     * 阻塞队列的实现
     */
    private static void testB() throws InterruptedException {
        jedis.lpush("queue2", testData);

//        //开启一个添加数据的线程
//        Thread addThread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    try {
//                        Thread.sleep(3*1000);
//                        jedis.lpush("queue2",testData2);
//                        System.out.println("添加数据线程执行OK!");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        addThread.start();

        while (true){
            /*unusable command, this command will be removed in 3.0.0*/
            //List<String> queue2 = jedis.brpop("queue2");
            //每次从queue2中取出一个数据 放入到list中
            //如果没有可取的数据了 则阻塞一定的时间
            List<String> queue2 =jedis.brpop(3,"queue2");

            //等待3s后 继续执行
            if(queue2.isEmpty()){
                System.out.println("queue2中等待任务添加>>>>>>");
                jedis.lpush("queue2", testData2);
            }
            else {
                System.out.println(queue2.toString());
                Thread.sleep(1000);
            }
        }
    }


    /**
     * 非阻塞的队列 list
     * @throws InterruptedException
     */
    private static void testA() throws InterruptedException {
        jedis.lpush("queue1", testData);

        while (true){
            //使用rpop如果队列为空则返回null
            String str = jedis.rpop("queue1");
            Thread.sleep(1000);
            if(str==null){
                break;
            }
            System.out.println(str);
        }
    }

}
