package com.client.pwd.api.token;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author linhpham
 *
 */
@Configuration
public class TokenAuthServerConfiguration {
	
	@Autowired
	private OAuth2ProtectedResourceDetails clientAppInfo;
	
	@Autowired
	RestTemplate restTemplate;
	
	@SuppressWarnings("unchecked")
	@Bean
	public IntegrationFlow tokenFlow(){
		return IntegrationFlows.from("accessTokenChannel")
				 .handle(Http.outboundGateway(clientAppInfo.getAccessTokenUri(), restTemplate)
						.httpMethod(HttpMethod.POST)
						.mappedRequestHeaders("Authorization")
						.uriVariablesFunction(p -> ((Map<String, String>) p.getPayload()))
						.expectedResponseType(OAuth2AccessToken.class)
						.get())
				.get();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public IntegrationFlow checkTokenFlow(Environment env){
		return IntegrationFlows.from("checkTokenChannel")
				 .handle(Http.outboundGateway(env.getProperty("app.client.checkTokenUri"), restTemplate)
						.httpMethod(HttpMethod.POST)
						.uriVariablesFunction(p -> ((Map<String, String>) p.getPayload()))
						.expectedResponseType(Map.class)
						.get())
				 .<Map, OAuth2Authentication> transform(map -> {
					return new DefaultAccessTokenConverter().extractAuthentication(map);
				 })
				.get();
	}
}
