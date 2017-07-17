package com.smsserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import javassist.NotFoundException;

@Stateless
public class Discovery {

    @Resource(lookup = "jdbc/pithia")
    DataSource pithia;
    @Resource(lookup = "jdbc/localdb")
    DataSource localdb;
	
	
	public  String getUsername(String mobile) throws NotFoundException {
		try {
			return findUsernameLocal(mobile);
		} catch (SQLException e) {
			try {
				return findUsernamePithia(mobile);
			} catch (SQLException ex) {
				e.printStackTrace();
				throw new NotFoundException(mobile);
			}
		}
	}

	public  String getMobile(String username) throws NotFoundException {
		try {
			return findMobileLocal(username);
		} catch (SQLException e) {
			try {
				return findMobilePithia(username);
			} catch (SQLException ex) {
				e.printStackTrace();
				throw new NotFoundException(username);
			}
		}
	}
	public  ArrayList<String> getMobileMass(ArrayList<String> usernames) throws SQLException {
		ArrayList<String> mobNumbers=new ArrayList<String>();
		try(
			Connection local = localdb.getConnection();
			Connection pithia1 = pithia.getConnection();
			PreparedStatement stmtLocal = local.prepareStatement("select mobNumber from mobilenumbers where username=?");
			PreparedStatement stmtPithia = pithia1
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
					    updateMobile(u,rs.getString(1));
					} 	
				}			
			}	
			rs=null;
		}
		
		return mobNumbers;
		
	}

	private  String findUsernameLocal(String mobile) throws SQLException {
		try (Connection conn = localdb.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("select username from mobilenumbers where mobNumber=?");) {

			stmt.setString(1, mobile);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			
			return rs.getString(1);
			
		}

	}

	private  String findUsernamePithia(String mobile) throws SQLException {
		try (Connection conn = pithia.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT username FROM  v_SMS_GetPithiaCreds WHERE  RIGHT(mobile,10) = ?");) {

			stmt.setString(1, mobile);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString(1);

		}
	}

	private  String findMobileLocal(String username) throws SQLException {
		try (Connection conn = localdb.getConnection();
				PreparedStatement stmt = conn.prepareStatement("select mobNumber from mobilenumbers where username=?");) {
			
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			
			return rs.getString(1);
			
		}


	}

	private  String findMobilePithia(String username) throws SQLException {
		try (Connection conn =pithia.getConnection();
				PreparedStatement stmt = conn
				.prepareStatement("SELECT RIGHT(mobile,10) FROM  v_SMS_GetPithiaCreds WHERE   username= ? and mobile != '-'")) {
			
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			updateMobile(username,rs.getString(1));
			return rs.getString(1);

		}
	}
	
	public void updateMobile(String username,String mobile) throws SQLException{
		try (Connection conn = localdb.getConnection();
				PreparedStatement stmt = conn
				.prepareStatement("insert into mobilenumbers(mobNumber,username) values(?,?)ON DUPLICATE KEY UPDATE mobNumber = ?");){
			
			stmt.setString(1, mobile);
			stmt.setString(2, username);
			stmt.setString(3, mobile);
			stmt.execute();	
		}
	}

}
