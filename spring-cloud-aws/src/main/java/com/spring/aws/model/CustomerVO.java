package com.spring.aws.model;

import java.util.Date;

import javax.annotation.Generated;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author linhpham
 *
 */
public class CustomerVO {

	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String phone;
	private String email;
	private String address;
	private String postcode;

	private MultipartFile image;
	private String imageKey;
	private String imageUrl;
	private String imageId;

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

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getImageKey() {
		return imageKey;
	}

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	@Generated("SparkTools")
	private CustomerVO(Builder builder) {
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.dateOfBirth = builder.dateOfBirth;
		this.phone = builder.phone;
		this.email = builder.email;
		this.address = builder.address;
		this.postcode = builder.postcode;
		this.image = builder.image;
		this.imageKey = builder.imageKey;
		this.imageUrl = builder.imageUrl;
		this.imageId = builder.imageId;
	}

	/**
	 * Creates builder to build {@link CustomerVO}.
	 * 
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
		private String phone;
		private String email;
		private String address;
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

		public Builder withPhone(String phone) {
			this.phone = phone;
			return this;
		}

		public Builder withEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder withAddress(String address) {
			this.address = address;
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
		return "CustomerVO [firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + ", email=" + email
				+ ", address=" + address + "]";
	}
	
}
