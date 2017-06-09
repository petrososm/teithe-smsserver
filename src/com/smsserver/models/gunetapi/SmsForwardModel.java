package com.smsserver.models.gunetapi;

import com.fasterxml.jackson.annotation.JsonProperty;

//import org.codehaus.jackson.annotate.JsonIgnoreProperties;
//@JsonIgnoreProperties(ignoreUnknown = true)//gia na min petaei error sta missmatch

public class SmsForwardModel {
	
	//Jsonproperty gia to jackson, gia na kanei consume to service
	//SerializedName gia to gson, gia opiadipote metatropi json-object 
	@JsonProperty("MSISDN")
	public String msisdn;//to kinito tilefwno toy apostolea-xristi
	public String keyword;//to string me to opoio ksekinaei to minima
	public String body;//self explanatory
    @JsonProperty("pre-shared key")
	public String preSharedKey;//to key p exei simfwnithei me to gunet
    @JsonProperty("sms-forward-id")
    public  String smsForwardId;
    
	public SmsForwardModel(String msisdn, String keyword, String body, String preSharedKey, String smsForwardId) {
		super();
		this.msisdn = msisdn;
		this.keyword = keyword;
		this.body = body;
		this.preSharedKey = preSharedKey;
		this.smsForwardId = smsForwardId;
	}
	public SmsForwardModel() {
		super();
	}
	@Override
	public String toString() {
		return "SmsForwardModel [msisdn=" + msisdn + ", keyword=" + keyword + ", body=" + body + ", preSharedKey="
				+ preSharedKey + ", smsForwardId=" + smsForwardId + "]";
	}
  
}
