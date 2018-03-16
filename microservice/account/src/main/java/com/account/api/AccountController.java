package com.account.api;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.api.repo.User;

import reactor.core.publisher.Mono;

/**
 * 
 * @author phamhoanglinh
 *
 */

@RestController
@RequestMapping(value = "/account")
public class AccountController {
	
	@Autowired
	private UserService userService;

	@GetMapping
	public Mono<User> get(Principal principal) {
		return userService.retrieve(principal.getName());
	}
	
	@PostMapping
	public void create(@RequestBody User user) {
		userService.create(user);
	}
}