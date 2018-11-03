package com.integration;

import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableIntegration
@Slf4j
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
//		new SpringApplicationBuilder(Application.class)
//                .properties(getProperties())
//                .run(args);
	}
	
	static Properties getProperties() {
	      Properties props = new Properties();
	      props.put("spring.config.location", "file:/Users/linhpham/Documents/conf_temp/");
	      props.put("spring.config.name", "gateway");
	      return props;
	   }
	
	@RestController
	public static class TestController {
		@RequestMapping(value="/set") 
		public String set(HttpSession session) {
			String uuid = UUID.randomUUID().toString();
			log.info("add {} into session", uuid);
			session.setAttribute("test", uuid);
			return uuid;
		}
		
		@RequestMapping(value="/get") 
		public String get(HttpSession session) {
			log.info("getting {} from session ",session.getAttribute("test"));
			return (String)session.getAttribute("test");
		}
	}
}
