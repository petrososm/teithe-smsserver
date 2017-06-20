package com.smsserver.controllers.models.site;

public class SendReport {

	public int sent;
	public int delivered;
	public int total;
	public SendReport(int sent, int delivered,int total) {
		super();
		this.sent = sent;
		this.delivered = delivered;
		this.total=total;
	}
	
	public SendReport(){}
	
	
}
