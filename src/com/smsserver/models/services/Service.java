package com.smsserver.models.services;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Service {

	
	public String type;
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

	public Service(){
	}


	public Service(Keywords keywords, String query, String database, Messages messages, String serviceId) {
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
