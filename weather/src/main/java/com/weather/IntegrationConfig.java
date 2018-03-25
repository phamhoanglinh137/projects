package com.weather;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.web.client.RestTemplate;


@Configuration
public class IntegrationConfig {
	
	@Autowired
	Environment env;
	
	@SuppressWarnings("unchecked")
	@Bean
	public IntegrationFlow tokenFlow(){
		return IntegrationFlows.from("cityNameChannel")
				 .handle(Http.outboundGateway(env.getProperty("weather.uri.city"), new RestTemplate())
						.httpMethod(HttpMethod.GET)
						.uriVariablesFunction(p -> ((Map<String, String>) p.getPayload()))
						.expectedResponseType(String.class)
						.get())
				.get();
	}
}
