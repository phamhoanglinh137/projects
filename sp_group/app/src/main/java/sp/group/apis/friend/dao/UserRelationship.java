package sp.group.apis.friend.dao;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import sp.group.apis.friend.FriendType;

/**
 * 
 * @author linhpham
 *
 */
@Entity
@Table(name = "sp_user_relationship")
public class UserRelationship {
	@EmbeddedId
	private UserRelationshipKey key;
	
	@Column(name="type", nullable = false)
	@Enumerated(EnumType.STRING)
	private FriendType type;
	
	@Column(name="block", nullable = false)
	private boolean block;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id", referencedColumnName="user_id", insertable = false, updatable = false)
	private User user;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="friend_id", referencedColumnName="user_id", insertable = false, updatable = false)
	private User friend;
	
	public UserRelationship() {
		
	}
	
	public UserRelationship(UserRelationshipKey key, FriendType type, boolean block) {
		this.key = key;
		this.type = type;
		this.block = block;
	}

	public UserRelationshipKey getKey() {
		return key;
	}

	public void setKey(UserRelationshipKey key) {
		this.key = key;
	}

	public FriendType getType() {
		return type;
	}

	public void setType(FriendType type) {
		this.type = type;
	}

	public boolean isBlock() {
		return block;
	}

	public void setBlock(boolean block) {
		this.block = block;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}
	
	@Override
	public String toString() {
		return "UserRelationship [key=" + key + ", type=" + type + ", block=" + block + "]";
	}
	
}

