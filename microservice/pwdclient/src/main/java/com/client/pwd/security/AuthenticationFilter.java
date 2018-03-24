package com.client.pwd.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.Getter;
import lombok.Setter;

/**
 * post login filter and make sure all request with valid token.
 * It will populate principal after token is validated in auth server. 
 * 
 * @author phamhoanglinh
 *
 */
@Setter @Getter
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	private RemoteTokenServices remoteTokenServices;

	public AuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String token = request.getHeader("token");
		OAuth2Authentication oAuth2Authentication = remoteTokenServices.loadAuthentication(token);
		oAuth2Authentication.setDetails(token);
		return oAuth2Authentication;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response); // continue Chain after Successful Authentication
	}
}