package com.integration;

import java.util.Map;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

import com.integration.jms.stub.server.JmsServerStubConfig.Person;

@MessagingGateway
public interface AppGateway {
	
	@Gateway(requestChannel="cityNameChannel")
	public String getByCityName(@Payload Map<String, String> cityname);
	
	@Gateway(requestChannel="outboundJMSChannel")
	public Person getPersonInfo(@Payload String msg);
	
	@Gateway(requestChannel="addSoapChannel", payloadExpression = "#args[0] + ':' + #args[1]")
	public Integer add(Integer num1, Integer num2);
	
}
