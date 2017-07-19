package com.smsserver.services.models.mobileoriginated;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Root {
    
	@XmlElement(name="service")
	private ArrayList<MobileOriginatedService> mobileOriginatedService;

	public ArrayList<MobileOriginatedService> getMobileOriginatedService() {
		return mobileOriginatedService;
	}

	public void setMobileOriginatedService(ArrayList<MobileOriginatedService> mobileOriginatedService) {
		this.mobileOriginatedService = mobileOriginatedService;
	}
	
	


}
