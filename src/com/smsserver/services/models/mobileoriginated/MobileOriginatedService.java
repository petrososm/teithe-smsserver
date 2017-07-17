package com.smsserver.services.models.mobileoriginated;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class MobileOriginatedService {

	private String serviceId;
	private Keywords keywords;
	private String preSharedKey;
	private String database;
	@XmlElement(name = "message")
	private Message[] messages;
	private Message errorMessage;


	public MobileOriginatedService() {
	}


	@Override
	public String toString() {
		return "MobileOriginatedService [serviceId=" + serviceId + ", keywords=" + keywords;
	}


	public String getServiceId() {
		return serviceId;
	}


	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}


	public Keywords getKeywords() {
		return keywords;
	}


	public void setKeywords(Keywords keywords) {
		this.keywords = keywords;
	}


	public String getPreSharedKey() {
		return preSharedKey;
	}


	public void setPreSharedKey(String preSharedKey) {
		this.preSharedKey = preSharedKey;
	}


	public String getDatabase() {
		return database;
	}


	public void setDatabase(String database) {
		this.database = database;
	}


	public Message[] getMessages() {
		return messages;
	}

	public void setMessages(Message[] messages) {
		this.messages = messages;
	}


	public Message getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(Message errorMessage) {
		this.errorMessage = errorMessage;
	}




}
