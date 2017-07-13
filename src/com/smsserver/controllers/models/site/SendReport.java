package com.smsserver.controllers.models.site;

public class SendReport {

	private int sent;
	private int delivered;
	private int total;
	public SendReport(int sent, int delivered,int total) {
		super();
		this.sent = sent;
		this.delivered = delivered;
		this.total=total;
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	
}
