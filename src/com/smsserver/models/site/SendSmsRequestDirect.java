package com.smsserver.models.site;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SendSmsRequestDirect {
	
	
	public String[] recipients;
	public String sender;
	public String messageId;
	public String[] replacements;

	
	
	public SendSmsRequestDirect(){}



	@Override
	public String toString() {
		return "SendSmsRequestDirect [recipients=" + Arrays.toString(recipients) + ", sender=" + sender + ", messageId="
				+ messageId + ", replacements=" + Arrays.toString(replacements) + "]";
	};

}
