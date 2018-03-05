package com.account.api;


import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author linhpham
 *
 */
@RestController
@RequestMapping("/user")
public class AccountController {
	
	public UserInfo getUserDetail(Principal principal) {
		return UserInfo.builder().userName(principal.getName()).build();
	}
	
}
