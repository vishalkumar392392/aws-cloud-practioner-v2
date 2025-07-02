package com.aws.cloud.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class Secrets {
	
	private String username;
	private String password;
	private String datasourceUrl;
	private String bucketName;
	private String queueName;

}
