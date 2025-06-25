package com.aws.cloud.sqs.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.cloud.model.Message;
import com.aws.cloud.sqs.Publisher;

@RestController
@RequestMapping("/sqs")
public class SQSController {
	
	
	private final Publisher publisher;

	public SQSController(Publisher publisher) {
		super();
		this.publisher = publisher;
	}
	
	@PostMapping("/publish")
	public String publish(@RequestBody Message msg) {
		
		return publisher.publishMessage(msg.getContent());
		
	}

}
