package com.smsserver.models.site;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SendSmsRequest {
	
	
	public String course;
	public String professor;
	public String messageId;
	public String[] replacements;

	
	
	public SendSmsRequest(){};

}
