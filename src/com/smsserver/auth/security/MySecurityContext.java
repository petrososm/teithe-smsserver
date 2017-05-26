package com.smsserver.auth.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class MySecurityContext implements SecurityContext {

    private final String username;

    public MySecurityContext(String username) {
       this.username=username;
    }

	
    @Override
    public Principal getUserPrincipal() {
        return new Principal() {
            @Override
            public String getName() {
                return username;
            }
        };
    }

    @Override
    public boolean isUserInRole(String role) {
    	return true;
    }

    @Override
    public boolean isSecure() { return true; }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }


}
