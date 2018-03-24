package com.client.pwd.api.token;

import java.util.Map;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * 
 * @author linhpham
 *
 */
@MessagingGateway
public interface TokenAuthServerGateway {
	
	@Gateway(requestChannel="accessTokenChannel")
	public OAuth2AccessToken getAccessToken(@Header("Authorization") String authorization, @Payload Map<String, String> payloads) throws Exception;
	
	@Gateway(requestChannel="checkTokenChannel")
	public OAuth2Authentication checkAccessToken(@Header("Authorization") String authorization, @Payload Map<String, String>  payloads) throws Exception;
}
