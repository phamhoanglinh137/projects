package sp.group.apis.friend.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 
 * @author linhpham
 * 
 * generate json response
 */

@JsonPropertyOrder({ "success", "friends", "recipients", "count" })
public class Response {
	
	@JsonView(UserView.Success.class)
	private boolean success = true;
	
	@JsonView(UserView.Friends.class)
	private List<String> friends;
	
	@JsonView(UserView.Recipients.class)
	private List<String> recipients;
	
	@JsonView(UserView.Count.class)
	private int count = 0;
	
	public Response() {
		
	}
	
	public Response(List<String> friends, List<String> recipients, int count) {
		this.friends = friends;
		this.recipients = recipients;
		this.count = count;
	}
	
	public boolean isSuccess() {
		return success;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
