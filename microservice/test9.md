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

sourceCompatibility = 1.8

repositories {
    mavenCentral()
    maven { url "https://repo.spring.io/milestone" }
}

ext {
	springCloudVersion = 'Finchley.M8'
	//springCloudVersion='Brixton.RELEASE'
}

dependencies {
	compile "org.springframework.cloud:spring-cloud-starter-config"
	compile "org.springframework.cloud:spring-cloud-starter-openfeign"
	compile "org.springframework.cloud:spring-cloud-starter-netflix-eureka-client"
	
	compile "org.springframework.security.oauth:spring-security-oauth2:2.2.1.RELEASE" //enable OAuth2
	compile "org.springframework.security:spring-security-jwt:1.0.9.RELEASE" //enable JWT token
	
	compile "org.springframework.boot:spring-boot-starter-security"
	compile "org.springframework.boot:spring-boot-starter-data-jpa"
    compile "org.springframework.boot:spring-boot-starter-web"
    compile "org.springframework.retry:spring-retry" // Config Client Retry : https://cloud.spring.io/spring-cloud-config/multi/multi__spring_cloud_config_client.html
    compile "org.springframework.boot:spring-boot-starter-aop"  // Config Client Retry : https://cloud.spring.io/spring-cloud-config/multi/multi__spring_cloud_config_client.html
    
    compile "net.logstash.logback:logstash-logback-encoder:4.9"
    compile 'com.h2database:h2'
    compile "org.projectlombok:lombok:1.16.20"
    compile "commons-io:commons-io:2.5"
     
    compile "net.logstash.logback:logstash-logback-encoder"
} 

dependencyManagement {
	imports {
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
}

