package com.spring.aws.exception;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author linhpham
 *
 */
public class AwsCloudException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private HttpStatus status = HttpStatus.BAD_REQUEST;
	
	private String errorCode;
	
	private String errorMsg;
	
	public AwsCloudException(String errorCode, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
    }
	
	public AwsCloudException(HttpStatus status, String errorCode, String errorMsg) {
		super();
        this.status = status;
        this.errorCode = errorCode;
		this.errorMsg = errorMsg;
    }
	
	public AwsCloudException(String errorCode, String errorMsg) {
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

	public HttpStatus getHttpCode() {
		return status;
	}
	
}
