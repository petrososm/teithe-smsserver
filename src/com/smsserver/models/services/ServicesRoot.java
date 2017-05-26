package com.smsserver.models.services;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ServicesRoot {
    
	@XmlElement(name="service")
	public ArrayList<Service> service;
	
	
	public ServicesRoot(){	}

}
