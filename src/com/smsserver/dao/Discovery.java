package com.smsserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import com.smsserver.controllers.resources.UserInfo;
import com.smsserver.services.auth.AuthenticatedUsers;

import javassist.NotFoundException;

@Stateless
public class Discovery {

	@Resource(lookup = "jdbc/pithia")
	DataSource pithia;
	@Resource(lookup = "jdbc/localdb")
	DataSource localdb;
	@EJB
	AuthenticatedUsers authUsers;

	private static Logger LOGGER = Logger.getLogger(Discovery.class.getName());

	public String getUsername(String mobile) throws NotFoundException {
		try {
			return findUsernameLocal(mobile);
		} catch (SQLException e) {
			try {
				String username = findUsernamePithia(mobile);
				updateMobile(username, mobile);
				return username;
			} catch (SQLException ex) {
				LOGGER.log(Level.INFO, "Username not found for mobile: " + mobile, e);
				throw new NotFoundException(mobile);
			}
		}
	}

	public String getMobile(String username) throws NotFoundException {
		try {
			return findMobileLocal(username);
		} catch (SQLException e) {
			try {
				String mobile = findMobilePithia(username);
				updateMobile(username, mobile);
				return mobile;
			} catch (SQLException ex) {
				LOGGER.log(Level.INFO, "Mobile not found for username: " + username, e);
				throw new NotFoundException(username);
			}
		}
	}

	public ArrayList<String> getMobileMass(String course) throws SQLException {
		HashMap<String, String> users = new HashMap<String, String>();
		ArrayList<String> mobNumbers;

		PreparedStatement stmtDrop = null;
		try (Connection local = localdb.getConnection();
				Connection pithia1 = pithia.getConnection();
				PreparedStatement stmtLocal = local
						.prepareStatement("select username,mobNumber from mobilenumbers natural join tmp");
				PreparedStatement stmtCreate = local
						.prepareStatement("create temporary table tmp(username varchar(30) primary key) ENGINE=MEMORY");
				PreparedStatement stmtInsert = local.prepareStatement("insert into tmp(username) values(?)");
				PreparedStatement stmtPithia = pithia1.prepareStatement(
						"SELECT username,RIGHT(mobile,10)as mobile FROM [eUniCentral].[dbo].[v_SMS_eggrafi] where coursecode=?");) {

			stmtDrop=local.prepareStatement("drop table tmp");
			stmtPithia.setString(1, course);

			ResultSet rs = stmtPithia.executeQuery();
			stmtCreate.execute();

			while (rs.next()) {
				if (!rs.getString("mobile").equals(""))
					users.put(rs.getString("username"), rs.getString("mobile"));
				stmtInsert.setString(1, rs.getString("username"));
				stmtInsert.addBatch();
			}

			stmtInsert.executeBatch();




			rs = stmtLocal.executeQuery();
			while (rs.next()) {
				users.put(rs.getString("username"), rs.getString("mobNumber"));
			}

			mobNumbers = new ArrayList<String>(users.values());

		}

		finally {
			if(stmtDrop!=null){
				stmtDrop.execute();
				stmtDrop.close();
			}
		}
		return mobNumbers;

	}

	private String findUsernameLocal(String mobile) throws SQLException {
		try (Connection conn = localdb.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("select username from mobilenumbers where mobNumber=?");) {

			stmt.setString(1, mobile);
			ResultSet rs = stmt.executeQuery();
			rs.next();

			return rs.getString(1);

		}

	}

	private String findUsernamePithia(String mobile) throws SQLException {
		try (Connection conn = pithia.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("SELECT username FROM  v_SMS_GetPithiaCreds WHERE  RIGHT(mobile,10) = ?");) {

			stmt.setString(1, mobile);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getString(1);

		}
	}

	private String findMobileLocal(String username) throws SQLException {
		try (Connection conn = localdb.getConnection();
				PreparedStatement stmt = conn
						.prepareStatement("select mobNumber from mobilenumbers where username=?");) {

			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			rs.next();

			return rs.getString(1);

		}

	}

	private String findMobilePithia(String username) throws SQLException {
		try (Connection conn = pithia.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT RIGHT(mobile,10) FROM  v_SMS_GetPithiaCreds WHERE   username= ? and mobile != '-'")) {

			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			updateMobile(username, rs.getString(1));
			return rs.getString(1);

		}
	}

	public void updateMobile(String username, String mobile) throws SQLException {
		String fullname;
		if (authUsers.contains(username))
			fullname = authUsers.get(username).getFullname();
		else
			fullname = getFullNamePithia(username);
		try (Connection conn = localdb.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"insert into mobilenumbers(mobNumber,username,fullname) values(?,?,?)ON DUPLICATE KEY UPDATE mobNumber = ?");) {

			stmt.setString(1, mobile);
			stmt.setString(2, username);
			stmt.setString(3, fullname);
			stmt.setString(4, mobile);
			stmt.execute();
		}
	}

	public String getFullNamePithia(String username) throws SQLException {
		try (Connection conn = pithia.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT top 1[first],[last] "
						+ "FROM [eUniCentral].[dbo].[v_SMS_eggrafi] " + "where username=?")) {

			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				return rs.getString("last") + " " + rs.getString("first");

			return null;

		}
	}

	public ArrayList<UserInfo> findUser(String query) throws Exception {
		String qpithia = "SELECT TOP 10 t.username,t.fullname,RIGHT(t.mobile,10)as mobile  "
				+ "FROM [eUniCentral].[dbo].[v_SMS_GetPithiaCreds] t " + "where (username LIKE ? " + "or mobile LIKE ? "
				+ "or am LIKE ? " + "or fullname LIKE ?) " + "and isnumeric(mobile)=1 and mobile != '-' ";
		String qlocal = "SELECT * FROM smsserver.mobilenumbers " + "where username like ? " + "or mobNumber like ? "
				+ "or fullname like ?" + "limit 5 ";
		ArrayList<UserInfo> results = new ArrayList<UserInfo>();
		ArrayList<UserInfo> localresults = new ArrayList<UserInfo>();
		try (Connection conn = pithia.getConnection(); PreparedStatement stmt = conn.prepareStatement(qpithia)) {

			stmt.setString(1, "%" + query + "%");
			stmt.setString(2, "%" + query + "%");
			stmt.setString(3, "%" + query + "%");
			stmt.setString(4, "%" + query + "%");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				results.add(new UserInfo(rs.getString("username"), rs.getString("mobile"), rs.getString("fullname")));
			}
		}
		try (Connection conn = localdb.getConnection(); PreparedStatement stmt = conn.prepareStatement(qlocal)) {

			stmt.setString(1, "%" + query + "%");
			stmt.setString(2, "%" + query + "%");
			stmt.setString(3, "%" + query + "%");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				localresults.add(
						new UserInfo(rs.getString("username"), rs.getString("mobNumber"), rs.getString("fullname")));
			}
		}
		boolean exists;
		for (UserInfo r : results) {
			exists = false;
			for (UserInfo lr : localresults) {
				if (r.getName().equalsIgnoreCase(lr.getName()))
					exists = true;
			}
			if (!exists)
				localresults.add(r);

		}
		return localresults;
	}

}
