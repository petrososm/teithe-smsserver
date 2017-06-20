package com.smsserver.controllers.models.gunetapi;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class SmsForwardResponseModel {

	// apostelnetai pisw sto gunet otan mas kanei smsforward
	int result;// an diaxristike swsta 0. alliws 1
	String errorCode;// to id toy error
	String error;// to minima lathous unknown service, unknown message, unknown
					// recipient, unknown institution, invalid pre-shared key,
					// unauthorized sender address, user not opted in, user
					// deactivated service, user credits expired, service
					// deactivated, general error etc.

	public SmsForwardResponseModel(int result, String errorCode, String error) {
		super();
		this.result = result;
		this.errorCode = errorCode;
		this.error = error;
	}

	public SmsForwardResponseModel(boolean succesful) {
		super();
		if (succesful) {
			this.result = 0;
			this.error = "";
			this.errorCode = "";
		} else {
			this.result = 1;
			this.error = "";
			this.errorCode = "";
		}

	}

	public SmsForwardResponseModel(boolean succesful, String errorCode, String error) {
		if (!succesful) {
			this.result = 1;
			this.errorCode = errorCode;
			this.error = error;
		}
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
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

	public SmsForwardResponseModel() {
		super();
	}

}