package com.spring.aws.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author linhpham
 * 
 * SNS class
 * - listen to new message in SNS topic
 * - send SMS message
 */
@Component
@ConfigurationProperties(prefix="admin.notification")
public class SnsSmsPulisher {
	Logger log = LoggerFactory.getLogger(SnsSmsPulisher.class);
	
	List<String> phones = new ArrayList<String>();
	
	@Value("${aws.sns.topic-arn}")
	String topicArn;
	
	@Autowired
	AmazonSNS amazonSns;
	
	private Map<String, MessageAttributeValue> smsAttributes;
	
	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	/**
	 * subscribe sms to Topic, so Admin will receive SMS when new customer is registered.
	 * 
	 * This part can be done in AWS console in SNS topic subscription part.
	 * 
	 * @param amazonSns
	 */
	@Autowired
	public SnsSmsPulisher(AmazonSNS amazonSns) {
		for (String phoneNumber : phones) {
			log.info("Subscribing SNS for phonenumber {}", phoneNumber);
			SubscribeRequest subscribe = new SubscribeRequest(topicArn, "sms", phoneNumber);
			SubscribeResult subscribeResult = amazonSns.subscribe(subscribe);
			log.info("Subscribe request: {}", amazonSns.getCachedResponseMetadata(subscribe));
			log.info("Subscribe result: {}", subscribeResult);
		}
	}
	
	/**
	 * initiating SMS attributes 
	 */
	@PostConstruct
	public void innitiate() {
		smsAttributes =
		        new HashMap<String, MessageAttributeValue>();
		smsAttributes.put("AWS.SNS.SMS.SenderID", new MessageAttributeValue()
		        .withStringValue("SprAWSCld") //The sender ID shown on the device.(1-11 characters)
		        .withDataType("String"));
		smsAttributes.put("AWS.SNS.SMS.MaxPrice", new MessageAttributeValue()
		        .withStringValue("0.50") //Sets the max price to 0.50 USD.
		        .withDataType("Number"));
		smsAttributes.put("AWS.SNS.SMS.SMSType", new MessageAttributeValue()
		        .withStringValue("Promotional") //Sets the type to promotional.
		        .withDataType("String"));
	}
	
	/**
	 * send sms to registered Customer.
	 * @param phoneNumber
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void sendSms(String message){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			AwsSNSMessage awsSnsMessage = objectMapper.readValue(message, AwsSNSMessage.class);
			log.info("send SMS to new customer {}", awsSnsMessage.getMessage());
			PublishResult result = amazonSns.publish(new PublishRequest()
	                .withMessage("Welcome to Spring Cloud AWS")
	                .withPhoneNumber(awsSnsMessage.getMessage())
	                .withMessageAttributes(smsAttributes));
			log.info("resutl {}", result.toString());
		} catch(Exception ex) {
			log.error("exception in sendSms", ex);
		}
	}
}
