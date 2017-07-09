package com.spring.aws.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;
import com.spring.aws.exception.AwsCloudException;
import com.spring.aws.message.SnsNotificationSender;
import com.spring.aws.model.CustomerVO;
import com.spring.aws.repo.CustomerRepository;
import com.spring.aws.repo.entity.Address;
import com.spring.aws.repo.entity.Customer;
import com.spring.aws.repo.entity.CustomerImage;

/**
 * 
 * @author linhpham
 *
 */
@Service
public class DefaultCustomerService implements CustomerService {
	Logger log = LoggerFactory.getLogger(DefaultCustomerService.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private S3FileService s3FileService;
	
//	@Autowired
//	private SqsQueueSender sender;
	
	@Autowired
	private SnsNotificationSender snsNotificationSender;
	
	@Override
	public List<CustomerVO> findCustomerByFirstName(String firstName) {
		List<Customer> customerLs = customerRepository.findByFirstName(firstName);
		List<CustomerVO> customerVOLs = new ArrayList<CustomerVO>();
		customerLs.forEach(customer -> {
			customerVOLs.add(CustomerVO.builder().withFirstName(customer.getFirstName()).withLastName(customer.getLastName()).withDateOfBirth(customer.getDateOfBirth())
					.withPhone(customer.getPhone()).withEmail(customer.getEmail()).withAddress(customer.getAddress().getAddress()).withPostcode(customer.getAddress().getPostcode())
				    .withImageUrl(customer.getCustomerImage().getUrl()).build());
		});
		return customerVOLs;
	}

	@Override
	public void registerCustomer(CustomerVO newCustomer) {
		log.info("register new Customer {}", newCustomer);
		
		Customer customer = new Customer();
		Address address = new Address();
		CustomerImage customerImage = new CustomerImage();
		try {
		    BeanUtils.copyProperties(newCustomer, customer);
		    BeanUtils.copyProperties(newCustomer, address);
		    customerImage = s3FileService.saveFileToS3(newCustomer.getImage());
		    
		    customer.setCustomerImage(customerImage);
			customer.setAddress(address);
			customerRepository.save(customer);
			
			// send msg in SQS queue 
			//sender.send(customer.getPhone());
			
			//send msg in SNS topic
			snsNotificationSender.send("Registration", customer.getPhone());
		} catch(Exception ex) {
			log.error("Exception in register new customer", ex);
			
			if(!StringUtils.isNullOrEmpty(customerImage.getKey())) {
				s3FileService.deleteImageFromS3(customerImage.getKey());
			}
			throw new AwsCloudException(HttpStatus.INTERNAL_SERVER_ERROR, "","");
		}
	}
}
