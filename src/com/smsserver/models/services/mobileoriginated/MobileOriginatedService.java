package com.smsserver.models.services.mobileoriginated;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class MobileOriginatedService {

	
	public Keywords keywords;
	public String query;
	public String database;
	public Messages messages;
	public String serviceId;
	public String preSharedKey;
	public int numberOfReplacements;
	public ExtraKeyword[] extraKeyword;
	public String description;
	public String userInput;
	public int queryParams;

	public MobileOriginatedService(){
	}


	public MobileOriginatedService(Keywords keywords, String query, String database, Messages messages, String serviceId) {
		super();
		this.keywords = keywords;
		this.query = query;
		this.database = database;
		this.messages = messages;
		this.serviceId = serviceId;
		queryParams = query.length() - query.replace("?", "").length();

	}


	@Override
	public String toString() {
		return "Question [keywords=" + keywords + ", query=" + query + ", database=" + database + ", messages="
				+ messages + ", serviceId=" + serviceId + ", preSharedKey=" + preSharedKey + ", queryParams="
				+ queryParams + "]";
	}






	
	
	
	
	
}
