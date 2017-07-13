package com.smsserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ejb.Stateless;
import com.smsserver.controllers.models.site.Course;

@Stateless
public class Moodle {
    @Resource(lookup = "jdbc/moodle")
    DataSource moodle;
    

   
	public ArrayList<Course> getCourses(String professor) throws SQLException{
		ArrayList<Course> courses=new ArrayList<Course>();
		String query="SELECT * FROM moodle.v_teacherspercourse where username=?";
		try (Connection conn =moodle.getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){
			stmt.setString(1, professor);

			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				courses.add(new Course(rs.getString("fullname"),rs.getString("shortname")));
			conn.close();
		} 
		
		return courses;
	}
	
	public ArrayList<String> getEnrolledStudents(String course) throws SQLException{
		ArrayList<String> usernames=new ArrayList<String>();
		String query="SELECT username FROM moodle.v_studentspercourse where CourseCode=?";
		try (Connection conn = moodle.getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){
			stmt.setString(1, course);

			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				usernames.add(rs.getString(1));
		} 
		
		return usernames;
		
		
		
	}

}
