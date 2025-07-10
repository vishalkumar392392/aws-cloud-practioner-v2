# Aws Cloud Practioner
# AWS Developer Assosicate
# AWS Services Integration with Spring boot

In this spring boot application we integrated AWS services like S3, Secretsmanager, SQS.
  1. We are not storing any sensitive information in the application.properties.
  2. We are storing all the sensitive information like database url, username, password, bucketName, queueName in AWS SecretsManager.
  3. We are reading everything from Secrets Manger and configuring in our spring config class.
  4. We are just mentioning secret-name of AWS SecretsManager which is not a problem.
How to run the Application in local
  1. We have download aws cli and configure with accessKey and secretkey in our local, then aws creds will store inside our localmachine under ./aws/credentials.
  2. So when our spring boot starts, it fetches details from ./aws/credentials and connects to AWS.
How to run the Application in AWS/CLOUD
  1. We created an AWS CloudFormation template in which we configured traditional web application architecture.
  2. We configured AWS IAM roles for permissions to connect with Cloudwatch, SQS, SecretsManager, S3 and security policies and installing java,     cloudwatchagent,maven,and configured EC2 instance, ALB, ASG.

     <img width="987" alt="image" src="https://github.com/user-attachments/assets/61473915-304e-4746-9eb7-b5050fa02664" />

