package com.spring.aws.repo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author linhpham
 *
 */
@Entity(name="app_address")
public class Address{

	public Address(){}
	
	public Address(String address, String postCode) {
		this.address = address;
		this.postcode = postCode;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	
	@Column(name = "address", nullable = false, length=40)
	private String address;
	
	@Column(name = "postcode", nullable = false, length=40)
	private String postcode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}