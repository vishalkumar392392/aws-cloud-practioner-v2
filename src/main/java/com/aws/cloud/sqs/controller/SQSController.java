package com.aws.cloud.sqs.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.cloud.model.Message;
import com.aws.cloud.sqs.Consumer;
import com.aws.cloud.sqs.Publisher;

@RestController
@RequestMapping("/sqs")
public class SQSController {
	
	
	private final Publisher publisher;
	
	private final Consumer consumer;

	public SQSController(Publisher publisher, Consumer consumer) {
		super();
		this.publisher = publisher;
		this.consumer = consumer;
	}
	
	@PostMapping("/publish")
	public String publish(@RequestBody Message msg) {
		
		return publisher.publishMessage(msg.getContent());
		
	}
	
	@GetMapping("/consume")
	public String consume() {
		return consumer.consumeMessages();
	}

}
