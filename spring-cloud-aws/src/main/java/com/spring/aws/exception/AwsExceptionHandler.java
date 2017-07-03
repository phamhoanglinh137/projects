package com.spring.aws.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author linhpham
 *
 */
@ControllerAdvice
public class AwsExceptionHandler {
	
	@ExceptionHandler(SpringAwsException.class)
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Object handleSpringAwsException(SpringAwsException ex) {
		return ex;
	}
}
