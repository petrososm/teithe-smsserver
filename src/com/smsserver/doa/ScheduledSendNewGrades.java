package com.smsserver.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.smsserver.models.gunetapi.SendSmsModel;

public class ScheduledSendNewGrades implements Runnable {

	@Override
	public void run() {
		
		DataSource ds=com.smsserver.sql.Pithia.getSqlConnections();
		String query="SELECT  * FROM  mytable WHERE   record_date >= DATEADD(day, -1, GETDATE())";
		SendSmsModel sms=new SendSmsModel();
		sms.messageId="testMessage";
		sms.serviceId="gradeService";
		sms.replacements=new String[2];

		try (Connection conn = ds.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()){
					sms.recipient=rs.getString("mobileNumber");
					sms.replacements[0]=rs.getString("course");
					sms.replacements[1]=rs.getString("grade");
				}					
			}				
		}
		catch(Exception e){		
		}	
	}

}
