package com.smsserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import com.smsserver.services.models.mobileoriginated.Message;

import javassist.NotFoundException;

@Stateless
public class Aimodosia {
	
    @Resource(lookup = "jdbc/nireas")
    DataSource nireas;
    @Resource(lookup = "jdbc/localdb")
    DataSource localdb;
    
	public ArrayList<String> getMobileNumbers() throws SQLException{
		ArrayList<String> mobileNumbers=new ArrayList<String>();
		ArrayList<String> blacklistedNumbers=new ArrayList<String>();
		
		String query="SELECT mobile FROM users u"
				+ " where mobile IS NOT NULL "
				+ "AND u.id NOT IN(  select r.donorId "
				+ "from donationdonor r,dates d  "
				+ "where  STR_TO_DATE(d.date, '%d-%m-%Y') > DATE_SUB(CURDATE(), INTERVAL 6 MONTH) "
				+ "and r.donationId=d.id)";
		try (Connection conn = nireas.getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){

			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				mobileNumbers.add(rs.getString(1));

		} 
		
		query="select mobile from aimodosia where blacklist =1";
		try (Connection conn = localdb.getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){

			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				mobileNumbers.add(rs.getString(1));
		} 
		
		query="select mobile from aimodosia where blacklist = 0";
		try (Connection conn = localdb.getConnection();
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
	
	public String[] queryAimodosia(Message message,String mobile,String[] body,int extra) throws SQLException, NotFoundException{
		String[] replacements = new String[message.getNumberOfReplacements()];
		DataSource ds;
		if(message.getDatabase().equals("localdb"))
			ds=localdb;
		else
			ds=nireas;
		System.out.println(message.getQuery());
		try (Connection conn = ds.getConnection(); PreparedStatement stmt = conn.prepareStatement(message.getQuery());) {

			stmt.setString(1, mobile);
			for (int i = 1; i < message.getQueryParams(); i++)// ksekinaei apo 1
															// giati mia
															// parametros einai
															// standard
				stmt.setString(i + 1, body[i - 1 + extra]);
			// -1 gia na paei sto 0 . extra an iparxei secondary

			
			try (ResultSet rs = stmt.executeQuery();) {
				if (!rs.next()) {
					throw new NotFoundException("query");
				} else {
					for (int i = 0; i < replacements.length; i++)
						replacements[i] = rs.getString(i + 1);
				}
			}
		}
		return replacements;

		
	}
	
}
