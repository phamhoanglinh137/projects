/**
 * 
 */
package sp.group.apis.friend.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import sp.group.apis.friend.FriendType;

/**
 * @author linhpham
 *
 */
public class DefaultUserRelationshipService implements UserRelationshipService {

	@Autowired
	private UserRelationshiopRepository userRelationRepo;

	@Override
	public void connect(String userId, String friendId) {
		UserRelationshipKey id1 = new UserRelationshipKey(userId, friendId);
		UserRelationshipKey id2 = new UserRelationshipKey(friendId, userId);
		persist(id1, FriendType.FRIEND, false);
		persist(id2, FriendType.FRIEND, false);
	}

	@Override
	public void subscribe(String requestorId, String targetId) {
		UserRelationshipKey id = new UserRelationshipKey(requestorId, targetId);
		persist(id, FriendType.SUBSCRIBE, false);
	}

	@Override
	public void block(String requestorId, String targetId) {
		UserRelationshipKey id = new UserRelationshipKey(requestorId, targetId);
		UserRelationship userRelation = userRelationRepo.findOne(id);
		if (userRelation != null) {
			userRelation.setBlock(true);
			userRelationRepo.save(userRelation);
		}
	}

	@Override
	public void persist(UserRelationshipKey id, FriendType type, boolean isBlock) {
		UserRelationship userRelation = userRelationRepo.findOne(id);
		if(userRelation == null) {
			userRelation = new UserRelationship(id, type, isBlock);
		} else {
			if(!FriendType.FRIEND.equals(userRelation.getType())) {
				userRelation.setType(type);
			}
			userRelation.setBlock(isBlock);
		}
		userRelationRepo.save(userRelation);
	}
	
	/**
	 * retrieve all friends or subscriber if relationship is not block.
	 */
	@Override
	public List<String> send(String senderId) {
		List<String> recipient = new ArrayList<String>();
		List<UserRelationship> users = userRelationRepo.findByKeyFriendId(senderId);
		if(users != null && users.size() > 0) {
			for (UserRelationship userRelationship : users) {
				if(!userRelationship.isBlock()) {
					recipient.add(userRelationship.getUser().getEmailAddress());
				}
			}
		}
		return recipient;
	}

}
