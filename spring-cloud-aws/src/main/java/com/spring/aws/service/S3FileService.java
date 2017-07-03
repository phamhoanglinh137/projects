package com.spring.aws.service;

import org.springframework.web.multipart.MultipartFile;

import com.spring.aws.repo.entity.CustomerImage;

/**
 * 
 * @author linhpham
 *
 */
public interface S3FileService {
	
	public CustomerImage saveFileToS3(MultipartFile multipartFile);
	
	public void deleteImageFromS3(CustomerImage customerImage);
}
