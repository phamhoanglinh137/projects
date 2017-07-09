This is sample for spring-aws integration
- s3
- RDS
- dynamodb
- sqs
- sns
- elastic cache

run application
{
 "region": "ap-southeast-1",
 "accessKey": "AKIAJSZKJWGB6NCBYKQ5",
 "secretKey": "sAZq9N+dk3lqhVyMP1TVd9jI28YpDXoP64sjE15to",
 "rdspwd":""
}

Simple Notification Service

1. Use topic to send/receive message by console or AWS jdk.
 - CreateTopicRequest to push message to Topic. In spring cloud aws, its wrapped into NotificationMessagingTemplate
 - SubcribeTopicRequest to subscribe to Topic. Spring cloud aws now only support HTTP/HTTPS listener, so use this class to publish SMS/Email by Topic
2. push message without topic.
  - PublishRequest to publish Msgs by sms/email/sqs queue/http(https)/application

  
  HOW TO SETUP
  1. SNS/SQS
  - create one topic
  - create SQS subscription
  2. RDS
  - create mysql db, grant permission in VPC to allow Internet access.
  - run script in project
  3. S3
  - create s3 bucket
  
 