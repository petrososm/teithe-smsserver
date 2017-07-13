package com.smsserver.controllers.models.site;

public class ServiceDescription {
	
	private String keyword;
	private String description;
	
	public ServiceDescription(String keyword, String description) {
		super();
		this.keyword = keyword;
		this.description = description;
	}
	public ServiceDescription(){}
	
	@Override
	public String toString() {
		return "ServiceDescription [keyword=" + keyword + ", description=" + description + "]";
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
