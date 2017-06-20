package com.smsserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.smsserver.controllers.models.gunetapi.DlrRequestModel;
import com.smsserver.dao.sqlconnections.LocalDb;
import com.smsserver.services.Logs;
import com.smsserver.services.models.logs.MobileOriginatedLogs;
import com.smsserver.services.models.logs.MobileTerminatedLogs;

public class ScheduledBackupLogs implements Runnable {

	@Override
	public void run() {
		System.out.println("Backing up..");	
		backupMO();
		backupMT();
		backupDlrs();

	}

	private void backupMT() {
		if (Logs.get_MTLogs().isEmpty())
			return;

		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement("INSERT INTO mobile_terminated(list,user,serviceId,messageId,replacements,sentTo,received) values (?,?,?,?,?,?,?)");){
			
			for ( MobileTerminatedLogs mt : Logs.get_MTLogs()) {
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

		Logs.get_MTLogs().clear();

	}

	private void backupMO() {
		if (Logs.get_MOLogs().isEmpty())
			return;

		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("INSERT INTO mobile_originated(mobile,keyword,body,serviceId,messageId,replacements,smsstatus) values (?,?,?,?,?,?,?)");){
			
			for ( MobileOriginatedLogs mo : Logs.get_MOLogs()) {
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
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Mobile OriginatedLogs Backup Interrupted");
		}

		Logs.get_MOLogs().clear();

	}

	private void backupDlrs() {
		if (Logs.getDeliveryReports().isEmpty())
			return;

		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("INSERT INTO DLRS(serviceId,recipient,status,error) values (?,?,?,?)");){
			
			for (DlrRequestModel dlr : Logs.getDeliveryReports()) {
				stmt.setString(1, dlr.getServiceId());
				stmt.setString(2, dlr.getRecipient());
				stmt.setString(3, dlr.getStatus());
				stmt.setString(4, dlr.getError());
				stmt.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("DlrsBackedUp");
		Logs.getDeliveryReports().clear();

	}

	public static void backup() {
		ScheduledBackupLogs sc = new ScheduledBackupLogs();
		sc.run();
	}

}
