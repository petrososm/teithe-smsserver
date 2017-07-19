package com.smsserver.services.models.mobileterminated;

public class Message {

	private String messageId;
	private String message;
	private int replacements;
	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", message=" + message + ", replacements=" + replacements + "]";
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getReplacements() {
		return replacements;
	}
	public void setReplacements(int replacements) {
		this.replacements = replacements;
	}
	
}
