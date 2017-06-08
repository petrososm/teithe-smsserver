package com.smsserver.resources;

import java.security.Principal;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.smsserver.auth.security.Secured;
import com.smsserver.doa.MobileChanger;
import com.smsserver.doa.Discovery;

@Path("/site/mobile")
public class Mobile {
	
	@Context
	SecurityContext securityContext;
	
	@GET
	@Secured
	public Response getUserMobile(){
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
    	try {
			return Response.ok(Discovery.getMobile(username)).build();
		} catch (Exception e) {
			e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@Path("/sendConfirmation/{mobile}")
	@GET
	@Secured
	public void getVerification(@PathParam("mobile") String mobile){
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
    	MobileChanger.sendVerificationCode(username,mobile);
	}
	
	
	@PUT
	@Secured
	public Response updateMobile(String verification){
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
    	try {
			MobileChanger.changeMobileNumber(username, Integer.parseInt(verification));
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
            return Response.status(Response.Status.FORBIDDEN).build();
		}

	}

}
