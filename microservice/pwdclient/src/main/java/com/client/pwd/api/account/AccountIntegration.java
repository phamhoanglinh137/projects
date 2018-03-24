package com.client.pwd.api.account;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author linhpham
 *
 */

@Slf4j
@Configuration
public class AccountIntegration {
	
	@Value("${app.client.userInfoUri}")
	public String accountUrl;
	
	@Bean
	public ResponseErrorHandler acctResponseErrorHandler(){
		return new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				log.info(response.toString());
				return false;
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				log.error(response.toString());
			}
		};
	}
	
	@Bean
	public IntegrationFlow flow(){
		return IntegrationFlows.from("accountChannel")
				 .enrichHeaders(s -> s.headerExpressions(c -> c.put("Authorization", "'Bearer '.concat(payload)")))
				 .handle(Http.outboundGateway(accountUrl, new RestTemplate())
						.httpMethod(HttpMethod.GET)
						.mappedRequestHeaders("Authorization")
						.expectedResponseType(Account.class)
						.errorHandler(acctResponseErrorHandler())
						.get())
				.get();
	}
	
}