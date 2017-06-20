package com.smsserver.services.models.site;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
@XmlRootElement
public class User implements Serializable {

	    private String username;
	    private String password;
	    private String token;
	    private String role;
	    

	    
		public User(String username, String token, String role) {
			super();
			this.username = username;
			this.token = token;
			this.role = role;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}

		public User(){}
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		@JsonProperty(access = Access.WRITE_ONLY)
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}

	    
	
}
