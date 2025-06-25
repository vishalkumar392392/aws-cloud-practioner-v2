package com.aws.cloud.sqs;


import java.sql.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.aws.cloud.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class Publisher {
	
	
	 @Value("${aws.queueName}")
	    private String queueName;

	    private final AmazonSQS amazonSQSClient;
	    private final ObjectMapper objectMapper;

	    public Publisher(AmazonSQS amazonSQSClient, ObjectMapper objectMapper) {
	        this.amazonSQSClient = amazonSQSClient;
	        this.objectMapper = objectMapper;
	    }

	    public String publishMessage(String msg) {
	        try {
	            GetQueueUrlResult queueUrl = amazonSQSClient.getQueueUrl(queueName);
	            Message message = Message.builder()
	                    .id(String.valueOf(System.currentTimeMillis()))
	                    .content(msg)
	                    .createdAt(new Date(System.currentTimeMillis())).build();
	            var result = amazonSQSClient.sendMessage(queueUrl.getQueueUrl(), objectMapper.writeValueAsString(message));
	            log.info("Message Published: {} ", result);
	            return result.toString();
	        } catch (Exception e) {
	            log.error("Queue Exception Message: {}", e.getMessage());
	        }
	        
	        return null;

	    }

}
