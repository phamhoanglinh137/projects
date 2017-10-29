package sp.group.apis.friend.request;

import java.util.List;

import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import sp.group.ApiException;
import sp.group.Error;
import sp.group.util.EmailUtil;

public class Request {
	private static Logger logger = LoggerFactory.getLogger(Request.class);
	
	@Size(min = 2, max = 2)
	private List<String> friends;

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}
	
	public void validate() throws ApiException {
		//verify email format and length		
		EmailUtil.validate(friends.get(0), friends.get(1));
		
		//verify duplicated email in list.
		if(friends.get(0).equalsIgnoreCase(friends.get(1))) {
			logger.error("Email Address is duplicated");
			throw new ApiException(HttpStatus.BAD_REQUEST, Error.VALIDATION_ERR);
		} 
		
	}
}
