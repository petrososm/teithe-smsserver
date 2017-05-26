package com.smsserver.models.logs;

public class MobileOriginatedLogs {

	public String mobile;
	public String keyword;
	public String body;
	public String serviceId;
	public String messageId;
	public String replacements;
	public String smsstatus;
	
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
	
	
	
	
	
}
