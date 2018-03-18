package com.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * 
 * @author phamhoanglinh
 *
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient //https://stackoverflow.com/questions/31976236/whats-the-differen	ce-between-enableeurekaclient-and-enablediscoveryclient
public class AuthApp {
	
	public static void main(String[] arguments) throws Exception {
		SpringApplication.run(AuthApp.class, arguments);
	}

	/**
	 * 
	 * security configuration
	 *
	 */
	@Configuration
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	@Order(1)
	public static class SecurityConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private UserDetailsService authUserDetailService;
		
		/*
		 * there are 3 options:
		 * - use custom Authentication Provider to do your own authentication.
		 * - use custom UserDetailService to load by user name using default DaoAuthenticationProvider.
		 * - use In Memory for testing below
		 */
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(authUserDetailService)
				.passwordEncoder(new PasswordEncoder() {
					@Override
					public boolean matches(CharSequence rawPassword, String encodedPassword) {
						return rawPassword.toString().equals(encodedPassword);
					}
					
					@Override
					public String encode(CharSequence rawPassword) {
						return rawPassword.toString();
					}
				});
		}
		
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
				.requestMatchers().antMatchers("/login", "/oauth/authorize")
            .and()
				.authorizeRequests()
							.antMatchers(HttpMethod.PUT).permitAll()
							.anyRequest().authenticated()
            .and()
            		.formLogin().permitAll();
		}
		
		@Override
		public void configure(WebSecurity web) throws Exception {
			super.configure(web);
		}
		
		@Override
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}
	}

	/**
	 * 
	 * http://projects.spring.io/spring-security-oauth/docs/oauth2.html
	 * 
	 * oauth2 security configuration
	 *
	 */
	@Configuration
	@EnableAuthorizationServer
	public static class OAuth2SecurityConfig extends AuthorizationServerConfigurerAdapter {
		
		@Autowired
		private Environment env;
		
		/*
		 * use the same authenticationManager with Security above ( default is ProviderManager)
		 */
		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;
		
		/*
		 * defines the security constraints on the token endpoint
		 * 
		 */
		@Override
		public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
			security
				.passwordEncoder(new PasswordEncoder() {
					@Override
					public boolean matches(CharSequence rawPassword, String encodedPassword) {
						return rawPassword.toString().equals(encodedPassword);
					}
					@Override
					public String encode(CharSequence rawPassword) {
						return rawPassword.toString();
					}
				})
			    .tokenKeyAccess("permitAll()")
			    .checkTokenAccess("isAuthenticated()");
		}
		
		/*
		 * A configurer that defines the client details service. Client details can be initialized, or you can just refer to an existing store.
		 * 
		 * (a callback from your AuthorizationServerConfigurer) can be used to define an in-memory or JDBC implementation of the client details service. 
		 * Important attributes of a client are :clientId, secret, scope, authorizedGrantTypes, authorities
		 * 
		 * http://www.baeldung.com/spring-security-oauth-dynamic-client-registration
		 */
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory()
					.withClient("app1")
					.secret("app1secret")
					.authorizedGrantTypes("authorization_code")
					.scopes("user_info")
					//.autoApprove(true)
			.and()
					.withClient("browser")
					.accessTokenValiditySeconds(10*60) // 10 minutes
					.refreshTokenValiditySeconds(24*60*60) // 24 hours
					.authorizedGrantTypes("refresh_token", "password")
					.scopes("ui")
			.and()
					.withClient("account-service")
					.secret("123")
					.accessTokenValiditySeconds(10*60) // 10 minutes
					.refreshTokenValiditySeconds(24*60*60) // 24 hours
					.authorizedGrantTypes("client_credentials", "refresh_token")
					.scopes("server");
		}
		
		/*
		 * defines the authorization and token endpoints and the token services.
		 * 
		 */
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
			tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
			
			endpoints
					 //.tokenServices(tokenServices()) //enable access token in UUID format 
					 .tokenStore(tokenStore())
					 .tokenEnhancer(tokenEnhancerChain)
					 .authenticationManager(authenticationManager);
			

		}
		
		/*
		 * add additional info into access token payload.
		 */
		@Bean
	    public TokenEnhancer tokenEnhancer() {
	        return new CustomTokenEnhancer();
	    }
		
		/*
		 * override in memory store, using JWT.
		 */
		@Bean
	    public TokenStore tokenStore() {
	        return new JwtTokenStore(accessTokenConverter());
	    }
		
		/*
		 * enable JWT function to generate JWT token using asymmetric(public/private key) or symmetric key.
		 * it allow to customize token payload by setAccessTokenConverter function, default is DefaultAccessTokenConverter
		 */
	    @Bean
	    public JwtAccessTokenConverter accessTokenConverter() {
	    		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
	        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(env.getProperty("token.file.path")), env.getProperty("token.file.password").toCharArray());
			converter.setKeyPair(keyStoreKeyFactory.getKeyPair(env.getProperty("token.file.keypair")));
            return converter;

	    }
	}
	
	@Configuration
	@EnableResourceServer
	public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	 
	    private static final String RESOURCE_ID = "my_rest_api";
	     
	    @Override
	    public void configure(ResourceServerSecurityConfigurer resources) {
	        resources.resourceId(RESOURCE_ID).stateless(false);
	    }
	 
	    @Override
	    public void configure(HttpSecurity http) throws Exception {
	    	http
			.requestMatchers().antMatchers("/user/**")
					.and()
						.authorizeRequests()
										.antMatchers(HttpMethod.PUT).permitAll()
										.anyRequest().authenticated()
					.and()
						.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	    }
	}
	
}