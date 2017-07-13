package com.smsserver.controllers.models.site;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
@XmlRootElement
public class User  {

	    private String username;
	    private String password;
	    private String role;
	    

	    
		public User(String username, String token, String role) {
			super();
			this.username = username;

			this.role = role;
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
		public String getPassword() {
			return "MOXy sucks";
		}
		public void setPassword(String password) {
			this.password = password;
		}

	    
	
}
