package com.account.api;

import com.account.api.repo.User;

import reactor.core.publisher.Mono;

public interface UserService {
	public Mono<User> retrieve(String username);
	public void create(User user);
}

