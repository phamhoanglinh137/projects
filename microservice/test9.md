spring:
  application:
    name: account
  profiles:
    active: local  
  cloud: 
    config: 
      uri: http://localhost:8081
      fail-fast: true
      name: ${spring.application.name}
      profile: ${spring.profiles.active}
      retry: 
        max-attempts: 5
        initial-interval: 5000
        max-interval: 10000
logging:
  config: ${spring.cloud.config.uri}/${spring.application.name}/${spring.cloud.config.profile}/master/log.xml


----------


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
	@EnableResourceServer
	public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	 
	    private static final String RESOURCE_ID = "my_rest_api";
	     
	    @Override
	    public void configure(ResourceServerSecurityConfigurer resources) {
	        resources.resourceId(RESOURCE_ID).stateless(false)
	        .tokenServices(tokenServices());
	    }
	 
	    @Override
	    public void configure(HttpSecurity http) throws Exception {
	    	http
			.requestMatchers().antMatchers("/user/**")
			.and().authorizeRequests().anyRequest().authenticated()
			.and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
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
	        Resource resource = new ClassPathResource("public-jwt-test");
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

------

buildscript {
	ext {gradlePluginVersion = '1.0.4.RELEASE'}
	ext {cloudVersion = 'Camden.SR7'}
	ext {bootVersion = '1.5.10.RELEASE'}
	
	repositories {
		mavenCentral()
    }
	
    dependencies {
        classpath "io.spring.gradle:dependency-management-plugin:${gradlePluginVersion}"
    }
}

apply plugin: 'java'
apply plugin: "io.spring.dependency-management"

repositories {
    mavenCentral()
}

dependencyManagement {
     imports {
          mavenBom "org.springframework.cloud:spring-cloud-starter-parent:${cloudVersion}"
          mavenBom "org.springframework.boot:spring-boot-starter-parent:${bootVersion}"
     }
     
      dependencies {
     	dependency "net.logstash.logback:logstash-logback-encoder:4.9"
     	dependency "org.projectlombok:lombok:1.16.20"
     	dependency "commons-io:commons-io:2.5"
     }
}
dependencies {
	compile "org.springframework.boot:spring-boot-starter-security"
	compile "org.springframework.security.oauth:spring-security-oauth2" //enable OAuth2
	compile "org.springframework.security:spring-security-jwt" //enable JWT token
	compile "org.springframework:spring-expression"
	
	compile "org.springframework.cloud:spring-cloud-starter-config"
	compile "org.springframework.cloud:spring-cloud-starter-eureka"
    compile "org.springframework.boot:spring-boot-starter-web"
    compile("org.springframework.retry:spring-retry")  // Config Client Retry : https://cloud.spring.io/spring-cloud-config/multi/multi__spring_cloud_config_client.html
    compile("org.springframework.boot:spring-boot-starter-aop")  // Config Client Retry : https://cloud.spring.io/spring-cloud-config/multi/multi__spring_cloud_config_client.html
    
    compile ("net.logstash.logback:logstash-logback-encoder")
    
    compile ("org.projectlombok:lombok")
    compile ("commons-io:commons-io")
} 

---

package com.account.api;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author phamhoanglinh
 *
 */

@RestController
@RequestMapping(value = "/user")
public class AccountController {

	@GetMapping
	public UserInfo getUserDetail(Principal principal) {
		return UserInfo.builder().username(principal.getName()).address("test").build();
	}
}


--
