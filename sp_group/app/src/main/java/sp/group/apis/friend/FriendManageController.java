package sp.group.apis.friend;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import sp.group.ApiException;
import sp.group.apis.friend.request.Request;
import sp.group.apis.friend.request.SendMsgReq;
import sp.group.apis.friend.request.UpdateReq;
import sp.group.apis.friend.response.Response;
import sp.group.apis.friend.response.UserView;
import sp.group.util.EmailUtil;

/**
 * 
 * @author linhpham
 *
 */
@RestController
public class FriendManageController {
	private static final Logger logger = LoggerFactory.getLogger(FriendManageController.class);
	
	@Autowired
	FriendManageService friendManageService;
	
	@JsonView(UserView.Success.class)
	@RequestMapping(value = "/connect", method = RequestMethod.PUT)
	public Response connect(@Validated @RequestBody Request rq) throws ApiException {
		logger.info("connecting called");
		rq.validate();
		friendManageService.connect(rq.getFriends().get(0), rq.getFriends().get(1));
		return new Response();
	}
	
	/**
	 * retrieve friend
	 * 
	 * @param email
	 * @return
	 * @throws ApiException
	 */
	@JsonView(UserView.Friends.class)
	@RequestMapping(value = "/friends", method = RequestMethod.GET)
	public Response retrieveFriends(@RequestParam("email") String email) throws ApiException {
		logger.info("retrieve Friends called");
		EmailUtil.validate(email);
		List<String> friends = new ArrayList<String>(friendManageService.retrieveFriend(email));
		return new Response(friends, null, friends.size());
	}
	
	/**
	 * Retrieve mutual friends - POST
	 * 
	 * Based on requirement, retrieve is POST and requestbody contains 2 Emails,
	 * but actually Retrieve mutual friends should be GET and 2 Emails are in request param. That is why I create another retrieve function 
	 * 
	 * @param rq
	 * @return
	 * @throws ApiException
	 */
	@JsonView(UserView.Friends.class)
	@RequestMapping(value = "/mutual", method = RequestMethod.POST)
	public Response retrieveMutual(@Validated @RequestBody Request rq) throws ApiException {
		logger.info("retrieve Mutual Friend - POST called");
		rq.validate();
		List<String> friends = new ArrayList<String>(friendManageService.retrieveMutual(rq.getFriends().get(0), rq.getFriends().get(1)));
		return new Response(friends, null, friends.size());
	}
	
	/**
	 * Retrieve mutual friends - GET
	 * 
	 * @param email1
	 * @param email2
	 * @return
	 * @throws ApiException
	 */
	@JsonView(UserView.Friends.class)
	@RequestMapping(value = "/mutual", method = RequestMethod.GET)
	public Response retrieveMutual(@RequestParam("email1") String email1, @RequestParam("email2") String email2) throws ApiException {
		logger.info("retrieve Mutual Friend - GET called");
		EmailUtil.validate(email1, email2);
		List<String> friends = new ArrayList<String>(friendManageService.retrieveMutual(email1, email1));
		return new Response(friends, null, friends.size());
	}
	
	
	@JsonView(UserView.Success.class)
	@RequestMapping(value = "/subscribe", method = RequestMethod.POST)
	public Response subscribe(@Validated @RequestBody UpdateReq req) throws ApiException {
		logger.info("subscribe called");
		friendManageService.subscribe(req.getRequestor(), req.getTarget());
		return new Response();
	}
	
	@JsonView(UserView.Success.class)
	@RequestMapping(value = "/block", method = RequestMethod.POST)
	public Response block(@Validated @RequestBody UpdateReq req) throws ApiException {
		logger.info("block called");
		friendManageService.block(req.getRequestor(), req.getTarget());
		return new Response();
	}
	
	@JsonView(UserView.Recipients.class)
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public Response sendMsg(@Validated @RequestBody SendMsgReq req) throws ApiException {
		logger.info("send Message called");
		List<String> recipients = friendManageService.send(req.getSender());
		return new Response(null, recipients, recipients.size());
	}
}
