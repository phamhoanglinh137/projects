package com.spring.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 
 * @author linhpham
 *
 */
@SpringBootApplication
@EnableConfigurationProperties
public class AwsApplication {
	
    public static void main(String args[]) {
    	SpringApplication.run(AwsApplication.class, args);
    }
}
