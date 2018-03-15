package com.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.WeatherConfig.MsgGateway;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@Configuration
@EnableIntegration
@Slf4j
public class WeatherApplication {

	public static void main(String[] args) {
		System.setProperty("http.proxyHost", "10.5.127.23");
		System.setProperty("http.proxyPort", "80");
		System.setProperty("http.proxyUser", "phamhoanglinh");
		System.setProperty("http.proxyPassword", "123Welcome");
		SpringApplication.run(WeatherApplication.class, args);
	}
	
	@RestController
	public static class Controller {

		@Autowired
		private MsgGateway msgGateway;

		@GetMapping(path = "/weather")
		public String send(@RequestParam("country") String country) {
			log.info("send...{}", country);
//			return msgGateway.send(Request.builder().countryName(country).build());
			return msgGateway.send(country);
		}
	}

	@Data
	@Builder
	public static class Request {
		private String countryName;
	}
}


----------------

package com.weather;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.integration.http.support.DefaultHttpHeaderMapper;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.client.ResponseErrorHandler;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
@Profile("default")
@Configuration
public class WeatherConfig {


	@MessagingGateway(defaultRequestChannel = "channel")
	public static interface MsgGateway {
		//String send(@Payload Request payload);
		String send(@Payload String payload);
	}
	
	@Bean
	public MessageChannel channel(@Autowired WireTap wireTap) {
		return MessageChannels.direct().interceptor(wireTap).get();
	}

	@Bean
	@ServiceActivator(inputChannel = "channel")
	public MessageHandler handleMsg() {

		HttpRequestExecutingMessageHandler messageHandler = new HttpRequestExecutingMessageHandler(
				"http://www.webservicex.net/globalweather.asmx/GetCitiesByCountry?CountryName={CountryName}");
		SpelExpressionParser expressionParser = new SpelExpressionParser();
		Map<String, Expression> uriVariableExpressions = new HashMap<String, Expression>();
		uriVariableExpressions.put("CountryName", expressionParser.parseExpression("payload"));

		DefaultHttpHeaderMapper defaultHttpHeaderMapper = new DefaultHttpHeaderMapper();
		defaultHttpHeaderMapper.setOutboundHeaderNames(new String[] { "" });
		defaultHttpHeaderMapper.setUserDefinedHeaderPrefix("");

		messageHandler.setRequestFactory(ntbBufferingClientHttpRequestFactory());
		messageHandler.setHeaderMapper(defaultHttpHeaderMapper);
		messageHandler.setExpectedResponseType(String.class);
		messageHandler.setErrorHandler(new ResponseErrorHandler() {

			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				log.error("hasError ::: {}", response.toString());
				return false;
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				log.error("handleError ::: {}", response.toString());
			}
		});

		// messageHandler.setOutputChannel(fxRateReplyChannel());
		messageHandler.setUriVariableExpressions(uriVariableExpressions);
		messageHandler.setHttpMethod(HttpMethod.GET);
		messageHandler.setEncodeUri(false);

		return messageHandler;
	}

	@Bean
	public BufferingClientHttpRequestFactory ntbBufferingClientHttpRequestFactory() {
		return new BufferingClientHttpRequestFactory(ntbOkHttpClientHttpRequestFactory());
	}

	@Bean
	public OkHttp3ClientHttpRequestFactory ntbOkHttpClientHttpRequestFactory() {
		OkHttp3ClientHttpRequestFactory okClientRequestFactory = new OkHttp3ClientHttpRequestFactory(
				new OkHttpClient());
		okClientRequestFactory.setReadTimeout(60 * 100);
		okClientRequestFactory.setConnectTimeout(60 * 100);
		okClientRequestFactory.setWriteTimeout(60 * 100);
		return okClientRequestFactory;
	}
	
	
	// logging ------
	
	  @Bean
	  public WireTap wireTap() {
	    return new WireTap(gatewayLogging());
	  }

	  @ServiceActivator(inputChannel = "gatewayLogging")
	  @Bean
	  public LoggingHandler loggingHandler() {
	    LoggingHandler loggingHandler = new LoggingHandler(Level.DEBUG);
	    loggingHandler.setShouldLogFullMessage(true);
	    return loggingHandler;
	  }

	  @Bean
	  public MessageChannel gatewayLogging() {
	    return new DirectChannel();
	  }
}

-------------

package com.weather;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.dsl.IntegrationFlow;

@Profile("dsl")
@Configuration
public class WeatherDslConfig {
	
	public IntegrationFlow flow () {
		return f -> f.transform(payload ->
        "<FahrenheitToCelsius xmlns=\"https://www.w3schools.com/xml/\">"
      +     "<Fahrenheit>" + payload + "</Fahrenheit>"
      + "</FahrenheitToCelsius>");
	}
}

----------------
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

group = 'com.weather'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.8

repositories {
	mavenCentral()
	maven { url "https://repo.spring.io/milestone" }
}


ext {
	springCloudVersion = 'Finchley.M8'
}

dependencies {
	compile('org.springframework.boot:spring-boot-starter-integration')
	compile("org.springframework.integration:spring-integration-java-dsl:1.2.3.RELEASE")
	compile('org.springframework.integration:spring-integration-http')
	compile('org.springframework.integration:spring-integration-ws')
	
	compile('org.springframework.boot:spring-boot-starter-web')
	compile('org.springframework.cloud:spring-cloud-starter-netflix-zuul')
	
	compile("com.squareup.okhttp3:okhttp:3.10.0")
	compile "org.projectlombok:lombok:1.16.20"
	
	testCompile('org.springframework.boot:spring-boot-starter-test')
}

dependencyManagement {
	imports {
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
}
