package com.smsserver.models.services.mobileoriginated;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Root {
    
	@XmlElement(name="service")
	public ArrayList<MobileOriginatedService> mobileOriginatedService;
	
	


}
