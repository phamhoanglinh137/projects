/**
 * 
 */
package sp.group.apis.friend;

import java.util.ArrayList;
import java.util.HashSet;
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
import sp.group.apis.friend.dao.UserRelationship;
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
		User user1 = retrieve(requestor);
		User user2 = retrieve(target);
		
		userRelationshipService.block(user1.getUserId(), user2.getUserId());
	}

	@Override
	public Set<String> retrieveFriend(String email) throws ApiException {
		Set<String> friends = new HashSet<String>();
		User user = retrieve(email);
		List<UserRelationship> userRelations = user.getFriends();
		for (UserRelationship userRelation : userRelations) {
			if(FriendType.FRIEND.equals(userRelation.getType())) {
				friends.add(userRelation.getFriend().getEmailAddress());
			}
		}
		return friends;
	}

	@Override
	public List<String> retrieveMutual(String email1, String email2) throws ApiException {
		List<String> mutualFriends = new ArrayList<String>();
		Set<String> friend1 = retrieveFriend(email1);
		Set<String> friend2 = retrieveFriend(email2);
		
		if(friend1.size() > 0 && friend2.size() > 0) {
			for(String friend: friend2) {
				if(friend1.contains(friend)) {
					mutualFriends.add(friend);
				}
			}
		}
		return mutualFriends;
	}

	@Override
	public List<String> send(String senderEmail) throws ApiException {
		User sender = retrieve(senderEmail);
		return userRelationshipService.send(sender.getUserId());
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
