package com.auth.api;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author phamhoanglinh
 *
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public Principal get(Principal principal) {
		log.info("inside get - start {}", principal.getName());
		return principal;
	}
	
	@PutMapping
	public void create(@RequestBody @Valid User user) {
		log.info("inside create - start {}", user);
		authService.create(user);
		log.info("inside create - start {}");
	}
}
