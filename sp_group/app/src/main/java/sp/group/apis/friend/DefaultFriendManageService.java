/**
 * 
 */
package sp.group.apis.friend;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import sp.group.ApiException;
import sp.group.Error;
import sp.group.apis.friend.dao.User;
import sp.group.apis.friend.dao.UserRelationshipService;
import sp.group.apis.friend.dao.UserService;

/**
 * @author linhpham
 *
 */
public class DefaultFriendManageService implements FriendManageService {
	private static final Logger logger = LoggerFactory.getLogger(DefaultFriendManageService.class);
	
	@Autowired 
	private UserService userService;
	
	@Autowired 
	private UserRelationshipService userRelationshipService;
	
	@Override
	@Transactional
	public void connect(String email1, String email2) {
		User user1 = userService.persist(email1);
		User user2 = userService.persist(email2);
		
		userRelationshipService.connect(user1.getUserId(), user2.getUserId());
	}

	@Override
	public void subscribe(String requestor, String target) throws ApiException {
		User user1 = retrieve(requestor);
		User user2 = retrieve(target);
		
		userRelationshipService.subscribe(user1.getUserId(), user2.getUserId());
	}

	@Override
	public void block(String requestor, String target) throws ApiException {

	}

	@Override
	public Set<String> retrieveFriend(String email) throws ApiException {
		return null;
	}

	@Override
	public List<String> retrieveMutual(String email) throws ApiException {
		return null;
	}

	@Override
	public List<String> send(String sender) throws ApiException {
		return null;
	}

	@Override
	public User retrieve(String email) throws ApiException {
		User user = userService.find(email);
		if(user == null) {
			logger.error("{} not existed", email);
			throw new ApiException(HttpStatus.FORBIDDEN, Error.EMAIL_NOT_EXISTED);
		}
		return user;
	}

}
