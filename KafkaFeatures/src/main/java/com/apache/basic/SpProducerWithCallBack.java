package com.apache.basic;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apache.obj.Account;
import com.apache.obj.Base;


/**
 * 
 * @author linhpham
 *
 */
public class SpProducerWithCallBack {
	final static Logger logger = LoggerFactory.getLogger(SpProducerWithCallBack.class);
	
	private static SpProducerWithCallBack producer;
	
	private Producer<String, Object> kafkaProducer;
	
	private SpProducerWithCallBack() {
		 Properties props = new Properties();
		 props.put("bootstrap.servers", "localhost:9092");
		 props.put("acks", "all");
		 props.put("retries", 0);
		 props.put("batch.size", 100);
		 props.put("linger.ms", 1);
		 props.put("buffer.memory", 33554432);
		 props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer"); 
		 
		 kafkaProducer = new KafkaProducer<>(props);
	}
	
	public static SpProducerWithCallBack getInstance() {
		if(producer == null) {
			producer = new SpProducerWithCallBack();
		}
		return producer;
	}
	
	public <T extends Base> void send (@SuppressWarnings("unchecked") T... msgs) {
		for (T msg : msgs) {
			ProducerRecord<String, Object> record = new ProducerRecord<String, Object>("account_open", msg.getMsgId(), msg.toString().getBytes());
			logger.info("send msg {}", record.key());
			kafkaProducer.send(record, new ProducerCallBack());
		}
		kafkaProducer.flush();
	}
    
	private static class ProducerCallBack implements org.apache.kafka.clients.producer.Callback {
        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if(exception == null){
            	logger.info("topic {}, offset {}, partiion {}", metadata.topic(), metadata.offset(), metadata.partition());
            } else {
            	logger.error(exception.getMessage());
            }
        }
    }
	
	public static void main(String args[]) {
		SpProducerWithCallBack.getInstance().send(new Account("9012312", "Alex", "G12345678"),
				new Account("9012313", "Andy", "F1234567"), new Account("9012314", "Beck", "M12345678"));
	}
}
