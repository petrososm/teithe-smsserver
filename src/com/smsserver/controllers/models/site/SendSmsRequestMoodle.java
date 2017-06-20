package com.smsserver.controllers.models.site;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SendSmsRequestMoodle {
	
	
	public String course;
	public String professor;
	public String messageId;
	public String[] replacements;

	
	
	public SendSmsRequestMoodle(){};

}
