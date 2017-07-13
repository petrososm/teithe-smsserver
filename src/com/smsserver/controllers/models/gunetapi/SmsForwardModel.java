package com.smsserver.controllers.models.gunetapi;

import javax.xml.bind.annotation.XmlElement;

//import org.codehaus.jackson.annotate.JsonIgnoreProperties;
//@JsonIgnoreProperties(ignoreUnknown = true)//gia na min petaei error sta missmatch

public class SmsForwardModel {

	// Jsonproperty gia to jackson, gia na kanei consume to service
	// SerializedName gia to gson, gia opiadipote metatropi json-object
	@XmlElement(name = "MSISDN")
	private String msisdn;// to kinito tilefwno toy apostolea-xristi
	private String keyword;// to string me to opoio ksekinaei to minima
	private String body;// self explanatory
	private String preSharedKey;// to key p exei simfwnithei me to gunet
	private String smsForwardId;

	public SmsForwardModel(String msisdn, String keyword, String body, String preSharedKey, String smsForwardId) {
		super();
		this.msisdn = msisdn;
		this.keyword = keyword;
		this.body = body;
		this.preSharedKey = preSharedKey;
		this.smsForwardId = smsForwardId;
	}

	public SmsForwardModel() {
	}

	@Override
	public String toString() {
		return "SmsForwardModel [msisdn=" + msisdn + ", keyword=" + keyword + ", body=" + body + ", preSharedKey="
				+ preSharedKey + ", smsForwardId=" + smsForwardId + "]";
	}

	public String getMsisdn() {
		return msisdn;
	}
	@XmlElement(name = "MSISDN")
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPreSharedKey() {
		return preSharedKey;
	}
	@XmlElement(name = "pre-shared key")
	public void setPreSharedKey(String preSharedKey) {
		this.preSharedKey = preSharedKey;
	}

	public String getSmsForwardId() {
		return smsForwardId;
	}
	@XmlElement(name = "sms-forward-id")
	public void setSmsForwardId(String smsForwardId) {
		this.smsForwardId = smsForwardId;
	}
	
	

}
