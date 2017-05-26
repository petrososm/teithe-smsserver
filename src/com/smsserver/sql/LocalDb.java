package com.smsserver.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.smsserver.models.gunetapi.DlrRequestModel;

public class LocalDb {
	// https://avaldes.com/jax-rs-restful-web-services-with-jndi-datasource-for-mysql-in-tomcat/
	// isws prepei na boyn rithmiseis k se alla simeia toy tomcat.katw katw
	private static DataSource mySqlDataSource = null;

	static {
		Context initContext;

		try {// gia na fortwsei to connection pool tou sqlserver
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			mySqlDataSource = (DataSource) envContext.lookup("jdbc/localDb");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static DataSource getSqlConnections() {
		return mySqlDataSource;
	}

	public static String test() {
		try {
			Connection conn = mySqlDataSource.getConnection();
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM DLRS");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		return null;

	}


}