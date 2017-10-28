package sp.group.apis.friend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author linhpham
 *
 */
@RestController
public class FriendManageController {
	
	@RequestMapping(value ="/hello")
	public String test() {
		return "hello world";
	}
}
