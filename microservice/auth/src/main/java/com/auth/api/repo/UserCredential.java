package com.auth.api.repo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "user_credential")
public class UserCredential {
	@Id
	private String username;
	
	private String password;
	
	private String role;
}
