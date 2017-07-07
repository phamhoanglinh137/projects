package com.spring.aws.config;

import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author linhpham
 * RDS configuration using @EnableRdsInstance
 * Dynamo DB 
 */
@Configuration
@EnableRdsInstance(databaseName = "${aws.database.name}", dbInstanceIdentifier = "${aws.database.identifier}", password = "${aws.database.password}") // RDS MYSQL credential information.
public class AwsDBConfiguration {
	
}
