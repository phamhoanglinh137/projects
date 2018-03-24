package com.client.pwd.api.account;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * 
 * @author phamhoanglinh
 *
 */
@MessagingGateway(name="accountGateway")
public interface AccountGateway {
	
	@Gateway(requestChannel="accountChannel")
	public Account get(@Payload String token) throws Exception;
}
