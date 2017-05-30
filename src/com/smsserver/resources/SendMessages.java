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
import com.smsserver.auth.security.Token;
import com.smsserver.auth.users.Role;
import com.smsserver.doa.MobileTerminated;
import com.smsserver.models.site.SendSmsRequest;
import com.smsserver.models.site.User;

@Path("/send")
public class SendMessages {
	@Context
	SecurityContext securityContext;
	
	@Path("/aimodosia")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	//@Secured(Role.AIMODOSIA)
    public Response sendAimodosia(String date) throws URISyntaxException {
    	try {
			MobileTerminated.sendAimodosia(date);
			return Response.ok().build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
    }
	
    @POST
    @Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
   //@Secured(Role.STAFF)
    public String sendMoodle(SendSmsRequest req) throws URISyntaxException {
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
    	return username;

    }

}
