package com.smsserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Stateful;
import javax.sql.DataSource;

import com.smsserver.controllers.models.gunetapi.DlrRequestModel;
import com.smsserver.services.Logs;
import com.smsserver.services.models.logs.MobileOriginatedLogs;
import com.smsserver.services.models.logs.MobileTerminatedLogs;

@Singleton
public class ScheduledBackupLogs {
    
	@Resource(lookup = "jdbc/localdb")
    DataSource localdb;

    
	private ArrayList<DlrRequestModel> deliveryReports = new ArrayList<DlrRequestModel>();
	private ArrayList<MobileOriginatedLogs> _MOLogs = new ArrayList<MobileOriginatedLogs>();
	private ArrayList<MobileTerminatedLogs> _MTLogs = new ArrayList<MobileTerminatedLogs>();

	
    @Schedule(minute = "*/10", hour = "*")
    @PreDestroy
	public void backup() {
		System.out.println("Backing up..");	
		backupMO();
		backupMT();
		backupDlrs();

	}
	

	private void backupMT() {
		if (_MTLogs.isEmpty())
			return;

		try (Connection conn = localdb.getConnection();
				PreparedStatement stmt = conn
				.prepareStatement("INSERT INTO mobile_terminated(list,user,serviceId,messageId,replacements,sentTo,received) values (?,?,?,?,?,?,?)");){
			
			for ( MobileTerminatedLogs mt : _MTLogs) {
				stmt.setString(1, mt.getList());
				stmt.setString(2, mt.getUser());
				stmt.setString(3, mt.getServiceId());
				stmt.setString(4, mt.getMessageId());
				stmt.setString(5, mt.getReplacements());
				stmt.setInt(6, mt.getSentTo());
				stmt.setInt(7, mt.getReceived());
				stmt.executeUpdate();
			}
			System.out.println("MobileTerminated Logs Backed Up");
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("MobileTerminated Logs Backup Interrupted");
		}

		_MTLogs.clear();

	}

	private void backupMO() {
		if (_MOLogs.isEmpty())
			return;

		try (Connection conn = localdb.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("INSERT INTO mobile_originated(mobile,keyword,body,serviceId,messageId,replacements,smsstatus) values (?,?,?,?,?,?,?)");){
			
			for ( MobileOriginatedLogs mo : _MOLogs) {
				stmt.setString(1, mo.getMobile());
				stmt.setString(2, mo.getKeyword());
				stmt.setString(3, mo.getBody());
				stmt.setString(4, mo.getServiceId());
				stmt.setString(5, mo.getMessageId());
				stmt.setString(6, mo.getReplacements());
				stmt.setString(7, mo.getSmsstatus());
				stmt.executeUpdate();
			}
			System.out.println("Mobile OriginatedLogs Backed Up");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Mobile OriginatedLogs Backup Interrupted");
		}

		_MOLogs.clear();

	}

	private void backupDlrs() {
		if (deliveryReports.isEmpty())
			return;

		try (Connection conn = localdb.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("INSERT INTO DLRS(serviceId,recipient,status,error) values (?,?,?,?)");){
			
			for (DlrRequestModel dlr : deliveryReports) {
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
		deliveryReports.clear();

	}

	public void addMoLog(MobileOriginatedLogs log){
		_MOLogs.add(log);
	}
	public void addMtLog(MobileTerminatedLogs log){
		_MTLogs.add(log);
	}
	public void addDeliveryReport(DlrRequestModel dlr){
		deliveryReports.add(dlr);
	}
	
}
