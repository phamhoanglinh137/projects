package com.spring.aws.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.aws.model.CustomerVO;
import com.spring.aws.service.CustomerService;

/**
 * 
 * @author linhpham
 *
 */
@RestController
public class AwsController {
	Logger log = LoggerFactory.getLogger(AwsController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(value="/customer/{firstName}", method = RequestMethod.GET)
	public List<CustomerVO> getCustomerByFirstName(@PathVariable("firstName") String firstName) {
		log.info("get Customer by FirstName {}", firstName);
		return customerService.findCustomerByFirstName(firstName);
	}
	
	@RequestMapping(value = "/customers", method = RequestMethod.POST)
    public @ResponseBody CustomerVO createCustomer (            
            @RequestParam(value="firstName", required=true) String firstName,
            @RequestParam(value="lastName", required=true) String lastName,
            @RequestParam(value="dateOfBirth", required=true) @DateTimeFormat(pattern="yyyy-MM-dd") Date dateOfBirth,
            @RequestParam(value="phone", required=true) String phone,
            @RequestParam(value="email", required=true) String email,
            @RequestParam(value="address", required=true) String address,
            @RequestParam(value="postcode", required=true) String postcode,
            @RequestParam(value="image", required=true) MultipartFile image) {
		log.info("create Customer start");
		CustomerVO customerVO = CustomerVO.builder().withFirstName(firstName).withLastName(lastName).withDateOfBirth(dateOfBirth)
				.withPhone(phone).withEmail(email).withAddress(address).withPostcode(postcode).withImage(image).build();
		customerService.registerCustomer(customerVO);
		log.info("create Customer end");
		return customerVO;
	}
}
