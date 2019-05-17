package cn.aikaiqiang.demo.kafka.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 *  生产者自定义分区类
 * @author aikaiqiang
 * @date 2019年05月17日 13:19
 */
public class CustomerPartitioner implements Partitioner {


	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		// 返回 0 号分区
		return 0;
	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {

	}
}
