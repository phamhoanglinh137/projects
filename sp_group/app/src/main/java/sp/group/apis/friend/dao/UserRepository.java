package sp.group.apis.friend.dao;

import org.springframework.data.repository.CrudRepository;

/**
 * 
 * @author linhpham
 *
 */
public interface UserRepository extends CrudRepository<User, String> {
	
	public User findByEmailAddress(String email);
}
