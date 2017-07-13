package com.smsserver.controllers.models.gunetapi;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SmsResponseModel {

	private String serviceId;// to id tis ipiresias poy klithike
	private String errorCode;// to id toy error
	private String error;// to minima lathous unknown service, unknown message, unknown
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
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

		
	
	
	
	
	

}
