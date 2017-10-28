package sp.group.apis.friend.dao;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "sp_user")
public class User {
	@Id
	@Column(name="user_id", nullable = false)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String userId;
	
	@Column(name="email_address", nullable = false, unique = true)
	private String emailAddress;
	
	@OneToMany(mappedBy = "key.userId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	List<UserRelationship> friends;
	
	public User(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public User() {
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public List<UserRelationship> getFriends() {
		return friends;
	}

	public void setFriends(List<UserRelationship> friends) {
		this.friends = friends;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", emailAddress=" + emailAddress + "]";
	}
	
}