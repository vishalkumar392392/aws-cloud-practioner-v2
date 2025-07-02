package com.aws.cloud.sqs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.aws.cloud.model.Secrets;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class Consumer {

	private final AmazonSQS amazonSQSClient;
	private final Secrets secrets;

	public Consumer(AmazonSQS amazonSQSClient, Secrets secrets) {
		this.amazonSQSClient = amazonSQSClient;
		this.secrets = secrets;
	}

	@Scheduled(cron = "*/2 * * * * *") // It runs every 2 seconds.
	public String consumeMessages() {
		try {
			String queueUrl = amazonSQSClient.getQueueUrl(secrets.getQueueName()).getQueueUrl();

			ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(queueUrl);

			if (!receiveMessageResult.getMessages().isEmpty()) {
				com.amazonaws.services.sqs.model.Message message = receiveMessageResult.getMessages().get(0);
				log.info("Read Message from queue: {}", message.getBody());
				amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
				return message.getBody();
			}

		} catch (Exception e) {
			log.error("Queue Exception Message: {}", e.getMessage());
		}
		return null;
	}

}
