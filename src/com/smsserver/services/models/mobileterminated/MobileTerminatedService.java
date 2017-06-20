package com.smsserver.services.models.mobileterminated;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;

public class MobileTerminatedService {
	
	public String serviceId;
	public String preSharedKey;
	public String type;
	@XmlElement(name="message")
	public Message[] messages;
	@Override
	public String toString() {
		return "MobileTerminatedService [serviceId=" + serviceId + ", preSharedkey=" + preSharedKey + ", messages="
				+ Arrays.toString(messages) + "]";
	}
	

}
