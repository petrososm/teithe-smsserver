package com.smsserver.services;

import javax.ejb.EJB;
import javax.ejb.Stateful;

import com.smsserver.controllers.models.gunetapi.SendSmsModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardResponseModel;
import com.smsserver.controllers.models.gunetapi.SmsResponseModel;
import com.smsserver.dao.Aimodosia;
import com.smsserver.dao.Discovery;
import com.smsserver.dao.Pithia;
import com.smsserver.services.gunetservices.GunetServices;
import com.smsserver.services.models.mobileoriginated.Message;
import com.smsserver.services.models.mobileoriginated.MobileOriginatedService;

import javassist.NotFoundException;

@Stateful
public class MobileOriginated {

	@EJB
	Discovery discovery;
	@EJB
	Services services;
	@EJB
	Logs logs;
	@EJB
	Aimodosia aimodosiaDao;
	@EJB
	Pithia pithiaDao;
	@EJB
	GunetServices gunet;

	public SmsForwardResponseModel reply(SmsForwardModel smsRequest) {
		System.out.println(services.getMobileOriginatedServices().keySet());
		if (!services.getMobileOriginatedServices().containsKey(smsRequest.getKeyword()))
			return new SmsForwardResponseModel(false, "Wrong keyword", "0");

		MobileOriginatedService mobileOriginatedService = services.getMobileOriginatedServices().get(smsRequest.getKeyword());

		// if(!mobileOriginatedService.preSharedKey.equals(smsRequest.preSharedKey))
		// return new SmsForwardResponseModel(false, "Wrong preSharedKey", "0");

		try {
			System.out.println("checkpoint");
			SendSmsModel sms = null;
			
			sms = prepareReply(smsRequest, mobileOriginatedService);

			SmsResponseModel response = gunet.sendSingleSms(sms);
			logs.logMobileOriginated(smsRequest, sms, response);
			return new SmsForwardResponseModel(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new SmsForwardResponseModel(false, e.getMessage(), "0");
		}

	}



	private SendSmsModel prepareReply(SmsForwardModel smsRequest, MobileOriginatedService mobileOriginatedService)
			throws Exception {
		String[] userParameters = smsRequest.getBody().split("\\s+");;
		int extra = 0;
		Message message = null;
		if (!smsRequest.getBody().equals("")) {
			for (Message mes : mobileOriginatedService.getMessages()) {
				if (mes.getKeyword().equalsIgnoreCase(userParameters[0])) {
					message = mes;
					extra++;
				}
			}
		}
		else {
			for (Message mes : mobileOriginatedService.getMessages())
				if (mes.getKeyword().equals(""))
					message = mes;
		}

		if (message == null)
			throw new Exception("No message found with no keyword");// todo real
																	// exception

			

		String[] replacements = null;
		SendSmsModel sms;

		try {
			if (mobileOriginatedService.getDatabase().equalsIgnoreCase("pithia")){
				String authenticator = discovery.getUsername(smsRequest.getMsisdn());
				replacements = pithiaDao.queryPithia(message, authenticator, userParameters,extra);
			}
			else if(mobileOriginatedService.getDatabase().equalsIgnoreCase("nireas")){
				replacements=aimodosiaDao.queryAimodosia(message, smsRequest.getMsisdn(), userParameters,extra);
			}
		} catch (Exception e) {
			message = mobileOriginatedService.getErrorMessage();
			replacements=null;
			e.printStackTrace();
		}

		sms = new SendSmsModel(mobileOriginatedService.getServiceId(), message.getMessageId(), replacements,
				smsRequest.getMsisdn(), smsRequest.getSmsForwardId());
		return sms;

	}

}
