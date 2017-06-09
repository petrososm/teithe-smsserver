package com.smsserver.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.smsserver.configuration.ServicesOnLoad;
import com.smsserver.gunetservices.GunetServices;
import com.smsserver.models.gunetapi.SendSmsModel;
import com.smsserver.models.gunetapi.SmsForwardModel;
import com.smsserver.models.gunetapi.SmsForwardResponseModel;
import com.smsserver.models.gunetapi.SmsResponseModel;
import com.smsserver.models.services.mobileoriginated.ExtraKeyword;
import com.smsserver.models.services.mobileoriginated.MobileOriginatedService;
import com.smsserver.sql.Pithia;

import javassist.NotFoundException;

public class MobileOriginated {


	
	public static SmsForwardResponseModel reply(SmsForwardModel smsRequest) {
		if (!ServicesOnLoad.getServices().containsKey(smsRequest.keyword))
			return keyWordNotExisting();

		MobileOriginatedService mobileOriginatedService = ServicesOnLoad.getServices().get(smsRequest.keyword);
		
		if(!mobileOriginatedService.preSharedKey.equals(smsRequest.preSharedKey))
			return new SmsForwardResponseModel(false, "Wrong preSharedKey", "0");

		
		try {
			System.out.println(smsRequest);
			SendSmsModel sms=null;
			if(smsRequest.keyword.equalsIgnoreCase("ΑΙΜΟΔΟΣΙΑ"))
				sms=prepareReplyAimodosia(smsRequest);
			else
				sms=prepareReplyPithia(smsRequest, mobileOriginatedService);//an petaksei exception to pianei katw k leitourgei antistoixa
			SmsResponseModel response=GunetServices.sendSingleSms(sms);
			Logs.logMobileOriginated(smsRequest,sms,response);
			return new SmsForwardResponseModel(true);
		}catch(NotFoundException ex){
			userNotFound();
			return new SmsForwardResponseModel(true);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new SmsForwardResponseModel(false, e.getMessage(), "0");
		}

	}

	private static SendSmsModel prepareReplyAimodosia(SmsForwardModel smsRequest) {
		// TODO Auto-generated method stub
		return null;
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
		
		
		String authenticator=Discovery.getUsername(smsRequest.msisdn);
		
		DataSource ds = Pithia.getSqlConnections();
		if(ds==null)
			throw new Exception("Database not specified in service");
		
		try (Connection conn = ds.getConnection();
				PreparedStatement stmt=conn.prepareStatement(service.query);) {
			
			stmt.setString(1, authenticator);
			for(int i=1;i<service.queryParams;i++)//ksekinaei apo 1 giati mia parametros einai standard
				stmt.setString(i+1, userParameters[i-1+extra]);//-1 gia na paei sto 0 . extra an iparxei secondary

			
			String messageToSend="";
			String[] replacements=null;
			
			System.out.println(service);
			System.out.println(authenticator);
			
			try (ResultSet rs = stmt.executeQuery();) {
				if(service.messages.defaultMessage==null)
					throw new Exception("NO REPLY");//na min stilei minima
				if (!rs.next()) {
					messageToSend = mobileOriginatedService.messages.errorMessage;// ypothetontas oti dn xreiazetai replacement kai oti einai panta to 2o minima
				} else {
					messageToSend=service.messages.defaultMessage;
					replacements=new String[service.numberOfReplacements];
					for(int i=0;i<service.numberOfReplacements;i++)
						replacements[i]=rs.getString(i+1);
				}
			}

			
			SendSmsModel sms = new SendSmsModel(service.serviceId, messageToSend, replacements, smsRequest.msisdn,
					smsRequest.smsForwardId);
			
			service=null;
			System.out.println(sms);
			return sms;
		} 
	}
	




	private static SmsForwardResponseModel keyWordNotExisting() {

		// create new SMS Model
		// send SMS
		return new SmsForwardResponseModel(false, "keyword not existing", "15");

	}
	private static void userNotFound() {
		// TODO Auto-generated method stub
		
	}

}
