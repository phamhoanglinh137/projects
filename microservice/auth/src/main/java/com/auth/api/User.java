package com.auth.api;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude="password")
public class User {
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	private String address;
	
	private Date dateOfBirth;
}