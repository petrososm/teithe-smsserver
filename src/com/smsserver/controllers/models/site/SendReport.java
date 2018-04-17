package com.smsserver.controllers.models.site;

public class SendReport {

	private int sent;
	private int delivered;
	public SendReport(int sent, int delivered) {
		super();
		this.sent = sent;
		this.delivered = delivered;
	}
	
	public SendReport(){}

	public int getSent() {
		return sent;
	}

	public void setSent(int sent) {
		this.sent = sent;
	}

	public int getDelivered() {
		return delivered;
	}

	public void setDelivered(int delivered) {
		this.delivered = delivered;
	}


	
	
}
