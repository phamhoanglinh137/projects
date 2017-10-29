package sp.group.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import sp.group.ApiException;
import sp.group.Error;

/**
 * 
 * @author linhpham
 * 
 */
public class EmailUtil {
	private static Logger logger = LoggerFactory.getLogger(EmailUtil.class);
	
	private final static String EMAIL_PATTERN = "^[a-z0-9._-]+@[a-z0-9-]+(?:\\.[a-z0-9-]+)*$";
	
	/**
	 * validate email: not null, max length, pattern
	 * 
	 * @param emails
	 * @throws ApiException
	 */
	public static void validate(String... emails) throws ApiException {
		for (String email : emails) {
			if(StringUtils.isEmpty(email)) {
				logger.error("Email Address is empty");
				throw new ApiException(HttpStatus.BAD_REQUEST, Error.VALIDATION_ERR);
			}
			
			if(email.length() > 100) {
				logger.error("Email Address is more than 100 characters");
				throw new ApiException(HttpStatus.BAD_REQUEST, Error.VALIDATION_ERR);
			}
			
			if(!email.matches(EMAIL_PATTERN)) {
				logger.error("Email Address is not correct format");
				throw new ApiException(HttpStatus.BAD_REQUEST, Error.VALIDATION_ERR);
			}
		}
	}
}
