package com.smsserver.services;

import java.util.ArrayList;

import com.smsserver.controllers.models.gunetapi.DlrRequestModel;
import com.smsserver.controllers.models.gunetapi.SendSmsModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardModel;
import com.smsserver.controllers.models.gunetapi.SmsResponseModel;
import com.smsserver.services.models.logs.MobileOriginatedLogs;
import com.smsserver.services.models.logs.MobileTerminatedLogs;

public class Logs {

	static ArrayList<DlrRequestModel> deliveryReports = new ArrayList<DlrRequestModel>();
	static ArrayList<MobileOriginatedLogs> _MOLogs = new ArrayList<MobileOriginatedLogs>();
	static ArrayList<MobileTerminatedLogs> _MTLogs = new ArrayList<MobileTerminatedLogs>();

	public static void logDlr(DlrRequestModel dlrReq) {
		deliveryReports.add(dlrReq);

	}

	public static void logMobileOriginated(SmsForwardModel smsRequest, SendSmsModel sms, SmsResponseModel response) {
		if(response.error.equals(""))
			response.error="Delivered";

		_MOLogs.add(new MobileOriginatedLogs(smsRequest.msisdn, smsRequest.keyword, smsRequest.body, sms.serviceId,
				sms.messageId, String.join(", ", sms.replacements), response.error));
	}
	
	public static void logMobileTerminated(String recipientGroup,String sender,SendSmsModel sms,int sentTo,int received){
		_MTLogs.add(new MobileTerminatedLogs(recipientGroup,sender,sms.serviceId,sms.messageId,String.join(", ", sms.replacements),sentTo,received));
	}

	public static ArrayList<DlrRequestModel> getDeliveryReports() {
		return deliveryReports;
	}

	public static ArrayList<MobileOriginatedLogs> get_MOLogs() {
		return _MOLogs;
	}

	public static ArrayList<MobileTerminatedLogs> get_MTLogs() {
		return _MTLogs;
	}

}
