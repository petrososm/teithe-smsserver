package com.smsserver.resources;

import java.net.URISyntaxException;
import java.security.Principal;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.smsserver.auth.security.Secured;
import com.smsserver.auth.users.Role;
import com.smsserver.doa.MobileTerminated;
import com.smsserver.models.site.SendSmsRequestDirect;
import com.smsserver.models.site.SendSmsRequestMoodle;

@Path("/send")
public class SendMessages {
	@Context
	SecurityContext securityContext;
	
	@Path("/aimodosia")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
	//@Consumes(MediaType.TEXT_PLAIN)
	@Secured(Role.ADMIN)
    public Response sendAimodosia(String date) throws URISyntaxException {
    	try {
			return Response.ok(MobileTerminated.sendAimodosia(date)).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
    }
	
	@Path("/moodle")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @Secured(Role.STAFF)
    public Response sendMoodle(SendSmsRequestMoodle req) throws URISyntaxException {
    	Principal principal = securityContext.getUserPrincipal();
    	req.professor = principal.getName();

    	try {
			
			return Response.ok(MobileTerminated.sendMoodle(req)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

    }
	
	@Path("/direct")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @Secured(Role.STAFF)
    public Response sendDirect(SendSmsRequestDirect req) throws URISyntaxException {
    	Principal principal = securityContext.getUserPrincipal();
    	req.sender = principal.getName();
    	
    	System.out.println(req);
    	try {
			
			return Response.ok(MobileTerminated.sendDirect(req)).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

    }

}
