package sp.group.apis.friend.dao;

import java.util.List;

import sp.group.apis.friend.FriendType;

public interface UserRelationshipService {
	
	void connect(String userId, String friendId);
	
	void subscribe(String requestorId, String targetId);
	
	void block(String requestorId, String targetId);
	
	void persist(UserRelationshipKey id, FriendType type, boolean isBlock);
	
	List<String> send(String senderId);
}
