package com.spring.aws.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	@SqsListener("spring-aws-sqs-sp1")
	public void receive(String message) {
		log.info("receive :: {}", message);
	}
}
