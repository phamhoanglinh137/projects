package com.oauth.client;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@SpringBootApplication
@EnableOAuth2Client
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
		.and().csrf().disable()
		.cors().disable()
		.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
	}

	private Filter ssoFilter() {
		ResourcePasswordAuthenticationProcessingFilter oauthClientFilter = new ResourcePasswordAuthenticationProcessingFilter("/login/auth");
		OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(clientAppInfo(), oauth2ClientContext);
		
		RemoteTokenServices tokenServices = new RemoteTokenServices();
		tokenServices.setClientId(restTemplate.getResource().getClientId());
		tokenServices.setClientSecret(restTemplate.getResource().getClientSecret());
		tokenServices.setTokenName(restTemplate.getResource().getTokenName());
		tokenServices.setRestTemplate(restTemplate);
		tokenServices.setCheckTokenEndpointUrl(env.getProperty("app.client.checkTokenUri"));
		
		oauthClientFilter.setRestTemplate(restTemplate);
		oauthClientFilter.setTokenServices(tokenServices);
		
		return oauthClientFilter;
	}

	@Bean
	@ConfigurationProperties("app.client")
	public OAuth2ProtectedResourceDetails clientAppInfo() {
		return new ResourceOwnerPasswordResourceDetails();
	}
}
