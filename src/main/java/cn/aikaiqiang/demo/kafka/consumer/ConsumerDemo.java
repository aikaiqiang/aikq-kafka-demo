package cn.aikaiqiang.demo.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 *  高级 API 消费数据
 * @author aikaiqiang
 * @date 2019年05月16日 15:04
 */
public class ConsumerDemo {

	public static final String broker_list = "192.168.0.24:9092";
	public static final String topic = "my-first-topic";

	public static void main(String[] args) {

		// 配置信息
		Properties props = new Properties();
		// kafka 集群
		props.setProperty("bootstrap.servers", broker_list);
		// 消费者组 id
		props.setProperty("group.id", "test");
		// 设置自动提交 offset
		props.setProperty("enable.auto.commit", "true");
		// 设置自动提交延时单位 ms 毫秒， （作消息队列时）此处可能会产生重复消费的情况
		props.setProperty("auto.commit.interval.ms", "1000");
		// KV 反序列化
		props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		// 创建消费者对象
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		// 订阅 topic （可以消费多个 topic）
		// consumer.subscribe(Arrays.asList("topic1", "topic2"));
		consumer.subscribe(Collections.singletonList(topic));
		while (true) {
			// 100 ms获取一次数据
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records){
				System.out.printf("topic= %s，partition= %d，offset = %d, key = %s, value = %s%n", record.topic(), record.partition(), record.offset(), record.key(), record.value());
			}
		}
	}
}
