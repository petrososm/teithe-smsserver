package com.smsserver.services.models.logs;

public class MobileTerminatedLogs {
	
	public String list;
	public String user;
	public String serviceId;
	public String messageId;
	public String replacements;
	public int sentTo;
	public int received;
	public MobileTerminatedLogs(String list, String user, String serviceId, String messageId, String replacements,
			int sentTo, int received) {
		super();
		this.list = list;
		this.user = user;
		this.serviceId = serviceId;
		this.messageId = messageId;
		this.replacements = replacements;
		this.sentTo = sentTo;
		this.received = received;
	}
	@Override
	public String toString() {
		return "MobileTerminatedLogs [list=" + list + ", user=" + user + ", serviceId=" + serviceId + ", messageId="
				+ messageId + ", replacements=" + replacements + ", sentTo=" + sentTo + ", received=" + received + "]";
	}

	
	
	
}
