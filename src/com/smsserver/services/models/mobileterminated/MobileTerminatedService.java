package com.smsserver.services.models.mobileterminated;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;

public class MobileTerminatedService {
	
	private String serviceId;
	private String preSharedKey;
	private String type;	
	private Message[] messages;
	@Override
	public String toString() {
		return "MobileTerminatedService [serviceId=" + serviceId + ", preSharedkey=" + preSharedKey + ", messages="
				+ Arrays.toString(messages) + "]";
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getPreSharedKey() {
		return preSharedKey;
	}
	public void setPreSharedKey(String preSharedKey) {
		this.preSharedKey = preSharedKey;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@XmlElement(name="message")
	public Message[] getMessages() {
		return messages;
	}
	public void setMessages(Message[] messages) {
		this.messages = messages;
	}
	
	

}
