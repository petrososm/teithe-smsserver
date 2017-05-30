package com.smsserver.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.smsserver.gunetservices.GunetServices;
import com.smsserver.models.gunetapi.SendSmsModel;
import com.smsserver.models.gunetapi.SmsResponseModel;
import com.smsserver.sql.Nireas;
import com.smsserver.sql.Pithia;

public class MobileTerminated {
	
	public static void sendAimodosia(String date) throws Exception{
		
		ArrayList<String> mobileNumbers=new ArrayList<String>();
		try (Connection conn = Nireas.getSqlConnections().getConnection();){
			PreparedStatement stmt = conn
					.prepareStatement("SELECT mobile FROM users u"
							+ " where mobile IS NOT NULL "
							+ "AND u.id NOT IN(  select r.donorId "
							+ "from donationdonor r,dates d  "
							+ "where  STR_TO_DATE(d.date, '%d-%m-%Y') > DATE_SUB(CURDATE(), INTERVAL 6 MONTH) "
							+ "and r.donationId=d.id)");
			
			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				mobileNumbers.add(rs.getString(1));
			stmt.close();
			conn.close();
		} 
		
		SendSmsModel sms=new SendSmsModel("aimodosiaservice","aimodosiamessageid",new String[]{date});
		int delivered=sendSmsParallel(sms,mobileNumbers);
		Logs.logMobileTerminated("AIMODOSIA", "ADMIN", sms, mobileNumbers.size(), delivered);
		
		
	}

	private static int sendSmsParallel(SendSmsModel sms,ArrayList<String> mobNumbers) throws InterruptedException {
		int threads=2;
		ExecutorService threadPool = Executors.newFixedThreadPool(threads);
		CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);

		Callable<String> callableObj = () -> {
			int count=0;
			SendSmsModel sms1=new SendSmsModel(sms);
			for(int k=0;k<mobNumbers.size()/2;k++){
				sms1.recipient=mobNumbers.get(k);
				SmsResponseModel smsResponse=GunetServices.testSend(sms1);
				if(smsResponse.error.equals(""))
					count++;

			}
		return String.valueOf(count);
		};
		
		Callable<String> callableObj2 = () -> {
			int count=0;
			SendSmsModel sms1=new SendSmsModel(sms);
			for(int k=0;k<mobNumbers.size()/2;k++){
				sms1.recipient=mobNumbers.get(k);
				SmsResponseModel smsResponse=GunetServices.testSend(sms1);
				if(smsResponse.error.equals(""))
					count++;

			}
		return String.valueOf(count);
		};

		pool.submit(callableObj);
		Thread.sleep(300);
		pool.submit(callableObj2);

		int total=0;
		for(int i = 0; i < 2; i++){
		   try {
			String result = pool.take().get();
			total+=Integer.parseInt(result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		   //Compute the result
		}

		threadPool.shutdown();
		return total;	
	}
}
