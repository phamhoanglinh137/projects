package sp.group.apis.friend.dao;

/**
 * 
 * @author linhpham
 *
 */
public interface UserService {
	
	User persist(String email);

	User find(String email);
}
