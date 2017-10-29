package sp.group.apis.friend.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UpdateReq {
	
	@NotNull
	@Size(max = 100)
	@Pattern(regexp = "^[a-z0-9._-]+@[a-z0-9-]+(?:\\.[a-z0-9-]+)*$")
	private String requestor;
	
	@NotNull
	@Size(max = 100)
	@Pattern(regexp = "^[a-z0-9._-]+@[a-z0-9-]+(?:\\.[a-z0-9-]+)*$")
	private String target;
	
	public String getRequestor() {
		return requestor;
	}

	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
