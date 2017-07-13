package com.smsserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import com.smsserver.services.models.mobileoriginated.MobileOriginatedService;

import javassist.NotFoundException;
@Stateless
public class Pithia {
	
    @Resource(lookup = "jdbc/pithia")
    DataSource pithia;
	
	
	public  String[] queryPithia(MobileOriginatedService service,String authenticator,String[] userParameters,int extra) throws Exception{
		String[] replacements=new String[service.numberOfReplacements];
		try (Connection conn = pithia.getConnection();
				PreparedStatement stmt=conn.prepareStatement(service.query);) {
			
			stmt.setString(1, authenticator);
			for(int i=1;i<service.queryParams;i++)//ksekinaei apo 1 giati mia parametros einai standard
				stmt.setString(i+1, userParameters[i-1+extra]);
			//-1 gia na paei sto 0 . extra an iparxei secondary
			System.out.println(service);
			System.out.println(authenticator);
			
			try (ResultSet rs = stmt.executeQuery();) {
				if (!rs.next()) {
					throw new NotFoundException("query");
				} else {
					replacements=new String[service.numberOfReplacements];
					for(int i=0;i<service.numberOfReplacements;i++)
						replacements[i]=rs.getString(i+1);
				}
			}
		} 
		return replacements;
	}

}
