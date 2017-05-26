package com.smsserver.models.gunetapi;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DlrRequestModel {
	


	String serviceId;//to Id tis ipiresias poy klithike
	String recipient;//o xristis p parelave to minima
	String status;//DELIVRD', 'ERROR', 'EXPIRED', 'PENDING', 'SENT', 'SUBMITTED', 'UNDELIV'
	String error;//0-999
	
	public DlrRequestModel(String serviceId, String recipient, String status, String error) {
		super();
		this.serviceId = serviceId;
		this.recipient = recipient;
		this.status = status;
		this.error = error;
	}
	public DlrRequestModel() {
		super();
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	

}
