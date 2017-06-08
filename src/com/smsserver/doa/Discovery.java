package com.smsserver.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.smsserver.sql.LocalDb;
import com.smsserver.sql.Pithia;

import javassist.NotFoundException;

public class Discovery {

	public static String getUsername(String mobile) throws NotFoundException {
		try {
			return findUsernameLocal(mobile);
		} catch (SQLException e) {
			try {
				return findUsernamePithia(mobile);
			} catch (SQLException ex) {
				throw new NotFoundException(mobile);
			}
		}
	}

	public static String getMobile(String username) throws NotFoundException {
		try {
			return findMobileLocal(username);
		} catch (SQLException e) {
			try {
				return findMobilePithia(username);
			} catch (SQLException ex) {
				throw new NotFoundException(username);
			}
		}
	}
	public static ArrayList<String> getMobileMass(ArrayList<String> usernames) throws SQLException {
		ArrayList<String> mobNumbers=new ArrayList<String>();
		try(
			Connection local = LocalDb.getSqlConnections().getConnection();
			Connection pithia = Pithia.getSqlConnections().getConnection();
			PreparedStatement stmtLocal = local.prepareStatement("select mobNumber from mobilenumbers where username=?");
			PreparedStatement stmtPithia = pithia
					.prepareStatement("SELECT RIGHT(mobile,10) FROM  v_SMS_GetPithiaCreds WHERE   username= ? and mobile != '-'");
			){
			
			
			ResultSet rs;
			for(String u : usernames){
				stmtLocal.setString(1, u);
				rs = stmtLocal.executeQuery();
				if (rs.next() ) {    
				    mobNumbers.add(rs.getString(1));
				} 
				else{
					stmtPithia.setString(1, u);
					rs=stmtPithia.executeQuery();
					if (rs.next() ) {    
					    mobNumbers.add(rs.getString(1));
					} 	
				}			
			}	
			rs=null;
		}
		
		return mobNumbers;
		
	}

	private static String findUsernameLocal(String mobile) throws SQLException {
		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("select username from mobilenumbers where mobNumber=?");) {

			stmt.setString(1, mobile);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString(1);
			
		}

	}

	private static String findUsernamePithia(String mobile) throws SQLException {
		try (Connection conn = Pithia.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT username FROM  v_SMS_GetPithiaCreds WHERE  RIGHT(mobile,10) = ?)");) {

			stmt.setString(1, mobile);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString(1);

		}

	}

	private static String findMobileLocal(String username) throws SQLException {
		try (Connection conn = LocalDb.getSqlConnections().getConnection();
				PreparedStatement stmt = conn.prepareStatement("select mobNumber from mobilenumbers where username=?");) {
			
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString(1);
		}

	}

	private static String findMobilePithia(String username) throws SQLException {
		try (Connection conn = Pithia.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement("SELECT RIGHT(mobile,10) FROM  v_SMS_GetPithiaCreds WHERE   username= ? and mobile != '-'")) {
			
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString(1);

		}
	}

}
