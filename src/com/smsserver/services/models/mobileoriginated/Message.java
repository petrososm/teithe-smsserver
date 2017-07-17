package com.smsserver.services.models.mobileoriginated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Message {

	private String messageId;
	private String keyword;
	private String query;
	private String description;
	private int numberOfReplacements;
	private String userInput;
	private int queryParams;
	private String database;
	
	public Message(){
	}



	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", keyword=" + keyword + ", query=" + query ;
	}

	


	public String getDatabase() {
		return database;
	}



	public void setDatabase(String database) {
		this.database = database;
	}



	public String getKeyword() {
		return keyword;
	}



	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}



	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumberOfReplacements() {
		return numberOfReplacements;
	}

	public void setNumberOfReplacements(int numberOfReplacements) {
		this.numberOfReplacements = numberOfReplacements;
	}

	public String getUserInput() {
		return userInput;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	public int getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(int queryParams) {
		this.queryParams = queryParams;
	}



	public String getFullKeyword() {
		if(keyword==null)
			return userInput;
		return keyword+"<"+userInput+">";
	}



	public void computeQueryParams() {
		queryParams=query.length() - query.replace("?", "").length();
		
	}








}
