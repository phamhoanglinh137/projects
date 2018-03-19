package com.oauth.client;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@PostMapping("/auth")
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	public Principal accesstoken(HttpServletRequest request, Principal principal) {
		return principal;
	}
}
