package com.client.pwd.api.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author linhpham
 *
 */

@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private AccountGateway accountGateway;
	
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public Account userDetail(Authentication authentication) throws Exception {
		return accountGateway.get((String)authentication.getDetails());
	}
}
