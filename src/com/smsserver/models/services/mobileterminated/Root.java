package com.smsserver.models.services.mobileterminated;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Root {
    
	@XmlElement(name="service")
	public ArrayList<MobileTerminatedService> mobileTerminatedService;


}
