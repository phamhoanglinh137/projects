package com.integration.jms.stub.server;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.mockrunner.jms.ConfigurationManager;
import com.mockrunner.jms.DestinationManager;
import com.mockrunner.mock.jms.MockQueueConnectionFactory;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author linhpham
 * 
 * enable JMS Server using MockRunner-JMS
 */
@Slf4j
@Configuration
public class JmsServerStubConfig {
	@Value("${jms.queue.in}")
	private String inQueue;
	
	@Value("${jms.queue.out}")
	private String outQueue;
	
	@Bean
	public ConnectionFactory jmsQueueConnectionFactory() {
		DestinationManager destinationManager = new DestinationManager();
		destinationManager.createQueue(inQueue);
		destinationManager.createQueue(outQueue);
		return new MockQueueConnectionFactory(destinationManager,
				new ConfigurationManager());
	}
	
	@Bean
	public JmsTemplate jmsTemplate() {
		return new JmsTemplate(jmsQueueConnectionFactory());
	}
	
	@Bean
	public DefaultMessageListenerContainer listenerContainer() {
		DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
		container.setConnectionFactory(jmsQueueConnectionFactory());
		container.setDestinationName(inQueue);
		container.setMessageListener(messageListener());
		return container;
	}
	
	@Bean
	public SessionAwareMessageListener<TextMessage> messageListener() {
		return (h, s)-> {
								log.info("MSG correlation id {} - content {}", h.getJMSCorrelationID(), h.getText());
								h.acknowledge();
								
								jmsTemplate().send(outQueue, session -> {
									ObjectMessage returnedMsg = session.createObjectMessage();
									returnedMsg.setJMSCorrelationID(h.getJMSCorrelationID());
									returnedMsg.setObject(person().get(h.getText()) != null ? person().get(h.getText()) : Strings.EMPTY);
									return returnedMsg;
								});
						};
	}
	
	@Data
	@Builder
	public static class Person implements Serializable{
		private static final long serialVersionUID = 1L;
		
		String name;
		LocalDate dateOfBirth;
		String nric;
		int age;
	}
	
	@Bean
	public Map<String, Person> person() {
		Map<String, Person> map = new HashMap<String, Person>();
		map.put("G1111111N", Person.builder().name("Alex").nric("G1111111N").dateOfBirth(LocalDate.of(2001, 12, 01)).build());
		map.put("G2222222N", Person.builder().name("Bob").nric("G2222222N").dateOfBirth(LocalDate.of(1992, 02, 03)).build());
		map.put("G3333333N", Person.builder().name("Reyan").nric("G3333333N").dateOfBirth(LocalDate.of(1988, 04, 01)).build());
		map.put("G4444444N", Person.builder().name("Kevin").nric("G4444444N").dateOfBirth(LocalDate.of(1983, 05, 21)).build());
		return map;
	}
}
