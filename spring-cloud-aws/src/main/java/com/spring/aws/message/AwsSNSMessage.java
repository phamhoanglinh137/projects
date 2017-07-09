package com.spring.aws.message;

/**
 * 
 * @author linhpham
 * 
 * AWS SNS message object
 */

public class AwsSNSMessage {
	String type;
	String messageId;
	String topicArn;
	String subject;
	String message;
	String timestamp;
	String signatureVersion;
	String signature;
	String signingCertURL;
	String unsubscribeURL;
	MessageAttributes messageAttributes;
	
	public AwsSNSMessage() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getTopicArn() {
		return topicArn;
	}

	public void setTopicArn(String topicArn) {
		this.topicArn = topicArn;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignatureVersion() {
		return signatureVersion;
	}

	public void setSignatureVersion(String signatureVersion) {
		this.signatureVersion = signatureVersion;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSigningCertURL() {
		return signingCertURL;
	}

	public void setSigningCertURL(String signingCertURL) {
		this.signingCertURL = signingCertURL;
	}

	public String getUnsubscribeURL() {
		return unsubscribeURL;
	}

	public void setUnsubscribeURL(String unsubscribeURL) {
		this.unsubscribeURL = unsubscribeURL;
	}

	public MessageAttributes getMessageAttributes() {
		return messageAttributes;
	}

	public void setMessageAttributes(MessageAttributes messageAttributes) {
		this.messageAttributes = messageAttributes;
	}

	public static class MessageAttributes {
		Base NOTIFICATION_SUBJECT_HEADER;
		Base id;
		Base contentType;
		Base timestamp;
		
		public MessageAttributes() {
			super();
		}

		public Base getNOTIFICATION_SUBJECT_HEADER() {
			return NOTIFICATION_SUBJECT_HEADER;
		}

		public void setNOTIFICATION_SUBJECT_HEADER(Base nOTIFICATION_SUBJECT_HEADER) {
			NOTIFICATION_SUBJECT_HEADER = nOTIFICATION_SUBJECT_HEADER;
		}

		public Base getId() {
			return id;
		}

		public void setId(Base id) {
			this.id = id;
		}

		public Base getContentType() {
			return contentType;
		}

		public void setContentType(Base contentType) {
			this.contentType = contentType;
		}

		public Base getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Base timestamp) {
			this.timestamp = timestamp;
		}
	}

	public static class Base {
		public Base() {
			super();
		}

		String type;
		String value;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
