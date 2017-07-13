package com.smsserver.controllers.models.site;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SendSmsRequestDirect {
	
	
	private String[] recipients;
	private String sender;
	private String messageId;
	private String[] replacements;

	public SendSmsRequestDirect(){}

	@Override
	public String toString() {
		return "SendSmsRequestDirect [recipients=" + Arrays.toString(recipients) + ", sender=" + sender + ", messageId="
				+ messageId + ", replacements=" + Arrays.toString(replacements) + "]";
	}

	public String[] getRecipients() {
		return recipients;
	}

	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String[] getReplacements() {
		return replacements;
	}

	public void setReplacements(String[] replacements) {
		this.replacements = replacements;
	};
	
	

}
