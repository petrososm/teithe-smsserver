package com.smsserver.models.site;

public class SmsTemplate {

	public String messageId;
	public String message;
	public SmsTemplate(String messageId, String message) {
		super();
		this.messageId = messageId;
		this.message = message;
	}
	public SmsTemplate(){};
}
