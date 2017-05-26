package com.smsserver.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.smsserver.models.gunetapi.DlrRequestModel;
import com.smsserver.models.logs.MobileOriginatedLogs;
import com.smsserver.models.logs.MobileTerminatedLogs;
import com.smsserver.sql.LocalDb;

public class ScheduledBackupLogs implements Runnable {

	@Override
	public void run() {
		System.out.println("Backing up..");	
		backupMO();
		backupMT();
		backupDlrs();

	}

	private void backupMT() {
		if (Logs._MTLogs.isEmpty())
			return;

		try (Connection conn = LocalDb.getSqlConnections().getConnection();){
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO mobile_terminated(list,user,serviceId,messageId,replacements,sentTo,received) values (?,?,?,?,?,?,?)");
			for ( MobileTerminatedLogs mt : Logs._MTLogs) {
				stmt.setString(1, mt.list);
				stmt.setString(2, mt.user);
				stmt.setString(3, mt.serviceId);
				stmt.setString(4, mt.messageId);
				stmt.setString(5, mt.replacements);
				stmt.setInt(6, mt.sentTo);
				stmt.setInt(7, mt.received);
				stmt.executeUpdate();
			}
			System.out.println("MobileTerminated Logs Backed Up");
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("MobileTerminated Logs Backup Interrupted");
		}

		Logs._MOLogs.clear();

	}

	private void backupMO() {
		if (Logs._MOLogs.isEmpty())
			return;

		try (Connection conn = LocalDb.getSqlConnections().getConnection();){
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO mobile_originated(mobile,keyword,body,serviceId,messageId,replacements,smsstatus) values (?,?,?,?,?,?,?)");
			for ( MobileOriginatedLogs mo : Logs._MOLogs) {
				stmt.setString(1, mo.mobile);
				stmt.setString(2, mo.keyword);
				stmt.setString(3, mo.body);
				stmt.setString(4, mo.serviceId);
				stmt.setString(5, mo.messageId);
				stmt.setString(6, mo.replacements);
				stmt.setString(7, mo.smsstatus);
				stmt.executeUpdate();
			}
			System.out.println("Mobile OriginatedLogs Backed Up");
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Mobile OriginatedLogs Backup Interrupted");
		}

		Logs._MOLogs.clear();

	}

	private void backupDlrs() {
		if (Logs.deliveryReports.isEmpty())
			return;

		try (Connection conn = LocalDb.getSqlConnections().getConnection();){
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO DLRS(serviceId,recipient,status,error) values (?,?,?,?)");
			for (DlrRequestModel dlr : Logs.deliveryReports) {
				stmt.setString(1, dlr.getServiceId());
				stmt.setString(2, dlr.getRecipient());
				stmt.setString(3, dlr.getStatus());
				stmt.setString(4, dlr.getError());
				stmt.executeUpdate();
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("DlrsBackedUp");
		Logs.deliveryReports.clear();

	}

	public static void backup() {
		ScheduledBackupLogs sc = new ScheduledBackupLogs();
		sc.run();
	}

}
