package com.smsserver.controllers.models.gunetapi;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SmsResponseModel {

	public String serviceId;// to id tis ipiresias poy klithike
	public String errorCode;// to id toy error
	public String error;// to minima lathous unknown service, unknown message, unknown
					// recipient, unknown institution, invalid pre-shared key,
					// unauthorized sender address, user not opted in, user
					// deactivated service, user credits expired, service
					// deactivated, general error etc.

	public SmsResponseModel(String serviceId, String errorCode, String error) {
		super();
		this.serviceId = serviceId;
		this.errorCode = errorCode;
		this.error = error;
	}
	public SmsResponseModel(){}
	@Override
	public String toString() {
		return "SmsResponseModel [serviceId=" + serviceId + ", errorCode=" + errorCode + ", error=" + error + "]";
	}

		
	
	
	
	
	

}
