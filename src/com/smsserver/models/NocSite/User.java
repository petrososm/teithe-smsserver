package com.smsserver.models.NocSite;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class User implements Serializable {

	    private String username;
	    private String password;
	    
		public User(String username, String password) {
			super();
			this.username = username;
			this.password = password;
		}
		
		public User(){}
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}

	    
	
}
