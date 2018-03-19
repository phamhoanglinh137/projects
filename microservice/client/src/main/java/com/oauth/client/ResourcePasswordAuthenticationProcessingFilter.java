package com.oauth.client;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

/**
 * 
 * @author linhpham
 *
 */
public class ResourcePasswordAuthenticationProcessingFilter extends OAuth2ClientAuthenticationProcessingFilter {

	public ResourcePasswordAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		String username = Strings.isNotEmpty(request.getHeader("username")) ? request.getHeader("username").trim()
				: Strings.EMPTY;
		String password = Strings.isNotEmpty(request.getHeader("password")) ? request.getHeader("password")
				: Strings.EMPTY;

		ResourceOwnerPasswordResourceDetails resourcePwd = (ResourceOwnerPasswordResourceDetails) this.restTemplate
				.getResource();
		resourcePwd.setUsername(username);
		resourcePwd.setPassword(password);

		return super.attemptAuthentication(request, response);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response); // continue Chain after Successful Authentication
	}

}
