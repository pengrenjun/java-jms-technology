package com.activemq;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description 启动所有定义的消息监听端口 订阅者消费可以将监听的配置单独放在一个xml文件中启动
 * @Date 2019/5/20 0020 下午 6:13
 * @Created by Pengrenjun
 */
public class ConsumerMain {

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:message-Advanced-features.xml");
    }

}
