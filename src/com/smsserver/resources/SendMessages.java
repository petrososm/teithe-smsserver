package com.smsserver.resources;

import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.smsserver.doa.MobileTerminated;
import com.smsserver.models.site.SendSmsRequest;

@Path("/send")
public class SendMessages {
	@Context
	SecurityContext securityContext;
	
	@Path("/aimodosia")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	//@Secured(Role.ADMIN)
    public Response sendAimodosia(String date) throws URISyntaxException {
    	try {
			return Response.ok(MobileTerminated.sendAimodosia(date)).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
    }
	
    @POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
   //@Secured(Role.STAFF)
    public Response sendMoodle(SendSmsRequest req) throws URISyntaxException {
    	//Principal principal = securityContext.getUserPrincipal();
    	//req.professor = principal.getName();
    	req.professor="petros";
    	try {
			
			return Response.ok(MobileTerminated.sendMoodle(req)).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

    }

}
