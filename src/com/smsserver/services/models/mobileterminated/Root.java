package com.smsserver.services.models.mobileterminated;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Root {
    
	@XmlElement(name="service")
	private ArrayList<MobileTerminatedService> mobileTerminatedService;

	public ArrayList<MobileTerminatedService> getMobileTerminatedService() {
		return mobileTerminatedService;
	}

	public void setMobileTerminatedService(ArrayList<MobileTerminatedService> mobileTerminatedService) {
		this.mobileTerminatedService = mobileTerminatedService;
	}


}
