package com.smsserver.resources;

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

import com.smsserver.auth.security.Secured;
import com.smsserver.auth.users.Role;
import com.smsserver.configuration.ServicesOnLoad;
import com.smsserver.doa.MoodleDoa;
import com.smsserver.models.site.ServiceDescription;
import com.smsserver.models.site.SmsTemplate;

@Path("/site")
public class Data {

	@Context
	SecurityContext securityContext;

	@Path("/moservices")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ServiceDescription> getServiceDescriptions() {
			return ServicesOnLoad.getDescriptions();
		
	}
	
	@Path("/mtservices/moodle")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({Role.STAFF,Role.ADMIN})
	public ArrayList<SmsTemplate> getMobileTerminatedServicesMoodle() {
			return ServicesOnLoad.getSmsTemplatesMoodle();
		
	}
	
	@Path("/mtservices/direct")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({Role.STAFF,Role.ADMIN})
	public ArrayList<SmsTemplate> getMobileTerminatedServicesSingle() {
			return ServicesOnLoad.getSmsTemplatesDirect();
		
	}
	
	@Path("/courses")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({Role.STAFF,Role.ADMIN})
	public Response getCoursesPerProfessor(){
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
		
		try {
			return Response.ok(MoodleDoa.getCourses(username)).build();
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	
}
