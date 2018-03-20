package com.oauth.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.oauth.client.security.AuthenticationFilter;
import com.oauth.client.security.ResourcePasswordAuthenticationProcessingFilter;

@SpringBootApplication
@EnableAutoConfiguration
@EnableOAuth2Client
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ClientApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
	
	@Autowired
	OAuth2ClientContext oauth2ClientContext;
	
	@Autowired
	Environment env;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().antMatchers("/**")
		.and()
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
		.addFilterBefore(postLoginFilter(), BasicAuthenticationFilter.class);
	}
	
    private ResourcePasswordAuthenticationProcessingFilter ssoFilter() {
		ResourcePasswordAuthenticationProcessingFilter oauthClientFilter = new ResourcePasswordAuthenticationProcessingFilter("/login/auth");
		oauthClientFilter.setRestTemplate(restTemplate());
		oauthClientFilter.setTokenServices(tokenService());
		
		return oauthClientFilter;
	}
    
	private AuthenticationFilter postLoginFilter() {
		AuthenticationFilter postLoginFilter = new AuthenticationFilter(new SecuredApiRequestMatcher());
		postLoginFilter.setRemoteTokenServices(tokenService());
		return postLoginFilter;
	}
	
	
	@Bean
	@ConfigurationProperties("app.client")
	public OAuth2ProtectedResourceDetails clientAppInfo() {
		return new ResourceOwnerPasswordResourceDetails();
	}
	
	@Bean
	public OAuth2RestTemplate restTemplate() {
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(clientAppInfo(), oauth2ClientContext);
		return restTemplate;
	}
	
	@Bean
	public RemoteTokenServices tokenService() {
		ResourceOwnerPasswordResourceDetails resourceDetail = (ResourceOwnerPasswordResourceDetails) clientAppInfo();
		RemoteTokenServices tokenService = new RemoteTokenServices();
		tokenService.setClientId(resourceDetail.getClientId());
		tokenService.setClientSecret(resourceDetail.getClientSecret());
		tokenService.setTokenName(resourceDetail.getTokenName());
		tokenService.setCheckTokenEndpointUrl(env.getProperty("app.client.checkTokenUri"));
		return tokenService;
	}
	
	private final class SecuredApiRequestMatcher implements RequestMatcher { 
	    private AntPathRequestMatcher[] requestMatchers = {
	        new AntPathRequestMatcher("/account"),
	    }; 
	    @Override
	    public boolean matches(HttpServletRequest request) {
	      for (AntPathRequestMatcher rm : requestMatchers) {
	        if (rm.matches(request)) {
	          return true;
	        }
	      }
	      return false;
	    }
	  }
	
	
}
---
package com.oauth.client.security;

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
		synchronized(this) {
			ResourceOwnerPasswordResourceDetails resourcePwd = (ResourceOwnerPasswordResourceDetails) this.restTemplate.getResource();
			resourcePwd.setUsername(username);
			resourcePwd.setPassword(password);
			return super.attemptAuthentication(request, response);
		} 
}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response); // continue Chain after Successful Authentication
	}

}
