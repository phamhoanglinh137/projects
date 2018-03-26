package com.weather;

import java.util.Map;
import java.util.UUID;

import javax.jms.ConnectionFactory;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.integration.transformer.ObjectToStringTransformer;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.web.client.RestTemplate;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockQueueConnectionFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class IntegrationConfig {
	
	@Autowired
	Environment env;
	
	@SuppressWarnings("unchecked")
	@Bean
	public IntegrationFlow cityNameFlow(){
		return IntegrationFlows.from("cityNameChannel")
				 .handle(Http.outboundGateway(env.getProperty("weather.uri.city"), new RestTemplate())
						.httpMethod(HttpMethod.GET)
						.uriVariablesFunction(p -> ((Map<String, String>) p.getPayload()))
						.expectedResponseType(String.class)
						.get())
				.get();
	}
	
	
	//----- JMS ------
	
	 @Bean
	 public JmsTemplate jmsTemplate() {
		 return new JmsTemplate(jmsQueueConnectionFactory());
	 }
	
	@Bean
	public ConnectionFactory jmsQueueConnectionFactory() {
		DestinationManager destinationManager = new DestinationManager();
		destinationManager.createQueue("MOCKRUNNER-IN-QUEUE");
		destinationManager.createQueue("MOCKRUNNER-OUT-QUEUE");
		return new MockQueueConnectionFactory(destinationManager,
				new ConfigurationManager());
	}
	
	@Bean
	public DefaultMessageListenerContainer listenerContainer() {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setConnectionFactory(jmsQueueConnectionFactory());
		container.setDestinationName("MOCKRUNNER-IN-QUEUE");
		container.setMessageListener(messageListener());
		return container;
	}
//	
//	@Bean
//	public DefaultMessageListenerContainer listenerContainer1() {
//		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
//		container.setConnectionFactory(jmsQueueConnectionFactory());
//		container.setDestinationName("MOCKRUNNER-OUT-QUEUE");
//		container.setMessageListener(messageListener1());
//		return container;
//	}
	
	@Bean
	public SessionAwareMessageListener<TextMessage> messageListener() {
		return (h, s)-> {
								log.info("MSG ID {}", h.getJMSMessageID());
								jmsTemplate().send("MOCKRUNNER-OUT-QUEUE", session -> session.createTextMessage("Received " + h.getJMSMessageID()));
								s.commit();
						};
	}
//	
//	@Bean
//	public SessionAwareMessageListener<TextMessage> messageListener1() {
//		return (h, s)-> {
//								log.info("messageListener1 {} ", h.toString());
//						};
//	}
	
	
	@Bean
	public IntegrationFlow jmsOutboundGatewayFlow() {
	    return IntegrationFlows.from("jmsOutboundGatewayChannel")
	            .handle(Jms.outboundGateway(jmsQueueConnectionFactory())
	                        .requestDestination("MOCKRUNNER-IN-QUEUE")
	                        .replyDestination("MOCKRUNNER-OUT-QUEUE")
	                        .correlationKey(UUID.randomUUID().toString())
//	                        .jmsMessageConverter(new SimpleMessageConverter())
	                        .replyContainer(c ->
                            		c.receiveTimeout((long) 8000))
	                        )
	            .transform(new ObjectToStringTransformer())
	            .get();
	}
	
	
}
----
buildscript {
	ext {
		springBootVersion = '2.0.0.RELEASE'
	}
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
	}
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

group = 'com.weather'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.8

repositories {
	mavenCentral()
	maven { url "https://repo.spring.io/milestone" }
}


ext {
	springCloudVersion = 'Finchley.M8'
}

dependencies {
	compile('org.springframework.boot:spring-boot-starter-integration')
	compile('org.springframework.integration:spring-integration-http') 
	compile('org.springframework.integration:spring-integration-ws')
	compile('org.springframework.integration:spring-integration-xml')
	compile('org.springframework.integration:spring-integration-jms')
	compile('org.springframework.integration:spring-integration-mail')
	compile("org.springframework:spring-jms")
	
	compile("com.mockrunner:mockrunner-jms:1.1.2")
	compile("org.glassfish.main.javaee-api:javax.jms:3.1.2.2")
	
	compile("com.squareup.okhttp3:okhttp:3.10.0")
	compile "org.projectlombok:lombok:1.16.20"
	
	testCompile('org.springframework.boot:spring-boot-starter-test')
	//testCompile('org.springframework.integration:spring-integration-tests')
}

dependencyManagement {
	imports {
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
}
