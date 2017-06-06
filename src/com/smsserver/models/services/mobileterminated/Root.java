package com.smsserver.models.services.mobileterminated;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Root {
    
	@XmlElement(name="service")
	public ArrayList<MobileTerminatedService> mobileTerminatedService;


}
