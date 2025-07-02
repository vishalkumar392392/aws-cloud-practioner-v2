package com.aws.cloud.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.aws.cloud.model.Secrets;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import java.util.Properties;


@Configuration
public class AWSConfig {

	@Value("${aws.secret.name}")
	private String secretName;

	@Bean
	public AmazonSQS amazonSQSClient() {
		return AmazonSQSClientBuilder.standard()
				.withCredentials(new com.amazonaws.auth.DefaultAWSCredentialsProviderChain())
				.withRegion(Regions.US_EAST_1).build();
	}

	@Bean
	public DataSource dataSource() throws JsonMappingException, JsonProcessingException {
		Secrets secrets = this.secrets();
		
		HikariConfig config = new HikariConfig();

        config.setJdbcUrl(secrets.getDatasourceUrl());
        config.setUsername(secrets.getUsername());
        config.setPassword(secrets.getPassword());
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300_000);
        config.setMaxLifetime(1_800_000);
        config.setConnectionTimeout(30_000);
        config.setKeepaliveTime(60_000);
        config.setValidationTimeout(5_000);

        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("testWhileIdle", "true");
        config.addDataSourceProperty("testOnBorrow", "true");

        return new HikariDataSource(config);
		
	}
	
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.aws.cloud"); // Replace with your entity package
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.show_sql", "true");

        factoryBean.setJpaProperties(jpaProperties);
        return factoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

	@Bean
	public Secrets secrets() throws JsonMappingException, JsonProcessingException {
		String secrets = getSecret();
		ObjectMapper obj = new ObjectMapper();
		Secrets allSecrets = obj.readValue(secrets, Secrets.class);
		return allSecrets;
	}

	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1) // change if using other region
				.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
	}

	public String getSecret() {
		Region region = Region.of("us-east-1");
		SecretsManagerClient client = SecretsManagerClient.builder().region(region).build();
		GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).build();
		GetSecretValueResponse getSecretValueResponse;
		try {
			getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
		} catch (Exception e) {
			throw e;
		}
		String secret = getSecretValueResponse.secretString();
		return secret;
	}

}
