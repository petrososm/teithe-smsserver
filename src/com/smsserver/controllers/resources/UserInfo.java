package com.smsserver.controllers.resources;

public class UserInfo {
	private String name;
	private String mobile;
	private String fullname;
	public UserInfo(String name, String mobile,String fullname) {
		super();
		this.name = name;
		this.mobile = mobile;
		this.fullname=fullname;
	}
	public UserInfo(){}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	
}
