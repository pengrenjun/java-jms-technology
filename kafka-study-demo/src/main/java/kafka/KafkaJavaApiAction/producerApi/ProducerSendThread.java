package kafka.KafkaJavaApiAction.producerApi;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.*;


/**
 * @Description 数据发送线程
 * @Date 2019/5/30 0030 下午 5:29
 * @Created by Pengrenjun
 */
public class ProducerSendThread implements Runnable {

    private Producer  kafkaProducer;

    private ProducerRecord producerRecord;

    private Callback callback;

    public <K, V>ProducerSendThread(Producer<K, V> kafkaProducer, ProducerRecord producerRecord, Callback callback) {
        this.kafkaProducer = kafkaProducer;
        this.producerRecord = producerRecord;
        this.callback = callback;
    }

    @Override
    public void run() {

        System.out.println("执行发送线程->"+Thread.currentThread().getName());
        try {
            kafkaProducer.send(producerRecord,callback);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //线程执行后关闭连接
            kafkaProducer.close();
        }
    }
}
