package com.smsserver.services.auth;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class User  {

	    private String username;
	    private String password;
	    private String role;
	    private String token;
	    private String fullname;
	    private int ldapLogin=0;
	    
	    

	    
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
		public String getRealPassword(){
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}


		public void setLdapLogin(int ldapLogin) {
			this.ldapLogin = ldapLogin;
		}

		public int getLdapLogin() {
			return ldapLogin;
		}

		public String getFullname() {
			return fullname;
		}

		public void setFullname(String fullname) {
			this.fullname = fullname;
		}
		

	    
	
}
