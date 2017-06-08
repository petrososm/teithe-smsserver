package com.smsserver.doa;

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

public class MobileTerminated {
	
	public static SendReport sendAimodosia(String date) throws Exception{
		
		ArrayList<String> mobileNumbers=AimodosiaDoa.getMobileNumbers();
		
		SendSmsModel sms=new SendSmsModel("aimodosiaservice","aimodosiamessageid",new String[]{date});
		int delivered=sendSmsParallel(sms,mobileNumbers,2);
		Logs.logMobileTerminated("AIMODOSIA", "ADMIN", sms, mobileNumbers.size(), delivered);
		sms=null;
		return new SendReport(mobileNumbers.size(),delivered,0);
		
	}
	
	
	public static SendReport sendMoodle(SendSmsRequest request)throws Exception{
		
		ArrayList<String> enrolledStudents=MoodleDoa.getEnrolledStudents(request.course);
		int enrolledStudentsCount=enrolledStudents.size();

		ArrayList<String> mobileNumbers=Discovery.getMobileMass(enrolledStudents);

		String serviceId=ServicesOnLoad.getMobileTerminatedServices().get(request.messageId).serviceId;
		
		SendSmsModel sms=new SendSmsModel(serviceId,request.messageId,request.replacements);
		int delivered=sendSmsParallel(sms,mobileNumbers,2);
		Logs.logMobileTerminated(request.course, request.professor, sms, mobileNumbers.size(), delivered);
		sms=null;
		request=null;
		enrolledStudents=null;
		return new SendReport(mobileNumbers.size(),delivered,enrolledStudentsCount);
		
	}


	private static int sendSmsParallel(SendSmsModel sms,ArrayList<String> mobNumbers,int threads) throws InterruptedException {
		ExecutorService threadPool = Executors.newFixedThreadPool(threads);
		CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);

		
		for(int i=0;i<threads;i++){
			pool.submit(getCallableTask(sms, mobNumbers,
					(int)Math.floor(i*(mobNumbers.size()/(double)threads)), (int)Math.floor((i+1)*(mobNumbers.size()/(double)threads))));
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
