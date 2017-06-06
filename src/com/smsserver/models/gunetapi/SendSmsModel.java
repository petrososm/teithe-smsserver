package com.smsserver.models.gunetapi;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smsserver.configuration.GetPropertyValues;



@XmlRootElement
public class SendSmsModel {

	
	public String serviceId;//id tis ipiresias pou periexei to to prokathorismeno minima pros apostoli
	public String messageId;//to id toy minimatos
	public String[] replacements; //se periptwsi p thelei array
	public String recipient;//username i msisdn toy xristi 
	public final String institution =  "TEITHE";//ypoxrewtiko gia teithe
    @JsonProperty("pre-shared key")
    public final String preSharedKey= GetPropertyValues.getProperties().getProperty("preSharedKey");
    @JsonProperty("dlr-url")
    public final String dlrUrl= GetPropertyValues.getProperties().getProperty("dlrUrl");//ena link-service opoy tha stelnetai mia anafora paradosis -proeraitiko
    @JsonProperty("sms-forward-id")
    public String smsForwardId;//efoson ginei se dinexeia enos forward request-proeraitiko
	
    
    public SendSmsModel(String serverId, String messageId, String[] replacements, String recipient, String smsForwardId) {
		super();
		this.serviceId = serverId;
		this.messageId = messageId;
		this.replacements = replacements;
		this.recipient = recipient;
		this.smsForwardId = smsForwardId;
	}
    public SendSmsModel(String serverId, String messageId, String[] replacements, String recipient) {
		super();
		this.serviceId = serverId;
		this.messageId = messageId;
		this.replacements = replacements;
		this.recipient = recipient;

	}
    public SendSmsModel(String serverId, String messageId, String[] replacements) {
		super();
		this.serviceId = serverId;
		this.messageId = messageId;
		this.replacements = replacements;

	}
    
    public SendSmsModel(SendSmsModel sms) {
		super();
		this.serviceId = sms.serviceId;
		this.messageId = sms.messageId;
		this.replacements = sms.replacements;

	}
    
    
    public SendSmsModel(){}

	
	@Override
	public String toString() {
		return "SendSmsModel [serviceId=" + serviceId + ", messageId=" + messageId + ", replacements="
				+ Arrays.toString(replacements) + ", recipient=" + recipient + ", smsForwardId=" + smsForwardId + "]";
	}

	
	
	
}
