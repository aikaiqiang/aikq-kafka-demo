package cn.aikaiqiang.demo.kafka.consumer;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.cluster.BrokerEndPoint;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 *  低级 API 消费 demo
 *  根据指定的 topic partition offset获取分区数据
 * @author aikaiqiang
 * @date 2019年05月17日 14:00
 */
public class LowerConsumerDemo {

	public static final String ENCODING = "UTF-8";

	private static List<Map<String, String>> brokerList;
	public static final String topic = "my-first-topic";
	public static final int partition = 0;
	public static final long offset = 0;

	{
		brokerList = new ArrayList<>();
		Map<String, String> host1 = new HashMap<>();
		host1.put("host","192.168.0.24");
		host1.put("port","9092");
		Map<String, String> host2 = new HashMap<>();
		host1.put("host","192.168.0.24");
		host1.put("port","9093");
		Map<String, String> host3 = new HashMap<>();
		host1.put("host","192.168.0.24");
		host1.put("port","9094");
		brokerList.add(host1);
		brokerList.add(host2);
		brokerList.add(host3);
	}

	public static void main(String[] args) {
		LowerConsumerDemo lowerConsumerDemo = new LowerConsumerDemo();
		try {
			lowerConsumerDemo.getDate(brokerList, topic, partition, offset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 找分区 leader
	 * @param brokerList
	 * @param topic
	 * @param partition
	 * @return
	 */
	private BrokerEndPoint findPartition(List<Map<String, String>> brokerList, String topic, int partition){
		for (Map<String, String> map : brokerList) {
			// 创建获取分区 leader 的消费者对象
			SimpleConsumer getLeader = new SimpleConsumer(map.get("host").toString(),
					Integer.parseInt(map.get("port")), 1000, 1024 * 4, "getLeader");

			// 创建主题元数据请求信息
			TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(Collections.singletonList(topic));

			// 获取元数据信息
			TopicMetadataResponse metadataResponse = getLeader.send(topicMetadataRequest);

			// 解析元数据信息
			List<TopicMetadata> topicMetadata = metadataResponse.topicsMetadata();

			// 遍历主题元数据
			for (TopicMetadata topicMetadatum : topicMetadata) {
				// 主题分区信息
				List<PartitionMetadata> partitionMetadata = topicMetadatum.partitionsMetadata();

				// 遍历分区
				for (PartitionMetadata partitionMetadatum : partitionMetadata) {
					int partitionId = partitionMetadatum.partitionId();
					if(partitionId == partition){
						// 主备信息列表，leader 挂了后便于找到新的 leader
						List<BrokerEndPoint> isr = partitionMetadatum.isr();
						// 返回 leader 对象
						return partitionMetadatum.leader();
					}
				}
			}
		}
		return null;
	}


	private void getDate(List<Map<String, String>> brokerList, String topic, int partition, long offset)
			throws UnsupportedEncodingException {
		// 获取分区 leader
		BrokerEndPoint leader = findPartition(brokerList, topic, partition);
		if(null == leader){
			return;
		}

		// 分区 leader 的主机 host 和端口 port
		String host = leader.host();
		int port = leader.port();

		// 获取数据的消费者对象
		SimpleConsumer getData = new SimpleConsumer(host, port, 1000, 1024 * 4, "getData");

		// 创建获取数据的请求信息, 可添加多个 addFetch
		FetchRequest fetchRequest = new FetchRequestBuilder().addFetch(topic, partition, offset, 500000).build();

		// 获取数据返回对象
		FetchResponse response = getData.fetch(fetchRequest);

		// 解析返回值，此处指定 topic ，添加多个 addFetch 后会返回多个
		ByteBufferMessageSet bufferMessageSet = response.messageSet(topic, partition);

		// 遍历打印数据
		for (MessageAndOffset messageAndOffset : bufferMessageSet) {
			long offset1 = messageAndOffset.offset();
			// 保存 offset，便于下次开始消费
			ByteBuffer payload = messageAndOffset.message().payload();
			byte[] bytes = new byte[payload.limit()];
			payload.get(bytes);
			System.out.println(offset1 + "-" + new String(bytes, ENCODING));
		}
	}
}
