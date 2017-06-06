package com.smsserver.models.services.mobileoriginated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Messages {

	public String defaultMessage;
	public String errorMessage;
	
	public Messages(){
	}

	@Override
	public String toString() {
		return "Messages [defaultMessage=" + defaultMessage + ", errorMessage=" + errorMessage + "]";
	}






}
