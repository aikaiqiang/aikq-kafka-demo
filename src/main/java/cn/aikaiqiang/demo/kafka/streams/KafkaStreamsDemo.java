package cn.aikaiqiang.demo.kafka.streams;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.processor.TopologyBuilder;

import java.util.Properties;

/**
 *  E
 * @author aikaiqiang
 * @date 2019年05月20日 14:39
 */
public class KafkaStreamsDemo {

	public static final String broker_list = "192.168.0.24:9092";
	public static final String topic = "my-first-topic";
	public static final String topic_2 = "my-second-topic";


	public static void main(String[] args) {

		// 创建拓扑对象
		TopologyBuilder topologyBuilder = new TopologyBuilder();
		// 配置文件
		Properties props = new Properties();

		props.setProperty("application.id", "kafka-stream");
		props.setProperty("bootstrap.servers", broker_list);

		// 构建拓扑结构
		topologyBuilder.addSource("source", topic)
				.addProcessor("processor", () -> new LogProcesser() {
				}, "source")
				.addSink("sink", topic_2, "processor");

		KafkaStreams kafkaStreams = new KafkaStreams(topologyBuilder, props);

		kafkaStreams.start();


	}
}
