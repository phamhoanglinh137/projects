package sp.group.apis.friend.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 * 
 * @author linhpham
 *
 */
public interface UserRelationshiopRepository extends CrudRepository<UserRelationship, UserRelationshipKey> {
	
	public List<UserRelationship> findByKeyFriendId(String friendId);
}
