package com.smsserver.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.smsserver.configuration.ServicesOnLoad;
import com.smsserver.gunetservices.GunetServices;
import com.smsserver.models.gunetapi.*;
import com.smsserver.models.services.*;

public class MobileOriginated {

	public static SmsForwardResponseModel reply(SmsForwardModel smsRequest) {
		if (!ServicesOnLoad.getServices().containsKey(smsRequest.keyword))
			return keyWordNotExisting();

		Service service = ServicesOnLoad.getServices().get(smsRequest.keyword);
		
		if(!service.preSharedKey.equals(smsRequest.preSharedKey))
			return new SmsForwardResponseModel(false, "Wrong preSharedKey", "0");

		
		try {
			SendSmsModel sms = prepareReply(smsRequest, service);//an petaksei exception to pianei katw k leitourgei antistoixa
			//SmsResponseModel response=GunetServices.sendSms(sms);
			//Logs.logMobileOriginated(smsRequest,sms,response);
			return new SmsForwardResponseModel(true);
		} catch (Exception e) {
			e.printStackTrace();
			return new SmsForwardResponseModel(false, "sqlError", "0");
		}

	}

	private static SendSmsModel prepareReply(SmsForwardModel smsRequest,Service service) throws Exception {
		
		DataSource ds=null;
		if(service.database.equalsIgnoreCase("pithia")){
			ds=com.smsserver.sql.Pithia.getSqlConnections();
		}
		else if(service.database.equalsIgnoreCase("localDb")){//paradeigma
			ds=com.smsserver.sql.LocalDb.getSqlConnections();
		}
		
		try (Connection conn = ds.getConnection();) {

			String messageId = service.messages.defaultMessage;
			int numberOfReplacements=service.numberOfReplacements;
			String serviceId = service.serviceId;
			String query=service.query;
			String[] replacements = null;
			int queryParams=service.queryParams;
			PreparedStatement stmt ;
			
			
			if(smsRequest.body.equalsIgnoreCase("")){
				stmt=conn.prepareStatement(query);//ena preparedstatement xwris parametrous
				stmt.setString(1, smsRequest.msisdn);
			}
			else{
				String[] userParameters = smsRequest.body.split("\\s+");//diaxwrizei to body
				int extra=0;//an xriastei na paei sto epomeno
					if(service.extraKeyword != null){
						for(ExtraKeyword ek:service.extraKeyword){//an yparxoyn k alla query
							if(userParameters[0].equalsIgnoreCase(ek.keyword)){
								messageId=ek.message;
								numberOfReplacements=ek.numberOfReplacements;//allazei tis parametrous tou erwtimatos
								query=ek.query;
								queryParams=ek.queryParams;	
								extra++;//gia na ksekinisei apo tin epomeni leksi
							}
						}
				}
				stmt=conn.prepareStatement(query);//kanei ena geniko prepare
												  //ean den bei edw, to kanei sto allo if
				stmt.setString(1, smsRequest.msisdn);
				for(int i=1;i<queryParams;i++)//ksekinaei apo 1 giati mia parametros einai standard
					stmt.setString(i+1, userParameters[i-1+extra]);//-1 gia na paei sto 0 . extra an iparxei secondary
			}


			try (ResultSet rs = stmt.executeQuery();) {
				if (!rs.next()) {
					messageId = service.messages.errorMessage;// ypothetontas oti dn xreiazetai replacement kai oti einai panta to 2o minima
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

}
