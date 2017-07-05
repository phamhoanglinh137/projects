package com.spring.aws.model;

import java.util.Date;

import javax.annotation.Generated;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author linhpham
 *
 */
public class CustomerVO {
	
	private String firstName; 
	private String lastName;
	private Date dateOfBirth;
	private String street;
	private String town;
	private String county;
	private String postcode;
	
	private MultipartFile image;
	private String imageKey;
	private String imageUrl;
	private String imageId;
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public String getStreet() {
		return street;
	}
	public String getTown() {
		return town;
	}
	public String getCounty() {
		return county;
	}
	public String getPostcode() {
		return postcode;
	}
	@JsonProperty("image")
	public MultipartFile getImage() {
		return image;
	}
	
	@JsonIgnore
	public MultipartFile setImage() {
		return image;
	}
	
	public String getImageKey() {
		return imageKey;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public String getImageId() {
		return imageId;
	}
	@Generated("SparkTools")
	private CustomerVO(Builder builder) {
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.dateOfBirth = builder.dateOfBirth;
		this.street = builder.street;
		this.town = builder.town;
		this.county = builder.county;
		this.postcode = builder.postcode;
		this.image = builder.image;
		this.imageKey = builder.imageKey;
		this.imageUrl = builder.imageUrl;
		this.imageId = builder.imageId;
	}
	/**
	 * Creates builder to build {@link CustomerVO}.
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}
	/**
	 * Builder to build {@link CustomerVO}.
	 */
	@Generated("SparkTools")
	public static final class Builder {
		private String firstName;
		private String lastName;
		private Date dateOfBirth;
		private String street;
		private String town;
		private String county;
		private String postcode;
		private MultipartFile image;
		private String imageKey;
		private String imageUrl;
		private String imageId;

		private Builder() {
		}

		public Builder withFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder withLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder withDateOfBirth(Date dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
			return this;
		}

		public Builder withStreet(String street) {
			this.street = street;
			return this;
		}

		public Builder withTown(String town) {
			this.town = town;
			return this;
		}

		public Builder withCounty(String county) {
			this.county = county;
			return this;
		}

		public Builder withPostcode(String postcode) {
			this.postcode = postcode;
			return this;
		}

		public Builder withImage(MultipartFile image) {
			this.image = image;
			return this;
		}

		public Builder withImageKey(String imageKey) {
			this.imageKey = imageKey;
			return this;
		}

		public Builder withImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}

		public Builder withImageId(String imageId) {
			this.imageId = imageId;
			return this;
		}

		public CustomerVO build() {
			return new CustomerVO(this);
		}
	}
	@Override
	public String toString() {
		return "CustomerVO [firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth=" + dateOfBirth
				+ ", street=" + street + ", town=" + town + ", county=" + county + ", postcode=" + postcode + "]";
	}
}
