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
@Entity(name="app_customer_image")
public class CustomerImage {

	public CustomerImage(){}
	
	public CustomerImage(String key, String url) {
		this.key = key;
		this.url =url;		
	}

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	
	@Column(name = "s3_key", nullable = false, length=200)
	private String key;
	
	@Column(name = "url", nullable = false, length=1000)
	private String url;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}