package sp.group.apis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import sp.group.ApiException;

/**
 * 
 * @author linhpham
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	/**
	 * handle API exceptions
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = ApiException.class)
	public ResponseEntity<Object> handleAppException(ApiException ex) {
		ApiException.ErrorResponse error = ex.getErrorResponse();
		if(error != null) {
			logger.error("{} : {} ({})", ex.getHttpStatus(), error.getCode(), error.getMessage());
			
		}
		return new ResponseEntity<Object>(error, ex.getHttpStatus());
	}
	
	/**
	 *  handle all uncatched Exception
	 * @param ex
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleUncatchException(Exception ex) {
		logger.error("Internal Server Exception thrown :", ex);
	}
}
