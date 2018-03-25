package com.weather;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway
public interface WeatherGateway {
	@Gateway
	public String getByCityName(@Payload String cityname);
	
}
