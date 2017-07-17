package com.smsserver.services.models.logs;

public class MobileTerminatedLogs {

	private String list;
	private String user;
	private String serviceId;
	private String messageId;
	private String replacements;
	private int sentTo;
	private int received;

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

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getReplacements() {
		return replacements;
	}

	public void setReplacements(String replacements) {
		this.replacements = replacements;
	}

	public int getSentTo() {
		return sentTo;
	}

	public void setSentTo(int sentTo) {
		this.sentTo = sentTo;
	}

	public int getReceived() {
		return received;
	}

	public void setReceived(int received) {
		this.received = received;
	}

	
}
