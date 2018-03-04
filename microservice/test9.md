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
    compile "org.springframework.retry:spring-retry" // Config Client Retry : https://cloud.spring.io/spring-cloud-config/multi/multi__spring_cloud_config_client.html
    compile "org.springframework.boot:spring-boot-starter-aop"  // Config Client Retry : https://cloud.spring.io/spring-cloud-config/multi/multi__spring_cloud_config_client.html
    
    compile "net.logstash.logback:logstash-logback-encoder"
} 

