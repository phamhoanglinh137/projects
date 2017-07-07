package com.spring.aws.repo.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * 
 * @author linhpham
 *
 */
@Entity(name="app_customer")
public class Customer{

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

	@Column(nullable = false, length = 30)
	private String firstName;
	
	@Column(nullable = false, length = 30)
	private String lastName;
	
	@Column(nullable = false)
	private Date dateOfBirth;
	
	@Column(nullable = false, length = 11)
	private String phone;
	
	@Column(nullable = false, length = 100)
	private String email;
	
	@OneToOne(cascade = {CascadeType.ALL})
	private CustomerImage customerImage;
	
	@OneToOne(cascade = {CascadeType.ALL})
	private Address address;
	
	public Customer(){}
	
	public Customer(String firstName, String lastName, Date dateOfBirth, String email, String phone, CustomerImage customerImage, Address address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phone = phone;
		this.customerImage = customerImage;
		this.address = address;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public CustomerImage getCustomerImage() {
		return customerImage;
	}

	public void setCustomerImage(CustomerImage customerImage) {
		this.customerImage = customerImage;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}