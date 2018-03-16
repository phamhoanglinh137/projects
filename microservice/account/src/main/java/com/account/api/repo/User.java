package com.account.api.repo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@Table(name="user")
@NoArgsConstructor @AllArgsConstructor
public class User {
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String userId;
	
	@Column(name="name")
	private String username;
	
	private String address;
	
	private String nric;
	
	@Column(name="birth")
	private Date dateOfBirth;
}