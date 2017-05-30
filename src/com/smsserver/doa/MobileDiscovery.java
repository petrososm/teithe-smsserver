package com.smsserver.doa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smsserver.sql.LocalDb;
import com.smsserver.sql.Pithia;

import javassist.NotFoundException;

public class MobileDiscovery {
	

	public static String getUsername(String mobile) throws NotFoundException{
		try{
			return findUsernameLocal(mobile);
		}
		catch(SQLException e ){
			try{
				return findUsernamePithia(mobile);
			}
			catch(SQLException ex){
				throw new NotFoundException(mobile);
			}
		}
	}
	
	public static String getMobile(String username) throws NotFoundException{
		try{
			return findMobileLocal(username);
		}
		catch(SQLException e ){
			try{
				return findMobilePithia(username);
			}
			catch(SQLException ex){
				throw new NotFoundException(username);
			}
		}
	}
	
	private static String findUsernameLocal(String mobile)throws SQLException  {
		String username=null;
		try (Connection conn = LocalDb.getSqlConnections().getConnection();){
			PreparedStatement stmt = conn
					.prepareStatement("select username from mobilenumbers where mobNumber=?");
			stmt.setString(1, mobile);
			ResultSet rs=stmt.executeQuery();
			if(rs.next())
				username=rs.getString(1);
			
			conn.close();
			return username;
		}
		
	}
	private static String findUsernamePithia(String mobile) throws SQLException {
		try (Connection conn = Pithia.getSqlConnections().getConnection();){
			PreparedStatement stmt = conn
					.prepareStatement("SELECT username FROM  v_SMS_GetPithiaCreds WHERE  RIGHT(mobile,10) = ?)");
			
			stmt.setString(1, mobile);
			ResultSet rs=stmt.executeQuery();
			String username=null;
			if(rs.next())
				username=rs.getString(1);
			stmt.close();
			conn.close();
			return username;
		} 

	}

	
	private static String findMobileLocal(String username)throws SQLException  {
		String mobile=null;
		try (Connection conn = LocalDb.getSqlConnections().getConnection();){
			PreparedStatement stmt = conn
					.prepareStatement("select mobNumber from mobilenumbers where username=?");
			stmt.setString(1, username);
			ResultSet rs=stmt.executeQuery();
			rs.next();
			mobile=rs.getString(1);
			
			conn.close();
			return mobile;
		}
		
	}
	private static String findMobilePithia(String username) throws SQLException {
		try (Connection conn = Pithia.getSqlConnections().getConnection();){
			PreparedStatement stmt = conn
					.prepareStatement("SELECT RIGHT(mobile,10) FROM  v_SMS_GetPithiaCreds WHERE   username= ?");
			
			stmt.setString(1, username);
			ResultSet rs=stmt.executeQuery();
			String mobile=null;
			rs.next();
			mobile=rs.getString(1);
			stmt.close();
			conn.close();
			return mobile;
		} 	
	}
	


}
