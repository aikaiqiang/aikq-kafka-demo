package cn.aikaiqiang.demo.kafka.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 *  生产者拦截器 count 统计成功和失败的次数
 * @author aikaiqiang
 * @date 2019年05月20日 13:47
 */
public class CountInterceptor implements ProducerInterceptor<String, String> {

	/**
	 * 发送成功次数
	 */
	private int successCount = 0;
	/**
	 * 发送失败次数
	 */
	private int failCount = 0;

	@Override
	public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
		return record;
	}

	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		if(exception == null){
			successCount++;
		}else {
			failCount++;
		}
	}

	@Override
	public void close() {
		// 关闭前打印
		System.out.println("Send Success Count ： " + successCount);
		System.out.println("Send Fail Count ： " + failCount);
	}

	@Override
	public void configure(Map<String, ?> configs) {

	}
}
