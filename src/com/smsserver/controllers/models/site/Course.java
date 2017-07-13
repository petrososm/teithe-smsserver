package com.smsserver.controllers.models.site;

public class Course {
	
	private String courseName;
	private String courseId;
	
	public Course(String courseName, String courseId) {
		super();
		this.courseName = courseName;
		this.courseId = courseId;
	}
	public Course(){}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	
	

}
