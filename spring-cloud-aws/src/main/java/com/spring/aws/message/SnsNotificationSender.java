package com.spring.aws.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * 
 * @author linhpham
 *
 */
@Component
public class SnsNotificationSender {
	
	@Autowired
	private  NotificationMessagingTemplate notificationMessagingTemplate;
	
	/**
	 * send message and subject to destination topic
	 * @param subject
	 * @param message
	 */
	public void send(String subject, String message) {
		this.notificationMessagingTemplate.sendNotification(message, subject);
	}
}
