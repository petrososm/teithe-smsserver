package com.smsserver.services.models.mobileterminated;

public class Message {

	public String messageId;
	public String message;
	public int replacements;
	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", message=" + message + ", replacements=" + replacements + "]";
	}
	
}
