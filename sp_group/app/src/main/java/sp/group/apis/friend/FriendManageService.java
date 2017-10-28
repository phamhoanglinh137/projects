package sp.group.apis.friend;

import java.util.List;
import java.util.Set;

import sp.group.ApiException;
import sp.group.apis.friend.dao.User;

/**
 * 
 * @author linhpham
 * 
 * Service to manage all Friend activities ( connect, block, retrieve friends or mutual friends,..)
 * 
 */
public interface FriendManageService {
	
	void connect(String email1, String email2);
	
	void subscribe(String requestor, String target);
	
	void block(String requestor, String target) throws ApiException;
	
	Set<String> retrieveFriend(String email) throws ApiException;
	
	List<String> retrieveMutual(String email) throws ApiException;
	
	List<String> send(String sender) throws ApiException;;
	
	User retrieve(String email) throws ApiException;

}
