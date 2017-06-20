package com.smsserver.controllers.resources;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.smsserver.controllers.filters.Secured;
import com.smsserver.dao.Moodle;
import com.smsserver.services.Services;
import com.smsserver.services.auth.Role;
import com.smsserver.services.models.site.ServiceDescription;
import com.smsserver.services.models.site.SmsTemplate;

@Path("/site")
public class Data {

	@Context
	SecurityContext securityContext;

	@Path("/moservices")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ServiceDescription> getServiceDescriptions() {
			return Services.getDescriptions();
		
	}
	
	@Path("/mtservices/moodle")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({Role.STAFF,Role.ADMIN})
	public ArrayList<SmsTemplate> getMobileTerminatedServicesMoodle() {
			return Services.getSmsTemplatesMoodle();
		
	}
	
	@Path("/mtservices/direct")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({Role.STAFF,Role.ADMIN})
	public ArrayList<SmsTemplate> getMobileTerminatedServicesSingle() {
			return Services.getSmsTemplatesDirect();
		
	}
	
	@Path("/courses")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({Role.STAFF,Role.ADMIN})
	public Response getCoursesPerProfessor(){
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
		
		try {
			return Response.ok(Moodle.getCourses(username)).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	
}
