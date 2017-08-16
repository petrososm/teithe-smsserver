package com.smsserver.services.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

@Stateless
public class PithiaLogin {
	@Resource(lookup = "jdbc/pithia")
	DataSource pithia;

	public String performAuthentication(User user) throws Exception{
		
		try (Connection conn = pithia.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement("SELECT [eUniCentral].[dbo].[f_Login] (?,?)");) {
			
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getRealPassword());
			ResultSet rs=stmt.executeQuery();
			if(rs.next()){
				String[] result = rs.getString(1).split(",");
				if(result[1].equals("-"))
					throw new Exception("Pithia Authentication failed");
				else if(result[1].equals("Staff")){
					user.setFullname(result[0]);
					return "staff";
				}else{
					user.setFullname(result[0]);
					return "stud";	
				}
			}	
		}
		throw new Exception("Pithia Authentication failed");
		}

}


