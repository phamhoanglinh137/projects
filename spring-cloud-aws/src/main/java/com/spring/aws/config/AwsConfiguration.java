package com.spring.aws.config;

import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

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
@ImportResource("classpath:/aws-config.xml")
@EnableRdsInstance(databaseName = "${aws.database.name}", dbInstanceIdentifier = "${aws.database.identifier}", password = "${aws.database.password}")
public class AwsConfiguration {
	
}
