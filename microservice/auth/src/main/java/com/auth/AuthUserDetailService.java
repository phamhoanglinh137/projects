package com.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.api.repo.AuthRepository;
import com.auth.api.repo.UserCredential;

@Service
public class AuthUserDetailService implements UserDetailsService {
	
	@Autowired
	private AuthRepository authRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserCredential credential = authRepo.findByUsername(username);
		return credential != null ? User.withUsername(username).password(credential.getPassword()).roles(credential.getRole()).build(): null;
	}

}

