package sp.group;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author linhpham
 *
 */
@SuppressWarnings("serial")
public class ApiException extends Exception {
	private HttpStatus httpStatus = HttpStatus.BAD_GATEWAY;

	private ErrorResponse errorResponse;
	
	public ApiException(HttpStatus httpStatus, Error error) {
		super();
		this.httpStatus = httpStatus;
		this.errorResponse = new ErrorResponse(error, error.message());
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(ErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}

	public static class ErrorResponse {
		private Error code;
		private String message;

		public ErrorResponse(Error code, String message) {
			super();
			this.code = code;
			this.message = message;
		}

		public Error getCode() {
			return code;
		}

		public void setCode(Error code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
