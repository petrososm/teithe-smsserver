package com.smsserver.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.smsserver.dao.sqlconnections.LocalDb;

import net.jodah.expiringmap.ExpiringMap;

public class MobileChanger {
	
	static Map<Integer, String> mobileVerification;
	static{
		mobileVerification=ExpiringMap.builder()
				  .expiration(20,TimeUnit.MINUTES)
				  .build();
	}
	

	
	public static void sendVerificationCode(String username,String mobile){
		Random rand = new Random();
		int  n = rand.nextInt(90000) + 10000;
		mobileVerification.put(n,mobile);
		sendCodeSms(mobile,n);
	}
	
	private static void sendCodeSms(String mobile, int n) {
		//new sms send
		System.out.println(n);
		
	}

	public static void changeMobileNumber(String username,int verificationcode) throws Exception{
		System.out.println(verificationcode);
		System.out.println(mobileVerification);
		if(mobileVerification.containsKey(verificationcode)){
				insertMobile(username,mobileVerification.get(verificationcode));
				mobileVerification.remove(verificationcode);
		}
		else
			throw new Exception();
				
	}

	private static void insertMobile(String username,String mobile) throws SQLException{
		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement("insert into mobilenumbers(mobNumber,username) values(?,?)ON DUPLICATE KEY UPDATE mobNumber = ?");){
			
			stmt.setString(1, mobile);
			stmt.setString(2, username);
			stmt.setString(3, mobile);
			stmt.execute();	
		}
	}

	
	
	

}
