buildscript {
	ext {
		springBootVersion = '2.0.0.RELEASE'
	}
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
	}
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

group = 'com.oauth.pwdclient'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.8

repositories {
	mavenCentral()
}


dependencies {
	compile('org.springframework.boot:spring-boot-starter-integration')
	//ompile("org.springframework.integration:spring-integration-java-dsl:1.2.3.RELEASE")
	compile('org.springframework.integration:spring-integration-http')
	compile('org.springframework.integration:spring-integration-ws')

	compile ('org.springframework.boot:spring-boot-starter-web')
	compile ('org.springframework.boot:spring-boot-starter-security')
	compile ('org.springframework.boot:spring-boot-autoconfigure')
	compile ('org.springframework.boot:spring-boot-configuration-processor')

	compile "org.springframework.security.oauth:spring-security-oauth2:2.2.1.RELEASE" //enable OAuth2
	compile "org.springframework.security:spring-security-jwt:1.0.9.RELEASE" //enable JWT token
	compile "org.springframework.security:spring-security-oauth2-client:5.0.3.RELEASE"

    compile "org.projectlombok:lombok:1.16.20"
    compile "commons-io:commons-io:2.5"

	testCompile('org.springframework.boot:spring-boot-starter-test')
}
---------
package com.oauth.client;

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

import com.oauth.client.security.ClientAuthenticationProvider;

@SpringBootApplication
@EnableAutoConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

@EnableIntegration
@IntegrationComponentScan(basePackages="{com.oauth.client}")

public class ClientApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
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
------
server: 
  port: 8091
  servlet:
    context-path: /client
app:  
  client: 
    clientId: app2
    clientSecret: app2secret
    accessTokenUri: http://localhost:8083/oauth/token
    checkTokenUri: http://localhost:8083/oauth/check_token
    userInfoUri: http://localhost:8084/account
logging:
  level:
    root: INFO
-----
package com.oauth.client.security;

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

import com.oauth.client.api.auth.TokenAuthServerGateway;

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
---
package com.oauth.client.security;

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
---
package com.oauth.client.api.auth;

import java.util.Map;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@MessagingGateway
public interface TokenAuthServerGateway {
	
	@Gateway(requestChannel="accessTokenChannel")
	public OAuth2AccessToken getAccessToken(@Header("Authorization") String authorization, @Payload Map<String, String> payloads) throws Exception;
	
	@Gateway(requestChannel="checkTokenChannel")
	public OAuth2Authentication checkAccessToken(@Header("Authorization") String authorization, @Payload Map<String, String>  payloads) throws Exception;
}

--
package com.oauth.client.api.auth;

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
	
	@SuppressWarnings("unchecked")
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

--

package com.oauth.client.api.auth;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@PostMapping("/auth")
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public Principal accesstoken(Principal principal) {
		return principal;
	}
	
}
--

package com.oauth.client.api.account;

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

--
package com.oauth.client.api.account;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * 
 * @author phamhoanglinh
 *
 */
@MessagingGateway(name="accountGateway")
public interface AccountGateway {
	
	@Gateway(requestChannel="accountChannel")
	public Account get(@Payload String token) throws Exception;
}
--
package com.oauth.client.api.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private AccountGateway accountGateway;
	
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public Account userDetail(Authentication authentication) throws Exception {
		return accountGateway.get((String)authentication.getDetails());
	}
}


--
package com.oauth.client.api.account;

import lombok.Data;

@Data
public class Account {
	private String username;
	private String address;
	private String nric;
}
