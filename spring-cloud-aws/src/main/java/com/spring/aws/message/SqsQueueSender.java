package com.spring.aws.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 
 * @author linhpham
 *
 */
@Component
public class SqsQueueSender {
	
	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;
	

	public void send(String message) {
		this.queueMessagingTemplate.send(MessageBuilder.withPayload(message).build());
	}
}
