package com.spring.aws.service;

import com.spring.aws.model.CustomerVO;

/**
 * 
 * @author linhpham
 *
 */
public interface CustomerService {
	public CustomerVO findCustomerByFirstName(String firstName);
	
	public void registerCustomer(CustomerVO newCustomer);
}
