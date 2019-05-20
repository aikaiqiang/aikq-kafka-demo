package cn.aikaiqiang.demo.kafka.producer;

import org.apache.kafka.clients.producer.*;

import java.util.ArrayList;
import java.util.Properties;

/**
 *  kafka 生产者
 * @author aikaiqiang
 * @date 2019年05月16日 9:11
 */
public class ProdeucerDemo {

	public static final String broker_list = "192.168.0.24:9092";
	public static final String topic = "my-first-topic";

	public static void main(String[] args) {
		// 配置信息
		Properties props = new Properties();
		// kafka 集群
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker_list);
		// 应答模式（"0", "1", "all"）:
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		// KV 序列化
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

		// 配置自定义分区
		props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "cn.aikaiqiang.demo.kafka.producer.CustomerPartitioner");

		// 配置拦截器链: 按照添加拦截器的顺序创建
		ArrayList<String> list = new ArrayList<>();
		list.add("cn.aikaiqiang.demo.kafka.interceptor.TimeInterceptor");
		list.add("cn.aikaiqiang.demo.kafka.interceptor.CountInterceptor");
		props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, list);

		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 10; i++){
			producer.send(new ProducerRecord<String, String>(topic , "msg" + Integer.toString(i + 100)), new Callback() {
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					if(exception == null){
						System.out.println("Send Success");
						System.out.println(metadata.partition() + "-" + metadata.offset());
					}else {
						System.out.println("Send Fail , Message: " + exception.getMessage());
					}
				}
			});
		}

		producer.close();
		System.out.println("----- end -----");
 	}
}
