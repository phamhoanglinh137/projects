/**
 * 
 */
package sp.group.apis.friend.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author linhpham
 *
 */
@Service
public class DefaultUserService implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public User persist(String email) {
		User user = userRepository.findByEmailAddress(email);
		if(user == null) {
			user = new User(email);
			userRepository.save(user);
		}
		return user;
	}

	@Override
	public User find(String email) {
		return userRepository.findByEmailAddress(email);
	}

}
