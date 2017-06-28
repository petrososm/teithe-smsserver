package com.smsserver.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.smsserver.controllers.models.gunetapi.SendSmsModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardResponseModel;
import com.smsserver.controllers.models.gunetapi.SmsResponseModel;
import com.smsserver.dao.Aimodosia;
import com.smsserver.dao.Discovery;
import com.smsserver.dao.Pithia;
import com.smsserver.dao.sqlconnections.PithiaConnections;
import com.smsserver.services.gunetservices.GunetServices;
import com.smsserver.services.models.mobileoriginated.ExtraKeyword;
import com.smsserver.services.models.mobileoriginated.MobileOriginatedService;

import javassist.NotFoundException;

public class MobileOriginated {


	
	public static SmsForwardResponseModel reply(SmsForwardModel smsRequest) {
		if (!Services.getServices().containsKey(smsRequest.keyword))
			return new SmsForwardResponseModel(false,"Wrong keyword","0");

		MobileOriginatedService mobileOriginatedService = Services.getServices().get(smsRequest.keyword);
		
		//if(!mobileOriginatedService.preSharedKey.equals(smsRequest.preSharedKey))
			//return new SmsForwardResponseModel(false, "Wrong preSharedKey", "0");

		
		try {
			System.out.println("checkpoint");
			SendSmsModel sms=null;
			if(smsRequest.keyword.equalsIgnoreCase("ΑΙΜΟΔΟΣΙΑ")
			||smsRequest.keyword.equalsIgnoreCase("AIMODOSIA"))
				sms=prepareReplyAimodosia(smsRequest);
			else
				sms=prepareReplyPithia(smsRequest, mobileOriginatedService);//an petaksei exception to pianei katw k leitourgei antistoixa
			
			SmsResponseModel response=GunetServices.sendSingleSms(sms);
			Logs.logMobileOriginated(smsRequest,sms,response);
			return new SmsForwardResponseModel(true);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new SmsForwardResponseModel(false, e.getMessage(), "0");
		}

	}

	private static SendSmsModel prepareReplyAimodosia(SmsForwardModel smsRequest) {
		String messageId=null;
		String[] replacements=new String[1];
		try{
		if(!smsRequest.body.equalsIgnoreCase("")){
			switch(smsRequest.body){
			case "STOP":
				Aimodosia.addBlacklist(smsRequest.msisdn);
				messageId="aimodosiaMsg2";
				replacements[0]="TEITHE ΑΙΜΟΔΟΣΙΑ";
				break;
			case "ΤΕΛΕΥΤΑΙΑ":
				replacements[0]=Aimodosia.getLastAimodosia(smsRequest.msisdn);
				messageId="aimodosiaMsg3";
				break;
			case "ΦΙΑΛΕΣ":
				replacements[0]=Aimodosia.getFiales(smsRequest.msisdn);
				messageId="aimodosiaMsg6";
				break;
			case "ΦΙΑΛΕΣ ΚΑΤΑΝΑΛ":
				replacements[0]=Aimodosia.getFialesKatanal(smsRequest.msisdn);
				messageId="aimodosiaMsg7";
				break;
			default:
				throw new Exception("NO REPLY");
			}		
		}
		else{
			Aimodosia.addSubscriber(smsRequest.msisdn);
			messageId="aimodosiaMsg1";
			replacements[0]="TEITHE STOP";
		}

		}
		catch(Exception e){
			e.printStackTrace();
			messageId="aimodosiaMsg4";
		}
		
		SendSmsModel sms = new SendSmsModel("aimodosia", messageId,replacements, smsRequest.msisdn,
					smsRequest.smsForwardId);
		return sms;
		
	
		
	}

	private static SendSmsModel prepareReplyPithia(SmsForwardModel smsRequest,MobileOriginatedService mobileOriginatedService) throws Exception {

		MobileOriginatedService service=new MobileOriginatedService(mobileOriginatedService);

		String[] userParameters=null;
		int extra=0;
		
		if(!smsRequest.body.equalsIgnoreCase("")){
			userParameters = smsRequest.body.split("\\s+");//diaxwrizei to body
				if(mobileOriginatedService.extraKeyword != null){
					for(ExtraKeyword ek:mobileOriginatedService.extraKeyword){//an yparxoyn k alla query
						if(userParameters[0].equalsIgnoreCase(ek.keyword)){
							service.messages.defaultMessage=ek.message;
							service.numberOfReplacements=ek.numberOfReplacements;//allazei tis parametrous tou erwtimatos
							service.query=ek.query;
							service.queryParams=ek.queryParams;
							extra++;//gia na ksekinisei apo tin epomeni leksi
						}
					}
			}
		}
		
		
		String messageToSend="";
		String[] replacements=null;
		try{
			String authenticator=Discovery.getUsername(smsRequest.msisdn);
			replacements=Pithia.queryPithia(service,authenticator,userParameters,extra);
		}
		catch(NotFoundException ex){
			messageToSend=service.messages.errorMessage;
		}
		SendSmsModel sms = new SendSmsModel(service.serviceId, messageToSend, replacements, smsRequest.msisdn,
				smsRequest.smsForwardId);

		return sms;
	}
	



}
