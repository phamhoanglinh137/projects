package com.spring.aws.config;

import java.util.List;

import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 
 * @author linhpham
 *	configurations:
 * - RDS using @EnableRdsInstance from Spring. 3 properites are provided from system properties
 * - S3
 * - Elastic Cache
 * - SQS/SNS
 */

@Configuration
@EnableRdsInstance(databaseName = "${aws.database.name}", dbInstanceIdentifier = "${aws.database.identifier}", password = "${aws.database.password}") // RDS MYSQL credential information.
public class AwsConfiguration extends WebMvcConfigurerAdapter {
	
	/**
	 *  ignore all empty/null properties
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
	}
}
