package cn.aikaiqiang.demo.kafka.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 *  生产者拦截器， 添加时间戳
 * @author aikaiqiang
 * @date 2019年05月20日 13:40
 */
public class TimeInterceptor implements ProducerInterceptor<String, String> {


	@Override
	public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
		return new ProducerRecord<String, String>(record.topic(), record.key(), System.currentTimeMillis() + "," + record.value());
	}

	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {

	}
}
