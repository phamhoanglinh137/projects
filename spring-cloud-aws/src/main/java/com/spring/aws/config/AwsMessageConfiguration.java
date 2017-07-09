package com.spring.aws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQSAsync;

/**
 * 
 * @author linhpham
 *
 * SQS/SNS configuration
 */
@Configuration
@ImportResource("classpath:/aws-config.xml")
public class AwsMessageConfiguration {
	
	@Value("${aws.sqs.queue-name}")
	private String SQS_QUEUE_NAME;
	
	@Value("${aws.sns.topic-name}")
	private String SNS_TOPIC_NAME;
	
	/**
	 * define Message Listener Factory
	 * @param amazonSqs
	 * @return
	 */
	@Bean
	public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs) {
		SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
		factory.setAmazonSqs(amazonSqs);
		factory.setAutoStartup(false);
		factory.setMaxNumberOfMessages(5);
		return factory;
	}
	
	/**
	 * define SQS Message Template to send message into queue
	 * @param amazonSqs
	 * @return
	 */
	@Bean
	public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSqs) {
		QueueMessagingTemplate queueMessagingTemplate = new QueueMessagingTemplate(amazonSqs);
		queueMessagingTemplate.setDefaultDestinationName(SQS_QUEUE_NAME);
		return queueMessagingTemplate;
	}
	
	/**
	 * define SNS notification message template to send message into TOPIC
	 * @param amazonSns
	 * @return
	 */
	@Bean
	public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSns) {
		NotificationMessagingTemplate notificationMessagingTemplate = new NotificationMessagingTemplate(amazonSns);
		notificationMessagingTemplate.setDefaultDestinationName(SNS_TOPIC_NAME);
		return notificationMessagingTemplate;
	}
	
}
