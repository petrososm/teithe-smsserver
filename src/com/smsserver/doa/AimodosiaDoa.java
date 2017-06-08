package com.smsserver.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.smsserver.sql.LocalDb;
import com.smsserver.sql.Nireas;

public class AimodosiaDoa {
	
	static ArrayList<String> getMobileNumbers() throws SQLException{
		ArrayList<String> mobileNumbers=new ArrayList<String>();
		ArrayList<String> blacklistedNumbers=new ArrayList<String>();
		
		String query="SELECT mobile FROM users u"
				+ " where mobile IS NOT NULL "
				+ "AND u.id NOT IN(  select r.donorId "
				+ "from donationdonor r,dates d  "
				+ "where  STR_TO_DATE(d.date, '%d-%m-%Y') > DATE_SUB(CURDATE(), INTERVAL 6 MONTH) "
				+ "and r.donationId=d.id)";
		try (Connection conn = Nireas.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){

			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				mobileNumbers.add(rs.getString(1));

		} 
		
		query="select mobile from aimodosia_blacklist";
		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){

			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				blacklistedNumbers.add(rs.getString(1));
		} 
		
		
		mobileNumbers.removeAll(blacklistedNumbers);
		blacklistedNumbers=null;
		return mobileNumbers;
	}
	
	static void addBlacklist(String mobile) throws SQLException{
		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement("insert into aimodosia_blacklist(mobile) values(?)");){
			
			stmt.setString(1, mobile);
			stmt.execute();	
		}
		
	}
	static void removeBlacklist(String mobile) throws SQLException{
		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement("delete from aimodosia_blacklist where mobile = ?");){
			
			stmt.setString(1, mobile);
			stmt.execute();	
		}
	}
	
}
