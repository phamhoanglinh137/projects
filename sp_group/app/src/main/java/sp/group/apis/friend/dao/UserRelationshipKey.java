package sp.group.apis.friend.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author linhpham
 *
 */

@SuppressWarnings("serial")
@Embeddable
public class UserRelationshipKey implements Serializable {
	
	@Column(name="user_id", nullable = false)
	private String userId;
	
	@Column(name="friend_id", nullable = false)
	private String friendId;

	public UserRelationshipKey(String userId, String friendId) {
		this.userId = userId;
		this.friendId = friendId;
	}
	
	public UserRelationshipKey() {
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	@Override
	public String toString() {
		return "UserRelationshipKey [userId=" + userId + ", friendId=" + friendId + "]";
	}
}
