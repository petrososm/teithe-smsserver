package com.smsserver.controllers.models.site;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SendSmsRequestMoodle {
	
	
	private String course;
	private String professor;
	private String messageId;
	private String[] replacements;

	
	
	public SendSmsRequestMoodle(){}



	public String getCourse() {
		return course;
	}



	public void setCourse(String course) {
		this.course = course;
	}



	public String getProfessor() {
		return professor;
	}



	public void setProfessor(String professor) {
		this.professor = professor;
	}



	public String getMessageId() {
		return messageId;
	}



	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}



	public String[] getReplacements() {
		return replacements;
	}



	public void setReplacements(String[] replacements) {
		this.replacements = replacements;
	};
	
	

}
