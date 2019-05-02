package com.csye6225.noteapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.csye6225.noteapp.model.AttachmentFile;

@Service
public class AmazonS3Example {

	@Value("${bucket.name}")
	private String bucketName;

	public AttachmentFile save(MultipartFile file, AttachmentFile attachment) {
		//AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
		AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient(); 
		s3client.putObject(new PutObjectRequest(bucketName, attachment.getFileName(), convertMultiPartToFile(file)));
		attachment.setFilePath(s3client.getUrl(bucketName, attachment.getFileName()).toString());
		return attachment;
	}

	private File convertMultiPartToFile(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(convFile);

			fos.write(file.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convFile;
	}

	public void delete(String attachmentName) {
		// TODO Auto-generated method stub
		//AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
		AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient();
		s3client.deleteObject(new DeleteObjectRequest(bucketName, attachmentName));
	}
}