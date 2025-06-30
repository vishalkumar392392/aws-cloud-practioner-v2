package com.aws.cloud.s3;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import jakarta.annotation.PostConstruct;

@Service
public class S3Service {
	
	
	@Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final AmazonS3 amazonS3;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }
    
    @PostConstruct
    public void postConstruct() {
    	bucketName = bucketName.trim();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));

        return amazonS3.getUrl(bucketName, fileName).toString();
    }
    
    public boolean deleteFile(String fileName) {
        try {
            amazonS3.deleteObject(bucketName, fileName);
            return true;
        } catch (AmazonS3Exception e) {
            System.err.println("Error deleting file: " + e.getMessage());
            return false;
        }
    }
    
    public String generatePresignedUrl(String fileName) {
        java.util.Date expiration = new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 10); // 10 mins

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

}
