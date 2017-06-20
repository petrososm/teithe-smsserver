package com.smsserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.smsserver.controllers.models.site.Course;

public class Moodle {
	
	public static ArrayList<Course> getCourses(String professor) throws SQLException{
		
		ArrayList<Course> courses=new ArrayList<Course>();
		String query="SELECT * FROM moodle.v_teacherspercourse where username=?";
		try (Connection conn =com.smsserver.dao.sqlconnections.MoodleConnections.getSqlConnections().getConnection();
				PreparedStatement stmt = conn
				.prepareStatement(query);){
			stmt.setString(1, professor);

			ResultSet rs=stmt.executeQuery();

			while(rs.next())
				courses.add(new Course(rs.getString("fullname"),rs.getString("shortname")));
		} 
		
		return courses;
	}
	
	public static ArrayList<String> getEnrolledStudents(String course) throws SQLException{
		ArrayList<String> usernames=new ArrayList<String>();
		String query="SELECT username FROM moodle.v_studentspercourse where CourseCode=?";
		try (Connection conn = com.smsserver.dao.sqlconnections.MoodleConnections.getSqlConnections().getConnection();
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
