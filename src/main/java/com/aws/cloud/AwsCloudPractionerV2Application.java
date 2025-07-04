package com.aws.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AwsCloudPractionerV2Application {

	public static void main(String[] args) {
		SpringApplication.run(AwsCloudPractionerV2Application.class, args);
	}

}
