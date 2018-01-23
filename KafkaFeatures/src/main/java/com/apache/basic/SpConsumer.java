package com.apache.basic;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpConsumer implements Runnable {
	final static Logger logger = LoggerFactory.getLogger(SpConsumer.class);
	
	private KafkaConsumer<String, Object> consumer;
	
	public SpConsumer(Properties props, String... topicNames) {
		consumer = new KafkaConsumer<String, Object>(props);
		consumer.subscribe(Arrays.asList(topicNames));
	}

	@Override
	public void run() {
		while(true) {
			ConsumerRecords<String, Object> records = consumer.poll(100);
	         for (ConsumerRecord<String, Object> record : records)
	        	 logger.info("offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
		}
	}
	
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		
		SpConsumer consumer = new SpConsumer(props, "account_open");
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		executorService.submit(consumer);
		
	}
}
