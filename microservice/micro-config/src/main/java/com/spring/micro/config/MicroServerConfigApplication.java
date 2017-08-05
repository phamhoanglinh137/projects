package com.spring.micro.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 
 * @author linhpham
 *
 */

@SpringBootApplication
@EnableConfigServer
public class MicroServerConfigApplication {
	public static void main(String[] args) {
		SpringApplication.run(MicroServerConfigApplication.class, args);
	}
}
