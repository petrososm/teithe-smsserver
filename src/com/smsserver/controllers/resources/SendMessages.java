package com.smsserver.controllers.resources;

import java.net.URISyntaxException;
import java.security.Principal;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.smsserver.controllers.filters.Secured;
import com.smsserver.controllers.models.site.SendSmsRequestDirect;
import com.smsserver.controllers.models.site.SendSmsRequestMoodle;
import com.smsserver.services.MobileTerminated;
import com.smsserver.services.auth.Role;

@Path("/send")
@Stateful
public class SendMessages {
	@Context
	SecurityContext securityContext;
	@EJB
	MobileTerminated mobileTerminated;
	

	
	@Path("/aimodosia")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
	//@Consumes(MediaType.TEXT_PLAIN)
	@Secured(Role.ADMIN)
    public Response sendAimodosia(String date) throws URISyntaxException {
    	try {
			return Response.ok(mobileTerminated.sendAimodosia(date)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
    }
	
	@Path("/moodle")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured({Role.STAFF,Role.ADMIN})
    public Response sendMoodle(SendSmsRequestMoodle req) throws URISyntaxException {
    	Principal principal = securityContext.getUserPrincipal();
    	req.setProfessor(principal.getName());

    	try {
			return Response.ok(mobileTerminated.sendMoodle(req)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

    }
	
	@Path("/direct")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured({Role.STAFF,Role.ADMIN})
    public Response sendDirect(SendSmsRequestDirect req) throws URISyntaxException {
    	Principal principal = securityContext.getUserPrincipal();
    	req.setSender (principal.getName());
    	
    	System.out.println(req);
    	try {
			
			return Response.ok(mobileTerminated.sendDirect(req)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

    }

}
