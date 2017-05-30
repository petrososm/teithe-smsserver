package com.smsserver.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import com.smsserver.configuration.ServicesOnLoad;
import com.smsserver.models.gunetapi.*;
import com.smsserver.models.services.mobileoriginated.ExtraKeyword;
import com.smsserver.models.services.mobileoriginated.MobileOriginatedService;
import com.smsserver.sql.LocalDb;
import com.smsserver.sql.Nireas;
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
			SendSmsModel sms = prepareReply(smsRequest, mobileOriginatedService);//an petaksei exception to pianei katw k leitourgei antistoixa
			//SmsResponseModel response=GunetServices.sendSms(sms);
			//Logs.logMobileOriginated(smsRequest,sms,response);
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

	private static SendSmsModel prepareReply(SmsForwardModel smsRequest,MobileOriginatedService mobileOriginatedService) throws Exception {

		String messageId = mobileOriginatedService.messages.defaultMessage;
		int numberOfReplacements=mobileOriginatedService.numberOfReplacements;
		String serviceId = mobileOriginatedService.serviceId;
		String query=mobileOriginatedService.query;
		String[] replacements = null;
		int queryParams=mobileOriginatedService.queryParams;
		String database=mobileOriginatedService.database;
		PreparedStatement stmt ;
		String[] userParameters=null;
		String authenticator=smsRequest.msisdn;
		int extra=0;
		
		if(!smsRequest.body.equalsIgnoreCase("")){
			userParameters = smsRequest.body.split("\\s+");//diaxwrizei to body
				if(mobileOriginatedService.extraKeyword != null){
					for(ExtraKeyword ek:mobileOriginatedService.extraKeyword){//an yparxoyn k alla query
						if(userParameters[0].equalsIgnoreCase(ek.keyword)){
							messageId=ek.message;
							numberOfReplacements=ek.numberOfReplacements;//allazei tis parametrous tou erwtimatos
							query=ek.query;
							queryParams=ek.queryParams;
							if(ek.database!=null)
								database=ek.database;
							extra++;//gia na ksekinisei apo tin epomeni leksi
						}
					}
			}

		}
		DataSource ds=null;
		if(mobileOriginatedService.database.equalsIgnoreCase("pithia")){
			ds=Pithia.getSqlConnections();
			authenticator=MobileDiscovery.getUsername(smsRequest.msisdn);
		}
		else if(mobileOriginatedService.database.equalsIgnoreCase("localDb")){//paradeigma
			ds=LocalDb.getSqlConnections();
		}	
		else if(mobileOriginatedService.database.equalsIgnoreCase("nireas")){//paradeigma
			ds=Nireas.getSqlConnections();
		}
		try (Connection conn = ds.getConnection();) {
			stmt=conn.prepareStatement(query);//ena preparedstatement xwris parametrous
			stmt.setString(1, smsRequest.msisdn);
			for(int i=1;i<queryParams;i++)//ksekinaei apo 1 giati mia parametros einai standard
				stmt.setString(i+1, userParameters[i-1+extra]);//-1 gia na paei sto 0 . extra an iparxei secondary

			


			try (ResultSet rs = stmt.executeQuery();) {
				if(messageId==null)
					throw new Exception("NO REPLY");//na min stilei minima
				if (!rs.next()) {
					messageId = mobileOriginatedService.messages.errorMessage;// ypothetontas oti dn xreiazetai replacement kai oti einai panta to 2o minima
				} else {					
					replacements=new String[numberOfReplacements];
					for(int i=0;i<numberOfReplacements;i++)
						replacements[i]=rs.getString(i+1);
				}
			}

			stmt.close();
			SendSmsModel sms = new SendSmsModel(serviceId, messageId, replacements, smsRequest.msisdn,
					smsRequest.smsForwardId);
			
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
