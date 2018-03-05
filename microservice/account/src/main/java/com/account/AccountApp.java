package com.account;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 
 * @author linhpham
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AccountApp {
	public static void main(String[] arguments) {
		SpringApplication.run(AccountApp.class, arguments);
	}

	@Configuration
	@EnableResourceServer
	public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		private static final String RESOURCE_ID = "my_rest_api";

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			resources.resourceId(RESOURCE_ID).stateless(false).tokenServices(tokenServices());
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.requestMatchers().antMatchers("/user/**").and().authorizeRequests().anyRequest().authenticated().and()
					.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
		}

		@Bean
		@Primary
		public DefaultTokenServices tokenServices() {
			DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
			defaultTokenServices.setTokenStore(tokenStore());
			return defaultTokenServices;
		}

		@Bean
		public TokenStore tokenStore() {
			return new JwtTokenStore(accessTokenConverter());
		}

		@Bean
		public JwtAccessTokenConverter accessTokenConverter() {
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			Resource resource = new ClassPathResource("public-key");
			String publicKey = null;
			try {
				publicKey = IOUtils.toString(resource.getInputStream());
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
			converter.setVerifierKey(publicKey);
			return converter;
		}
	}
}