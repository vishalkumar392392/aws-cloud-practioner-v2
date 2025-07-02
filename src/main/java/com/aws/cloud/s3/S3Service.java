package com.aws.cloud.s3;

import java.io.IOException;
import java.net.URL;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aws.cloud.model.Secrets;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class S3Service {

	private final AmazonS3 amazonS3;
	private final Secrets secrets;

	public S3Service(AmazonS3 amazonS3, Secrets secrets) {
		this.amazonS3 = amazonS3;
		this.secrets = secrets;
	}

	public String uploadFile(MultipartFile file) throws IOException {
		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());
		amazonS3.putObject(new PutObjectRequest(secrets.getBucketName(), fileName, file.getInputStream(), metadata));
		log.info("File uploaded successfully {}", file.getOriginalFilename());
		return amazonS3.getUrl(secrets.getBucketName(), fileName).toString();
	}

	public boolean deleteFile(String fileName) {
		try {
			amazonS3.deleteObject(secrets.getBucketName(), fileName);
			log.info("File deleted successfully {}", fileName);
			return true;
		} catch (AmazonS3Exception e) {
			System.err.println("Error deleting file: " + e.getMessage());
			return false;
		}
	}

	public String generatePresignedUrl(String fileName) {
		java.util.Date expiration = new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 10); // 10 mins

		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
				secrets.getBucketName(), fileName).withMethod(HttpMethod.GET).withExpiration(expiration);

		URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}

}
