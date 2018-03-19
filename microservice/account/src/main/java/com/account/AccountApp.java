package com.account;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
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
 * @author phamhoanglinh
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AccountApp {
	public static void main(String[] arguments) {
        SpringApplication.run(AccountApp.class, arguments);
    }
	
	@Configuration
	@EnableWebSecurity
	public static class SecurityConfig extends WebSecurityConfigurerAdapter {
		@Autowired
		private Environment enviroment;
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication().withUser(enviroment.getProperty("app.internal.user"))
					.password(enviroment.getProperty("app.internal.password"))
					.roles(enviroment.getProperty("app.internal.role")).and()
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
			http
			.csrf().disable()
			.headers().frameOptions().sameOrigin()
			.and()
				.requestMatchers()
				.antMatchers(HttpMethod.POST,"/account/**")
			.and()
				.authorizeRequests()
				.antMatchers("/**").authenticated()
			.and()
				.httpBasic();
		}
		
		@Override
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}
	}
	
	@Configuration
	@EnableResourceServer
	public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
		
		@Autowired
		private Environment enviroment;
		
	    @Override
	    public void configure(ResourceServerSecurityConfigurer resources) {
	        resources
	        			.resourceId(enviroment.getProperty("my_resource_id")).stateless(false)
	        			.tokenServices(tokenServices());
	    }
	    
	    @Override
	    public void configure(HttpSecurity http) throws Exception {
		    http	.csrf().disable()
				.headers().frameOptions().sameOrigin()
				.and()
					.requestMatchers()
					.antMatchers(HttpMethod.GET,"/account/**")
					.antMatchers("/accountdb/**")
				.and()
					.authorizeRequests()
						.antMatchers(HttpMethod.GET, "/account/**").authenticated()
						.antMatchers("/accountdb/**").permitAll()
		    		.and()
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
	            publicKey = IOUtils.toString(resource.getInputStream(), "UTF-8");
	        } catch (final IOException e) {
	            throw new RuntimeException(e);
	        }
	        converter.setVerifierKey(publicKey);
	        return converter;
	    }
	}
}