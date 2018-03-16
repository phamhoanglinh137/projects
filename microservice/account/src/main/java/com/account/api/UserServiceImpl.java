package com.account.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.account.api.repo.User;
import com.account.api.repo.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;

	public Mono<User> retrieve(String username) {
		User user = userRepo.findByUsername(username);
		return Mono.just(user);
	}

	@Override
	public void create(User user) {
		userRepo.save(user);
	}
}