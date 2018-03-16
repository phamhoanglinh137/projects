package com.auth.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<UserCredential, String>{
	UserCredential findByUsername(String username);
}