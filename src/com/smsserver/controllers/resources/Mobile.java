package com.smsserver.controllers.resources;

import java.security.Principal;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.smsserver.controllers.filters.Secured;
import com.smsserver.dao.Discovery;
import com.smsserver.services.MobileChanger;

@Path("/site/mobile")
public class Mobile {
	
	@Context
	SecurityContext securityContext;
	@EJB
	Discovery discovery;
	@EJB
	MobileChanger mobChanger;

	
	@GET
	@Secured
	public Response getUserMobile(){
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
    	try {
			return Response.ok(discovery.getMobile(username)).build();
		} catch (Exception e) {
			e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@Path("/sendConfirmation/{mobile}")
	@GET
	@Secured
	public Response getVerification(@PathParam("mobile") String mobile){
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
    	mobChanger.sendVerificationCode(username,mobile);
    	return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	
	@PUT
	@Secured
	public Response updateMobile(String verification){
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
    	try {
			mobChanger.changeMobileNumber(username, Integer.parseInt(verification));
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
		}

	}

}
