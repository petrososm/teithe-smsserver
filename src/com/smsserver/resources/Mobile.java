package com.smsserver.resources;

import java.security.Principal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import com.smsserver.auth.security.Secured;
import com.smsserver.doa.Mobiles;

@Path("/site/mobile")
public class Mobile {
	
	@Context
	SecurityContext securityContext;
	
	@GET
	@Secured
	public String getUserMobile(){
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
    	return Mobiles.getMobile(username);
	}

}
