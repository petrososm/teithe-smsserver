package com.smsserver.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.smsserver.models.logs.MobileTerminatedLogs;
import com.smsserver.sql.LocalDb;
import com.smsserver.sql.Pithia;

public class Mobiles {
	
	public static String getMobile(String username){
		try (Connection conn = Pithia.getSqlConnections().getConnection();){
			PreparedStatement stmt = conn
					.prepareStatement("SELECT RIGHT(mobile,10) FROM  v_SMS_GetPithiaCreds WHERE  (username = ?)");
			
			stmt.setString(1, username);
			ResultSet rs=stmt.executeQuery();
			String mobile=null;
			if(rs.next())
				mobile=rs.getString(1);
			stmt.close();
			conn.close();
			return mobile;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("MobileTerminated Logs Backup Interrupted");
		}
		return null;
	}

}
