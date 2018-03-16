server:
  port: 8084
app: 
  resource: 
    id: my_resource_id
spring:
  h2: 
    console: 
      enabled: true
      path: /h2  
  datasource: 
    url: jdbc:h2:file:~/accountdb
    username: sa
    password: 
  jpa:
    show-sql: false
    hibernate: 
      ddl-auto: create-drop
    properties:
      hibernate: 
        dialect: org.hibernate.dialect.H2Dialect
  --------
  
  eureka: 
  instance: 
    prefer-ip-address: true
  client:
    serviceUrl:
      defaultZone: ${EUREKA_SERVER}/eureka/
app:
  internal: 
    user: SERVER
    password: SERVER_PWD
    role: SERVER
--------
server:
  port: 8083
token: 
  file: 
    path: jwt-test.jks # store in auth module locally coz spring config server not really support binary file, it is causing format file exception during loading keystore by URL
    keypair: jwt-test
    password: mypass
spring:
  h2: 
    console: 
      enabled: true
      path: /h2  
  datasource: 
    url: jdbc:h2:file:~/authdb
    username: sa
    password: 
  jpa:
    show-sql: false
    hibernate: 
      ddl-auto: create-drop
    properties:
      hibernate: 
        dialect: org.hibernate.dialect.H2Dialect
-----
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <property name="LOG_PATH" value="${app.log.path}"/>
    <property name="FILE_NAME" value="${app.name}"/>
    
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{dd-MM-yyyy HH:mm:ss.SSS} %magenta([%thread]) %highlight(%-5level) %logger{36}.%M - %msg%n</pattern>
        </encoder>
    </appender>
    
    <appender name="SAVE-TO-FILE" class="ch.qos.logback.core.FileAppender">
        <file>${LOG_PATH}/${FILE_NAME}.log</file>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <Pattern>
                %d{dd-MM-yyyy HH:mm:ss.SSS} [%thread] %-5level %logger{36}.%M - %msg%n
            </Pattern>
        </encoder>
    </appender>
	
	<appender name="STASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
      <destination>127.0.0.1:9001</destination>

      <!-- encoder is required -->
      <encoder class="net.logstash.logback.encoder.LogstashEncoder" />
  	</appender>
  
	
    <logger name="com" additivity="false" level="debug">
	    <appender-ref ref="SAVE-TO-FILE" />
        <appender-ref ref="STDOUT" />
<!--         <appender-ref ref="STASH" /> -->
	</logger>
	
	<logger name="com.registry" additivity="false" level="error">
	    <appender-ref ref="SAVE-TO-FILE" />
        <appender-ref ref="STDOUT" />
<!--         <appender-ref ref="STASH" /> -->
	</logger>
	
<!-- 	<logger name="org.springframwork" additivity="false" level="debug"> -->
<!-- 	    <appender-ref ref="SAVE-TO-FILE" /> -->
<!--         <appender-ref ref="STDOUT" /> -->
<!--         <appender-ref ref="STASH" /> -->
<!-- 	</logger> -->

    <root level="debug">
        <appender-ref ref="STDOUT" />
<!--         <appender-ref ref="SAVE-TO-FILE" /> -->
<!--         <appender-ref ref="STASH" /> -->
    </root>

</configuration>


-------
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <property name="LOG_PATH" value="${app.log.path}"/>
    <property name="FILE_NAME" value="${app.name}"/>
    
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{dd-MM-yyyy HH:mm:ss.SSS} %magenta([%thread]) %highlight(%-5level) %logger{36}.%M - %msg%n</pattern>
        </encoder>
    </appender>
    
    <appender name="SAVE-TO-FILE" class="ch.qos.logback.core.FileAppender">
        <file>${LOG_PATH}/${FILE_NAME}.log</file>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <Pattern>
                %d{dd-MM-yyyy HH:mm:ss.SSS} [%thread] %-5level %logger{36}.%M - %msg%n
            </Pattern>
        </encoder>
    </appender>
	
	<appender name="STASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
      <destination>127.0.0.1:9001</destination>

      <!-- encoder is required -->
      <encoder class="net.logstash.logback.encoder.LogstashEncoder" />
  	</appender>
  
	

    <root level="error">
        <appender-ref ref="STDOUT" />
    </root>

</configuration>

------

server:
  port: 8082
#spring: 
#  cloud:
#    config: 
#      discovery: 
#        serviceId: config
#        enabled: true
eureka:
  instance:
    prefer-ip-address: true
  client:
    registerWithEureka: false
    fetchRegistry: false
  server: 
    waitTimeInMsWhenSyncEmpty: 0

