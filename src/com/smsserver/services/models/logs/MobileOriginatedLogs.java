package com.smsserver.services.models.logs;

public class MobileOriginatedLogs {

	private String mobile;
	private String keyword;
	private String body;
	private String serviceId;
	private String messageId;
	private String replacements;
	private String smsstatus;
	
	public MobileOriginatedLogs(String mobile, String keyword, String body, String serviceId, String messageId,
			String replacements, String smsstatus) {
		super();
		this.mobile = mobile;
		this.keyword = keyword;
		this.body = body;
		this.serviceId = serviceId;
		this.messageId = messageId;
		this.replacements = replacements;
		this.smsstatus = smsstatus;
	}
	@Override
	public String toString() {
		return "MobileOriginatedLogs [mobile=" + mobile + ", keyword=" + keyword + ", body=" + body + ", serviceId="
				+ serviceId + ", messageId=" + messageId + ", replacements=" + replacements + ", smsstatus=" + smsstatus
				+ "]";
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
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
	public String getSmsstatus() {
		return smsstatus;
	}
	public void setSmsstatus(String smsstatus) {
		this.smsstatus = smsstatus;
	}
	
	
	
	
	
	
}
