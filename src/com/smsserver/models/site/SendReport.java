package com.smsserver.models.site;

public class SendReport {

	public int sent;
	public int delivered;
	public SendReport(int sent, int delivered) {
		super();
		this.sent = sent;
		this.delivered = delivered;
	}
	
	public SendReport(){}
	
	
}
