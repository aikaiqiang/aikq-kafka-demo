package cn.aikaiqiang.demo.kafka.streams;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

/**
 *  日志数据清洗
 * @author aikaiqiang
 * @date 2019年05月20日 14:52
 */
public class LogProcesser implements Processor<byte[], byte[]> {
	/**
	 * 处理器上下文
	 */
	private ProcessorContext context;

	@Override
	public void init(ProcessorContext processorContext) {
		context = processorContext;
	}

	@Override
	public void process(byte[] bytes, byte[] bytes2) {
		// 取值 value
		String line = new String(bytes2);
		// 清洗数据，去除 >>>
		line = line.replaceAll(">>>", "");
		// 获取清洗后的数据
		bytes2 = line.getBytes();
		// 输出数据
		context.forward(bytes, bytes2);
	}

	@Override
	public void punctuate(long l) {

	}

	@Override
	public void close() {
		System.out.println(">>>流处理器已经关闭");
	}
}
