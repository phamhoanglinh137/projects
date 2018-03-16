package com.auth.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.auth.api.User;

import feign.auth.BasicAuthRequestInterceptor;
import lombok.extern.slf4j.Slf4j;

@FeignClient(name = "account", fallback = AccountClientCallback.class, configuration = AccountClientConfiguration.class)
public interface AccountClient {
	@RequestMapping(method = RequestMethod.POST, value = "/account", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	void createUser(User user) throws Exception;
}

@Slf4j
@Component
class AccountClientCallback implements AccountClient {

	@Override
	public void createUser(User user) throws Exception {
		log.error("calling account service issue !");
		throw new Exception("calling account service issue");
	}
    
}

@Configuration
class AccountClientConfiguration  {
	@Autowired
	private Environment enviroment;
	@Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(enviroment.getProperty("app.internal.user"), enviroment.getProperty("app.internal.password"));
    }
}
