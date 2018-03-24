package com.client.pwd.api.auth;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public Principal accesstoken(Principal principal) {
		return principal;
	}
	
}