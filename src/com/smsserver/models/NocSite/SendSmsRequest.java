package com.smsserver.models.NocSite;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SendSmsRequest {
	
	
	public String course;
	public String professor;
	public String serviceId;
	public String[] replacements;
	public String auth;
	
	
	public SendSmsRequest(){};

}
