package com.smsserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.smsserver.dao.sqlconnections.LocalDb;
import com.smsserver.dao.sqlconnections.Nireas;

public class Aimodosia {
	
	public static ArrayList<String> getMobileNumbers() throws SQLException{
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
		
		query="select mobile from aimodosia where blacklist == 1";
		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){

			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				mobileNumbers.add(rs.getString(1));
		} 
		
		query="select mobile from aimodosia where blacklist == 0";
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
	
	public static void addBlacklist(String mobile) throws SQLException{
		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement("insert into aimodosia(mobile,blacklist) values(?,1)ON DUPLICATE KEY UPDATE blacklist = 1");){
			
			stmt.setString(1, mobile);
			stmt.execute();	
		}
		
	}
	public static void addSubscriber(String mobile) throws SQLException{
		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("insert into aimodosia(mobile,blacklist) values(?,0)ON DUPLICATE KEY UPDATE blacklist = 0");){
			
			stmt.setString(1, mobile);
			stmt.execute();	
		}
	}
	


	public static String getLastAimodosia(String mobile) throws SQLException {
		String query="SELECT STR_TO_DATE(d.date, '%d-%m-%Y')as date FROM "
				+ "dates d,donationdonor r,users u "
				+ "where d.id=r.donationId and r.donorId=u.id and u.mobile=? "
				+ "order by date desc limit 1;";
		try (Connection conn = Nireas.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){
			stmt.setString(1, mobile);
			
			ResultSet rs=stmt.executeQuery();

			if(rs.next())
				return rs.getString(1);
		} 
		return null;
		
	}

	public static String getFialesKatanal(String mobile) throws SQLException {
		String query="SELECT r.flasks  "
				+ "from recipient r,users u "
				+ "where u.id=r.donorId and u.mobile= ? ;";
		try (Connection conn = Nireas.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){
			stmt.setString(1, mobile);
			
			ResultSet rs=stmt.executeQuery();

			if(rs.next())
				return rs.getString(1);
		} 
		return null;
		
	}

	public static String getFiales(String mobile) throws SQLException {
		String query="select flasks from users where mobile = ? ";
		try (Connection conn = Nireas.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){
			stmt.setString(1, mobile);
			
			ResultSet rs=stmt.executeQuery();

			if(rs.next())
				return rs.getString(1);
		} 
		return null;
		
		
	}
	
}