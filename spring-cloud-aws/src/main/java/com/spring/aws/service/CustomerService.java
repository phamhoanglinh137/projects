package com.spring.aws.service;

import java.util.List;

import com.spring.aws.model.CustomerVO;

/**
 * 
 * @author linhpham
 *
 */
public interface CustomerService {
	public List<CustomerVO> findCustomerByFirstName(String firstName);
	
	public void registerCustomer(CustomerVO newCustomer);
}
