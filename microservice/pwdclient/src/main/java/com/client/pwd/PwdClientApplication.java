package com.client.pwd;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.client.RestTemplate;

import com.client.pwd.security.ClientAuthenticationProvider;

@SpringBootApplication
@EnableAutoConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

@EnableIntegration
@IntegrationComponentScan(basePackages="{com.client.pwd}")

public class PwdClientApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(PwdClientApplication.class, args);
	}
	
	@Autowired
	Environment env;
	
	@Bean
	public AuthenticationProvider clientAuthenticationProvider() {
		return new ClientAuthenticationProvider(); 
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(clientAuthenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().antMatchers("/**")
		.and()
			.csrf().disable()
			.httpBasic()
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
    
//	private AuthenticationFilter postLoginFilter() {
//		AuthenticationFilter postLoginFilter = new AuthenticationFilter(new SecuredApiRequestMatcher());
//		return postLoginFilter;
//	}
	
	
	@Bean
	@ConfigurationProperties("app.client")
	public OAuth2ProtectedResourceDetails clientAppInfo() {
		return new ResourceOwnerPasswordResourceDetails();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
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