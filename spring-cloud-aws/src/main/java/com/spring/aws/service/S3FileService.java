package com.spring.aws.service;

import org.springframework.web.multipart.MultipartFile;

import com.spring.aws.repo.entity.CustomerImage;

/**
 * 
 * @author linhpham
 *
 */
public interface S3FileService {
	
   CustomerImage saveFileToS3(MultipartFile multipartFile) throws Exception;
   void deleteImageFromS3(String imageKey);
}
