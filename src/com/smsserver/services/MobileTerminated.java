package com.smsserver.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.smsserver.controllers.models.gunetapi.SendSmsModel;
import com.smsserver.controllers.models.gunetapi.SmsResponseModel;
import com.smsserver.controllers.models.site.SendReport;
import com.smsserver.controllers.models.site.SendSmsRequestDirect;
import com.smsserver.controllers.models.site.SendSmsRequestMoodle;
import com.smsserver.dao.Aimodosia;
import com.smsserver.dao.Discovery;
import com.smsserver.dao.Moodle;
import com.smsserver.services.gunetservices.GunetServices;

public class MobileTerminated {
	
	public static SendReport sendAimodosia(String date) throws Exception{
		
		ArrayList<String> mobileNumbers=Aimodosia.getMobileNumbers();
		
		SendSmsModel sms=new SendSmsModel("aimodosia","aimodosiaMsg5",new String[]{date});
		int delivered=sendSmsParallel(sms,mobileNumbers,2,false);
		Logs.logMobileTerminated("AIMODOSIA", "ADMIN", sms, mobileNumbers.size(), delivered);
		sms=null;
		return new SendReport(mobileNumbers.size(),delivered,0);
		
	}
	
	
	public static SendReport sendMoodle(SendSmsRequestMoodle request)throws Exception{
		
		ArrayList<String> enrolledStudents=Moodle.getEnrolledStudents(request.course);
		int enrolledStudentsCount=enrolledStudents.size();

		ArrayList<String> mobileNumbers=Discovery.getMobileMass(enrolledStudents);

		String serviceId=Services.getMobileTerminatedServices().get(request.messageId).serviceId;
		
		SendSmsModel sms=new SendSmsModel(serviceId,request.messageId,request.replacements);
		int delivered=sendSmsParallel(sms,mobileNumbers,2,true);
		Logs.logMobileTerminated(request.course, request.professor, sms, mobileNumbers.size(), delivered);
		return new SendReport(mobileNumbers.size(),delivered,enrolledStudentsCount);
		
	}
	public static SendReport sendDirect(SendSmsRequestDirect request) throws Exception {
		ArrayList<String> mobileNumbers=new ArrayList<String>(Arrays.asList(request.recipients));
		String serviceId=Services.getMobileTerminatedServices().get(request.messageId).serviceId;

		SendSmsModel sms=new SendSmsModel(serviceId,request.messageId,request.replacements);
		int delivered=sendSmsParallel(sms,mobileNumbers,2,false);
		Logs.logMobileTerminated(String.join(", ", request.recipients), request.sender, sms, mobileNumbers.size(), delivered);
		return new SendReport(mobileNumbers.size(),delivered,mobileNumbers.size());

	}


	private static int sendSmsParallel(SendSmsModel sms,ArrayList<String> mobNumbers,int threads,boolean test) throws InterruptedException {
		ExecutorService threadPool = Executors.newFixedThreadPool(threads);
		CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);

		
		for(int i=0;i<threads;i++){
			pool.submit(getCallableTask(sms, mobNumbers,
					(int)Math.floor(i*(mobNumbers.size()/(double)threads)), (int)Math.floor((i+1)*(mobNumbers.size()/(double)threads)),test));
			Thread.sleep(200);
		}
		int total=0;
		for(int i = 0; i < threads; i++){
		   try {
			String result = pool.take().get();
			total+=Integer.parseInt(result);
		   }  catch (ExecutionException e) {
			   e.printStackTrace();
		   }
		}

		threadPool.shutdown();
		return total;	
	}
	
	
	private static Callable<String> getCallableTask(SendSmsModel sms,ArrayList<String> mobNumbers,int start,int end,boolean test){
		
		Callable<String> callableObj = () -> {
			int count=0;
			SendSmsModel sms1=new SendSmsModel(sms);
			for(int k=start;k<end;k++){
				sms1.recipient=mobNumbers.get(k);
				SmsResponseModel smsResponse;
				if(test){	
					smsResponse=GunetServices.testSend(sms1);
				}
				else{
					smsResponse=GunetServices.sendSingleSms(sms1);
				}
				if(smsResponse.error.equals(""))
					count++;

			}
		return String.valueOf(count);
		};
		return callableObj;
	}
	
}
