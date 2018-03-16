package com.auth.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth.api.repo.AuthRepository;
import com.auth.api.repo.UserCredential;
import com.auth.client.AccountClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private AccountClient accountClient;
	
	@Autowired
	private AuthRepository authRepository;
	
	@Override
	@Transactional
	public void create(User user) {
		log.info("create user {}", user);
		try {
			authRepository
					.save(UserCredential.builder().username(user.getUsername()).password(user.getPassword()).role("USER").build());
			
			accountClient.createUser(user);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
}