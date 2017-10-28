package sp.group.apis.friend;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sp.group.ApiException;

/**
 * 
 * @author linhpham
 *
 */
@RestController
public class FriendManageController {
	
	@Autowired
	FriendManageService friendManageService;
	
	@RequestMapping(value ="/connect")
	public String connect() {
		friendManageService.connect("test1@example.com", "test2@example.com");
		return "hello world";
	}
	
	@RequestMapping(value ="/retrieveßßßßßß")
	public Set<String> retrieve() throws ApiException {
		return friendManageService.retrieveFriend("test1@example.com");
	}
}
