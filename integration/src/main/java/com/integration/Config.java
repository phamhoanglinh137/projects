package com.integration;

import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.support.DefaultHttpHeaderMapper;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.integration.ws.DefaultSoapHeaderMapper;
import org.springframework.integration.ws.MarshallingWebServiceOutboundGateway;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.messaging.MessageHandler;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import com.integration.soap.obj.Add;

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
	
	
	@Bean
	public ExpressionParser parser() {
	    return new SpelExpressionParser();
	}

	@Bean
	public HeaderMapper<HttpHeaders> headerMapper() {
	    return new DefaultHttpHeaderMapper();
	}

	@Bean
	public IntegrationFlow citynameInBound() {
		return IntegrationFlows
				.from(Http.inboundGateway("/city/{city}")
							.headerMapper(headerMapper())
							.requestMapping(request -> request.consumes("application/json")
													.produces("application/json")
													.methods(HttpMethod.GET))
							.payloadExpression("#pathVariables.city")
							.requestTimeout(10*1000)
							.replyTimeout(10*1000))
				.log(Level.INFO, "payload", m -> m.getPayload().toString())
				.<String, Map<String, String>>transform(h -> {		
							Map<String, String> maps = new HashMap<String, String>();
							maps.put("city", h);
							return maps; })
				.channel("cityNameChannel")
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
	
	// -- SOAP --
	@Bean
	@ServiceActivator(inputChannel = "addSoapChannel")
	public MessageHandler addChannelHandler() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.integration.soap.obj");

		MarshallingWebServiceOutboundGateway outboundGateway = new MarshallingWebServiceOutboundGateway(
				env.getProperty("soap.add.uri"), marshaller);
		WebServiceMessageCallback messageCallback = new SoapActionCallback(env.getProperty("soap.add.action"));

		SaajSoapMessageFactory saajSoapMessageFactory = new SaajSoapMessageFactory();
		saajSoapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
		saajSoapMessageFactory.afterPropertiesSet();

		DefaultSoapHeaderMapper defHeader = new DefaultSoapHeaderMapper();
		defHeader.setRequestHeaderNames("SOAPAction");

		outboundGateway.setMessageFactory(saajSoapMessageFactory);
		outboundGateway.setHeaderMapper(defHeader);
		outboundGateway.setRequestCallback(messageCallback);

		return outboundGateway;
	}

	@Bean
	public IntegrationFlow addNumber() {
		return IntegrationFlows.from("addSoapChannel")
				.<String, Add>transform(payload -> Add.builder().intA(Integer.parseInt(payload.split(":")[0]))
						.intB(Integer.parseInt(payload.split(":")[1])).build())
				.handle(addChannelHandler())
				.transform(Transformers.toJson())
				.transform("#jsonPath(payload, '$.addResult')").get();
	}
	
	// -- EMAIL -- 
}
