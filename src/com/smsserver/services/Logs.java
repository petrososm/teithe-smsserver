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

import com.smsserver.controllers.models.gunetapi.DlrRequestModel;
import com.smsserver.controllers.models.gunetapi.SendSmsModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardModel;
import com.smsserver.controllers.models.gunetapi.SmsResponseModel;
import com.smsserver.dao.ScheduledBackupLogs;
import com.smsserver.services.models.logs.MobileOriginatedLogs;
import com.smsserver.services.models.logs.MobileTerminatedLogs;

@Singleton
@Startup
@DependsOn("properties")
public class Logs {

    private ScheduledExecutorService scheduler;
    private ScheduledBackupLogs sbl;
    
    @PostConstruct
    private void initLogBackup(){
		scheduler = Executors.newScheduledThreadPool(1); 
		sbl=new ScheduledBackupLogs();
		scheduler.scheduleAtFixedRate(sbl, 0, 
				Integer.parseInt(GetPropertyValues.getProperties().getProperty("backupInterval")), TimeUnit.MINUTES);
    }
    
    @PreDestroy
    private void backupOnExit(){
    	scheduler.shutdown();
    	sbl.run();
    }
    
    
	private ArrayList<DlrRequestModel> deliveryReports = new ArrayList<DlrRequestModel>();
	private ArrayList<MobileOriginatedLogs> _MOLogs = new ArrayList<MobileOriginatedLogs>();
	private ArrayList<MobileTerminatedLogs> _MTLogs = new ArrayList<MobileTerminatedLogs>();

	public void logDlr(DlrRequestModel dlrReq) {
		deliveryReports.add(dlrReq);

	}

	public void logMobileOriginated(SmsForwardModel smsRequest, SendSmsModel sms, SmsResponseModel response) {
		if(response.getError().equals(""))
			response.setError("Delivered");
		String replacements="";
		if(sms.getReplacements()!=null)
			replacements=String.join(", ", sms.getReplacements());

		_MOLogs.add(new MobileOriginatedLogs(smsRequest.getMsisdn(), smsRequest.getKeyword(), smsRequest.getBody(), sms.getServiceId(),
				sms.getMessageId(),replacements , response.getError()));
	}
	
	public void logMobileTerminated(String recipientGroup,String sender,SendSmsModel sms,int sentTo,int received){
		String replacements="";
		if(sms.getReplacements()!=null)
			replacements=String.join(", ", sms.getReplacements());
		_MTLogs.add(new MobileTerminatedLogs(recipientGroup,sender,sms.getServiceId(),sms.getMessageId(),replacements,sentTo,received));
	}

	public ArrayList<DlrRequestModel> getDeliveryReports() {
		return deliveryReports;
	}

	public ArrayList<MobileOriginatedLogs> get_MOLogs() {
		return _MOLogs;
	}

	public ArrayList<MobileTerminatedLogs> get_MTLogs() {
		return _MTLogs;
	}

}
