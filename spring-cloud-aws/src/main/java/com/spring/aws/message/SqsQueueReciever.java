package com.spring.aws.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

/**
 * 
 * @author linhpham
 *
 */
@Component
public class SqsQueueReciever {
	Logger log = LoggerFactory.getLogger(SqsQueueReciever.class);
	
	@Autowired
	SnsSmsPulisher publisher;
	
	@SqsListener("spring-aws-sqs-sp1")
	public void receive(String message) {
		log.info("receive :: {}", message);
	}
	
	@SqsListener("spring-aws-sqs-sp2-listen-sns")
	public void receiveFromTopic(String message) {
		log.info("receive from Topic:: {}", message);
		publisher.sendSms(message);
	}
}
