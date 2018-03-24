package com.client.pwd.security;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.client.pwd.api.token.TokenAuthServerGateway;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author phamhoanglinh
 *
 */
@Slf4j
public class ClientAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private TokenAuthServerGateway tokenAuthServerGateway;
	
	@Autowired
	private OAuth2ProtectedResourceDetails clientAppInfo;
	
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String header = null;
		try {
			header ="Basic " + new String(Base64.getEncoder().encodeToString((clientAppInfo.getClientId() + ":" + clientAppInfo.getClientSecret()).getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Could not convert String"); 
		}
		
		Map<String, String> payload = new HashMap<String, String>();
		UsernamePasswordAuthenticationToken userAuthentication = (UsernamePasswordAuthenticationToken) authentication;
		payload.put("username", (String) userAuthentication.getPrincipal());
		payload.put("password", (String)userAuthentication.getCredentials());
		payload.put("grant_type", "password");
		
		OAuth2Authentication result = null;
		try {
			OAuth2AccessToken accessToken = tokenAuthServerGateway.getAccessToken(header, payload);
			
			Map<String, String> token = new HashMap<String, String>(); 
			token.put("token", accessToken.getValue());
			result = tokenAuthServerGateway.checkAccessToken(header, token);
			result.setDetails(accessToken.getValue());
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BadCredentialsException(e.getMessage()); 
		}
		
		return result;
	}


}