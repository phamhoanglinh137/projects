package com.auth.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<UserCredential, String>{
	UserCredential findByUsername(String username);
}

---


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


---

package com.auth.api;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author phamhoanglinh
 *
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public Principal get(Principal principal) {
		log.info("inside get - start {}", principal.getName());
		return principal;
	}
	
	@PutMapping
	public void create(@RequestBody @Valid User user) {
		log.info("inside create - start {}", user);
		authService.create(user);
		log.info("inside create - start {}");
	}
}

--

package com.auth.api;

public interface AuthService {
	public void create(User user);
}


--


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

--

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
