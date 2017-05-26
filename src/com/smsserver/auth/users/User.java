package com.smsserver.auth.users;

import java.lang.annotation.Annotation;

public class User  {
	
	String username;
	String role;
	
	User(){}

	public User(String username, String role) {
		super();
		this.username = username;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	

	
	
}
