package com.account.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author phamhoanglinh
 *
 */
public interface UserRepository extends JpaRepository<User, String> {
	User findByUsername(String username);
}
