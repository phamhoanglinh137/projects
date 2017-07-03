package com.spring.aws.exception;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author linhpham
 *
 */
public class SpringAwsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final static HttpStatus HTTP_CODE = HttpStatus.BAD_REQUEST;
	
	private String errorCode;
	
	private String errorMsg;
	
	public SpringAwsException(String errorCode, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
    }
	
	public SpringAwsException(String errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public static HttpStatus getHttpCode() {
		return HTTP_CODE;
	}
	
}
