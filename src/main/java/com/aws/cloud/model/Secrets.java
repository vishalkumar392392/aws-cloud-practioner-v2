package com.aws.cloud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Secrets {
	
	private String username;
	private String password;
	private String datasourceUrl;

}
