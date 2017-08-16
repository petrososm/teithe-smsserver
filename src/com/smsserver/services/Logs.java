package com.smsserver.services;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;

import com.smsserver.controllers.models.gunetapi.DlrRequestModel;
import com.smsserver.controllers.models.gunetapi.SendSmsModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardModel;
import com.smsserver.controllers.models.gunetapi.SmsResponseModel;
import com.smsserver.dao.ScheduledBackupLogs;
import com.smsserver.services.models.logs.MobileOriginatedLogs;
import com.smsserver.services.models.logs.MobileTerminatedLogs;

@Stateless
public class Logs {

	@EJB
	ScheduledBackupLogs sbl;

	public void logDlr(DlrRequestModel dlrReq) {
		sbl.addDeliveryReport(dlrReq);

	}

	public void logMobileOriginated(SmsForwardModel smsRequest, SendSmsModel sms, SmsResponseModel response) {
		if (response.getError().equals(""))
			response.setError("Delivered");
		String replacements = "";
		if (sms.getReplacements() != null)
			replacements = String.join(", ", sms.getReplacements());

		sbl.addMoLog(new MobileOriginatedLogs(smsRequest.getMsisdn(), smsRequest.getKeyword(), smsRequest.getBody(),
				sms.getServiceId(), sms.getMessageId(), replacements, response.getError()));
	}

	public void logMobileTerminated(String recipientGroup, String sender, SendSmsModel sms, int sentTo, int received) {
		String replacements = "";
		if (sms.getReplacements() != null)
			replacements = String.join(", ", sms.getReplacements());
		sbl.addMtLog(new MobileTerminatedLogs(recipientGroup, sender, sms.getServiceId(), sms.getMessageId(),
				replacements, sentTo, received));
	}

}
