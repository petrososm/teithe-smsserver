package com.smsserver.doa;

import java.util.ArrayList;

import com.smsserver.models.NocSite.SendSmsRequest;
import com.smsserver.models.gunetapi.DlrRequestModel;
import com.smsserver.models.gunetapi.SendSmsModel;
import com.smsserver.models.gunetapi.SmsForwardModel;
import com.smsserver.models.gunetapi.SmsResponseModel;
import com.smsserver.models.logs.MobileOriginatedLogs;
import com.smsserver.models.logs.MobileTerminatedLogs;

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
	
	public static void logMobileTerminated(SendSmsRequest smsRequest,SendSmsModel sms,int sentTo,int received){
		_MTLogs.add(new MobileTerminatedLogs(smsRequest.course,smsRequest.professor,sms.serviceId,sms.messageId,String.join(", ", sms.replacements),sentTo,received));
	}

}
