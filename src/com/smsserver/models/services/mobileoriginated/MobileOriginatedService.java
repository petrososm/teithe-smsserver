package com.smsserver.models.services.mobileoriginated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

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


	public MobileOriginatedService(MobileOriginatedService mobileOriginatedService) {
		super();
		this.query = mobileOriginatedService.query;
		this.database = mobileOriginatedService.database;
		this.messages = mobileOriginatedService.messages;
		this.serviceId = mobileOriginatedService.serviceId;
		this.queryParams=mobileOriginatedService.queryParams;
		this.numberOfReplacements=mobileOriginatedService.numberOfReplacements;
	}


	@Override
	public String toString() {
		return "MobileOriginatedService [query=" + query + ", database=" + database + ", messages=" + messages
				+ ", serviceId=" + serviceId + ", preSharedKey=" + preSharedKey + ", numberOfReplacements="
				+ numberOfReplacements + ", queryParams=" + queryParams + "]";
	}










	
	
	
	
	
}
