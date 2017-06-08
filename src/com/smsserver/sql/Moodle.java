package com.smsserver.sql;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Moodle {
	// https://avaldes.com/jax-rs-restful-web-services-with-jndi-datasource-for-mysql-in-tomcat/
	// isws prepei na boyn rithmiseis k se alla simeia toy tomcat.katw katw
	private static DataSource mySqlDataSource = null;

	static {
		Context initContext;

		try {// gia na fortwsei to connection pool tou sqlserver
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			mySqlDataSource = (DataSource) envContext.lookup("jdbc/moodle");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static DataSource getSqlConnections() {
		return mySqlDataSource;
	}


	


}