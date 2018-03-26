package com.integration;

import java.util.Map;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.jms.support.converter.SimpleMessageConverter;

/**
 * 
 * @author linhpham
 *
 */
@Configuration
public class Config {
	
	@Autowired
	Environment env;
	
	// -- HTTP--
	@SuppressWarnings("unchecked")
	@Bean
	public IntegrationFlow cityName(){
		return IntegrationFlows.from("cityNameChannel")
				 .handle(Http.outboundGateway(env.getProperty("http.uri.city"))
						.httpMethod(HttpMethod.GET)
						.uriVariablesFunction(p -> ((Map<String, String>) p.getPayload()))
						.expectedResponseType(String.class)
						.get())
				.get();
	}
	
	// -- JMS --
	@Bean
	public IntegrationFlow sendJms(ConnectionFactory jmsQueueConnectionFactory){
		return IntegrationFlows.from("outboundJMSChannel")
				 .handle(Jms.outboundGateway(jmsQueueConnectionFactory)
						 .requestDestination(env.getProperty("jms.queue.in"))
						 .replyDestination(env.getProperty("jms.queue.out"))
						 .correlationKey("JMSCorrelationID")
						 .jmsMessageConverter(new SimpleMessageConverter())
						 .replyContainer(spec -> spec.receiveTimeout(Long.valueOf(10 * 1000))
													.concurrentConsumers(3)
													.sessionTransacted(true))
						 .get()
				)
			    .get();
	}
}
