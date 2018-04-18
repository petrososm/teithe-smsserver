package com.smsserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ejb.Stateless;
import com.smsserver.controllers.models.site.Course;


@Stateless
public class Courses {
    @Resource(lookup = "jdbc/pithia")
    DataSource pithia;
    

   
	public ArrayList<Course> getCoursesPithia(String professor) throws SQLException{
		System.out.println(professor);
		ArrayList<Course> courses=new ArrayList<Course>();
		String query="SELECT  distinct classtitle,altkey "
				+ "FROM [eUniCentral].[dbo].[v_SMS_ClassTeachers] "
				+ "where teacherusername=? and classyear between ?-1 and ?";
		try (Connection conn =pithia.getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){
			
			int year = Calendar.getInstance().get(Calendar.YEAR);
			stmt.setString(1, professor);
			stmt.setInt(2, year);
			stmt.setInt(3, year);

			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				courses.add(new Course(rs.getString("classtitle"),rs.getString("altkey")));
			conn.close();
		} 
		
		return courses;
	}
	
//	public ArrayList<Course> getCoursesLdap(String professorFullName) throws Exception{
//		
//		String[] name = professorFullName.split("\\s+");
//		String pithiaName = null;
//		ArrayList<String> firstnames=new ArrayList<String>();
//		try (Connection conn =pithia.getConnection();
//				PreparedStatement stmt=conn.prepareStatement("SELECT  distinct teacherfirst "
//						+ "FROM [eUniCentral].[dbo].[v_SMS_ClassTeachers]  where teacherlast=? "
//						+ "and classyear between ?-1 and ?");
//						PreparedStatement stmt2=conn.prepareStatement("SELECT  distinct teacherusername "
//								+ "FROM [eUniCentral].[dbo].[v_SMS_ClassTeachers]  where teacherlast=? "
//								+ "and teacherfirst = ? and classyear between ?-1 and ?")){
//			
//			int year = Calendar.getInstance().get(Calendar.YEAR);
//			stmt.setString(1, name[0]);
//			stmt.setInt(2, year);
//			stmt.setInt(3, year);
//
//			ResultSet rs=stmt.executeQuery();
//			while(rs.next())
//				firstnames.add(rs.getString("teacherfirst"));
//			
//			String fname=maxSimilarity(firstnames,name[1]);
//			
//			stmt2.setString(1, name[0]);
//			stmt2.setString(2, fname);
//			stmt2.setInt(3, year);
//			stmt2.setInt(4, year);
//			rs=stmt2.executeQuery();
//			if(rs.next())
//				pithiaName=rs.getString("teacherusername");
//				
//		} 
//		
//		return getCoursesPithia(pithiaName);
//	}
//
//	private String maxSimilarity(ArrayList<String> firstnames, String userFirstName) throws Exception {
//		String mostSimilar=null;
//		double maxPerc=0;
//        JaroWinkler jw = new JaroWinkler();
//
//		for(String fname:firstnames){
//			double similarity=jw.similarity(fname,userFirstName);
//			if(similarity>maxPerc){
//				maxPerc=similarity;
//				mostSimilar=fname;
//			}		
//		}
//		if(maxPerc>0.75)
//			return mostSimilar;
//		else
//			throw new Exception();
//		
//	}
	


}
