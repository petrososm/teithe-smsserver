package com.smsserver.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.smsserver.controllers.models.gunetapi.SendSmsModel;
import com.smsserver.dao.Discovery;
import com.smsserver.services.gunetservices.GunetServices;

import net.jodah.expiringmap.ExpiringMap;

@Singleton
public class MobileChanger {

	@EJB
	Discovery discovery;
	@EJB
	GunetServices gunet;

	Map<Integer, String> mobileVerification;

	public MobileChanger() {
		mobileVerification = ExpiringMap.builder().expiration(20, TimeUnit.MINUTES).build();
	}

	public void sendVerificationCode(String username, String mobile) {
		Random rand = new Random();
		int n = rand.nextInt(90000) + 10000;
		mobileVerification.put(n, mobile);
		sendCodeSms(mobile, n);
	}

	private void sendCodeSms(String mobile, int n) {
		SendSmsModel sms = new SendSmsModel("mobileConfirm", "mobileConfirmMsg", new String[] { String.valueOf(n) },
				mobile, null);
		gunet.sendSingleSms(sms);

	}

	public void changeMobileNumber(String username, int verificationcode) throws Exception {
		System.out.println(verificationcode);
		System.out.println(mobileVerification);
		if (mobileVerification.containsKey(verificationcode)) {
			discovery.updateMobile(username, mobileVerification.get(verificationcode));
			mobileVerification.remove(verificationcode);
		} else
			throw new Exception();

	}

}
