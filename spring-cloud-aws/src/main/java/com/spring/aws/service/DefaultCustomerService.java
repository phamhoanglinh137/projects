package com.spring.aws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.aws.model.CustomerVO;
import com.spring.aws.repo.CustomerRepository;
import com.spring.aws.repo.entity.Customer;

/**
 * 
 * @author linhpham
 *
 */
@Service
public class DefaultCustomerService implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private S3FileService s3FileService;
	
	@Override
	public CustomerVO findCustomerByFirstName(String firstName) {
		List<Customer> customerLs = customerRepository.findByFirstName(firstName);
		return null;
	}

	@Override
	public void registerCustomer(CustomerVO newCustomer) {
		s3FileService.saveFileToS3(null);
	}
}
