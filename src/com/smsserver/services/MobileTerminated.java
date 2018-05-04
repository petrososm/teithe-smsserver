package com.smsserver.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateful;

import com.smsserver.controllers.models.gunetapi.SendSmsModel;
import com.smsserver.controllers.models.gunetapi.SmsResponseModel;
import com.smsserver.controllers.models.site.SendReport;
import com.smsserver.controllers.models.site.SendSmsRequestDirect;
import com.smsserver.controllers.models.site.SendSmsRequestMoodle;
import com.smsserver.dao.Aimodosia;
import com.smsserver.dao.Discovery;
import com.smsserver.dao.Courses;
import com.smsserver.services.auth.AuthenticatedUsers;
import com.smsserver.services.gunetservices.GunetServices;

@Stateful
public class MobileTerminated {
	@EJB
	Discovery discovery;
	@EJB
	Services services;
	@EJB
	Aimodosia aimodosia;
	@EJB
	Logs logs;
	@EJB
	Courses moodle;
	@EJB
	GunetServices gunet;
	@EJB
	AuthenticatedUsers authUsers;
        
        
        private final boolean TEST=Boolean.parseBoolean(GetPropertyValues.getProperties().getProperty("testSend"));

	private static Logger LOGGER = Logger.getLogger(Discovery.class.getName());

	public SendReport sendAimodosia(String date) throws Exception {

		ArrayList<String> mobileNumbers = new ArrayList<String>();
                mobileNumbers.addAll(aimodosia.getMobileNumbers());
		SendSmsModel sms = new SendSmsModel("aimodosia", "aimodosiaMsg5", new String[] { date });
		int delivered = sendSmsParallel(sms, mobileNumbers, 2, TEST);
		logs.logMobileTerminated("AIMODOSIA", "ADMIN", sms, mobileNumbers.size(), delivered);
		sms = null;
		return new SendReport(mobileNumbers.size(), delivered);

	}

	public SendReport sendMoodle(SendSmsRequestMoodle request) throws Exception {

		ArrayList<String> mobileNumbers = discovery.getMobileMass(request.getCourse());

		String serviceId = services.getMobileTerminatedServices().get(request.getMessageId()).getServiceId();

		SendSmsModel sms = new SendSmsModel(serviceId, request.getMessageId(), request.getReplacements());
		int delivered = sendSmsParallel(sms, mobileNumbers, 2,TEST);
		logs.logMobileTerminated(request.getCourse(), request.getProfessor(), sms, mobileNumbers.size(), delivered);
		return new SendReport(mobileNumbers.size(), delivered);

	}

	public SendReport sendDirect(SendSmsRequestDirect request) throws Exception {
		ArrayList<String> mobileNumbers = new ArrayList<String>(Arrays.asList(request.getRecipients()));
		String serviceId = services.getMobileTerminatedServices().get(request.getMessageId()).getServiceId();

		SendSmsModel sms = new SendSmsModel(serviceId, request.getMessageId(), request.getReplacements());
		int delivered = sendSmsParallel(sms, mobileNumbers, 2, TEST);
		logs.logMobileTerminated(String.join(", ", request.getRecipients()), request.getSender(), sms,
				mobileNumbers.size(), delivered);
		return new SendReport(mobileNumbers.size(), delivered);

	}

	private int sendSmsParallel(SendSmsModel sms, ArrayList<String> mobNumbers, int threads, boolean test)
			throws InterruptedException {
		ExecutorService threadPool = Executors.newFixedThreadPool(threads);
		CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);

		for (int i = 0; i < threads; i++) {
			pool.submit(getCallableTask(sms, mobNumbers, (int) Math.floor(i * (mobNumbers.size() / (double) threads)),
					(int) Math.floor((i + 1) * (mobNumbers.size() / (double) threads)), test));
			Thread.sleep(200);
		}
		int total = 0;
		for (int i = 0; i < threads; i++) {
			try {
				String result = pool.take().get();
				total += Integer.parseInt(result);
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		threadPool.shutdown();
		return total;
	}

	private Callable<String> getCallableTask(SendSmsModel sms, ArrayList<String> mobNumbers, int start, int end,
			boolean test) {

		Callable<String> callableObj = () -> {
			int count = 0;
			SendSmsModel sms1 = new SendSmsModel(sms);
			for (int k = start; k < end; k++) {
				sms1.setRecipient(mobNumbers.get(k));
				SmsResponseModel smsResponse;
				if (test) {
					smsResponse = gunet.testSend(sms1);
				} else {
					smsResponse = gunet.sendSingleSms(sms1);
				}
				if (smsResponse.getError().equals(""))
					count++;

			}
			return String.valueOf(count);
		};
		return callableObj;

	}

}
