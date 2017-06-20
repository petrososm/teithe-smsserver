package com.smsserver.services.models.mobileoriginated;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Keywords {
	@XmlElement(name="keyword")
	public ArrayList<String> keywordList;
	
	public Keywords(){
	}

	@Override
	public String toString() {
		return "Keywords [keyword=" + keywordList + "]";
	}






}
