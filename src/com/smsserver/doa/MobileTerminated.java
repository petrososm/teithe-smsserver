package com.smsserver.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.smsserver.configuration.ServicesOnLoad;
import com.smsserver.gunetservices.GunetServices;
import com.smsserver.models.gunetapi.SendSmsModel;
import com.smsserver.models.gunetapi.SmsResponseModel;
import com.smsserver.models.site.SendReport;
import com.smsserver.models.site.SendSmsRequest;
import com.smsserver.sql.LocalDb;
import com.smsserver.sql.Nireas;

public class MobileTerminated {
	
	public static SendReport sendAimodosia(String date) throws Exception{
		
		ArrayList<String> mobileNumbers=new ArrayList<String>();
		ArrayList<String> blacklistedNumbers=new ArrayList<String>();
		
		String query="SELECT mobile FROM users u"
				+ " where mobile IS NOT NULL "
				+ "AND u.id NOT IN(  select r.donorId "
				+ "from donationdonor r,dates d  "
				+ "where  STR_TO_DATE(d.date, '%d-%m-%Y') > DATE_SUB(CURDATE(), INTERVAL 6 MONTH) "
				+ "and r.donationId=d.id)";
		try (Connection conn = Nireas.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){

			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				mobileNumbers.add(rs.getString(1));

		} 
		
		query="select mobile from aimodosia_blacklist";
		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){

			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				blacklistedNumbers.add(rs.getString(1));
		} 
		
		
		mobileNumbers.removeAll(blacklistedNumbers);
		
		SendSmsModel sms=new SendSmsModel("aimodosiaservice","aimodosiamessageid",new String[]{date});
		int delivered=sendSmsParallel(sms,mobileNumbers,2);
		Logs.logMobileTerminated("AIMODOSIA", "ADMIN", sms, mobileNumbers.size(), delivered);
		sms=null;
		return new SendReport(mobileNumbers.size(),delivered);
		
	}
	
	
	public static SendReport sendMoodle(SendSmsRequest request)throws Exception{
		
		ArrayList<String> mobileNumbers=new ArrayList<String>();
		//find mobileNumbersFromMoodle
		mobileNumbers.add("6973256967");
		mobileNumbers.add("6973255947");
		mobileNumbers.add("6973296874");
		mobileNumbers.add("6973296861");
		
		String serviceId=ServicesOnLoad.getMobileTerminatedServices().get(request.messageId).serviceId;
		
		SendSmsModel sms=new SendSmsModel(serviceId,request.messageId,request.replacements);
		int delivered=sendSmsParallel(sms,mobileNumbers,2);
		Logs.logMobileTerminated(request.course, request.professor, sms, mobileNumbers.size(), delivered);
		sms=null;
		request=null;
		return new SendReport(mobileNumbers.size(),delivered);
		
	}


	private static int sendSmsParallel(SendSmsModel sms,ArrayList<String> mobNumbers,int threads) throws InterruptedException {
		ExecutorService threadPool = Executors.newFixedThreadPool(threads);
		CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);

		
		for(int i=0;i<threads;i++){
			pool.submit(getCallableTask(sms, mobNumbers, i*(mobNumbers.size()/threads), (i+1)*(mobNumbers.size()/threads)));
			Thread.sleep(200);
		}


		int total=0;
		for(int i = 0; i < threads; i++){
		   try {
			String result = pool.take().get();
			total+=Integer.parseInt(result);
		   }  catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		   }
		}

		threadPool.shutdown();
		return total;	
	}
	
	private static Callable<String> getCallableTask(SendSmsModel sms,ArrayList<String> mobNumbers,int start,int end){
		
		Callable<String> callableObj = () -> {
			int count=0;
			SendSmsModel sms1=new SendSmsModel(sms);
			for(int k=start;k<end;k++){
				sms1.recipient=mobNumbers.get(k);
				SmsResponseModel smsResponse=GunetServices.testSend(sms1);
				if(smsResponse.error.equals(""))
					count++;

			}
			sms1=null;
		return String.valueOf(count);
		};
		return callableObj;
	}
}
