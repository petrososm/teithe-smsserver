package com.smsserver.models.gunetapi;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonProperty;



@XmlRootElement
public class SendSmsModel {

	
	public String serviceId;//id tis ipiresias pou periexei to to prokathorismeno minima pros apostoli
	public String messageId;//to id toy minimatos
	public String[] replacements; //se periptwsi p thelei array
	public String recipient;//username i msisdn toy xristi 
	public String institution =  "TEITHE";//ypoxrewtiko gia teithe
    @JsonProperty("pre-shared key")
    public String preSharedKey= "f0F0767834843f0";//tha simfwnithei ystera
    @JsonProperty("dlr-url")
    public String dlrUrl= "http://195.251.120.230:8080/sms/service/dlr"; //ena link-service opoy tha stelnetai mia anafora paradosis -proeraitiko
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
