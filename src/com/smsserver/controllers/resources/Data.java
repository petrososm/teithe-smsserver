package com.smsserver.controllers.resources;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.smsserver.controllers.filters.Secured;
import com.smsserver.controllers.models.site.Course;
import com.smsserver.controllers.models.site.ServiceDescription;
import com.smsserver.controllers.models.site.SmsTemplate;
import com.smsserver.dao.Discovery;
import com.smsserver.dao.Courses;
import com.smsserver.services.Services;
import com.smsserver.services.auth.AuthenticatedUsers;
import com.smsserver.services.auth.Role;

@Path("/site")
public class Data {

	@Context
	SecurityContext securityContext;
	@EJB
	Services services;
	@EJB
	Courses moodle;
	@EJB
	Discovery discovery;
	@EJB
	AuthenticatedUsers authUsers;
	
	@Path("/moservices")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ServiceDescription> getServiceDescriptions() {
//			System.out.println(services.getDescriptions());
			return services.getDescriptions();
		
	}
	
	@Path("/mtservices/moodle")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({Role.STAFF,Role.ADMIN})
	public ArrayList<SmsTemplate> getMobileTerminatedServicesMoodle() {
			return services.getSmsTemplatesMoodle();
		
	}
	
	@Path("/mtservices/direct")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({Role.STAFF,Role.ADMIN})
	public ArrayList<SmsTemplate> getMobileTerminatedServicesSingle() {
			return services.getSmsTemplatesDirect();
		
	}
	
	@Path("/courses")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Secured({Role.STAFF,Role.ADMIN})
	public List<Course> getCoursesPerProfessor() throws Exception{
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();
		return moodle.getCoursesPithia(username);
	}
	
	@Path("/users/{search}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<UserInfo> getUsers(@PathParam("search") String search) throws Exception{
		return discovery.findUser(search);

	}
	
	
	
}
