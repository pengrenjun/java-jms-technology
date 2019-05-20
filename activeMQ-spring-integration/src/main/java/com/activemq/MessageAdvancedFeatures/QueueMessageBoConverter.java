package com.activemq.MessageAdvancedFeatures;

import org.springframework.jms.support.converter.SimpleMessageConverter;

/**
 * @Description 消息信息对象的发送接收转换器
 * @Date 2019/5/20 0020 下午 5:39
 * @Created by Pengrenjun
 */

public class QueueMessageBoConverter extends SimpleMessageConverter {
    //A simple message converter which is able to handle TextMessages, BytesMessages,
    //MapMessages, and ObjectMessages. Used as default conversion strategy
    //默认的转换即可符合要求  也可以实现MessageConverter 自定义转换
}
