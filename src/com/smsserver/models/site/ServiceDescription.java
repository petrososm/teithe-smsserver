package com.smsserver.models.site;

public class ServiceDescription {
	
	public String keyword;
	public String description;
	public ServiceDescription(String keyword, String description) {
		super();
		this.keyword = keyword;
		this.description = description;
	}
	@Override
	public String toString() {
		return "ServiceDescription [keyword=" + keyword + ", description=" + description + "]";
	}
	
	

}
