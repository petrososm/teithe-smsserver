package com.smsserver.resources;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.smsserver.configuration.ServicesOnLoad;
import com.smsserver.models.site.ServiceDescription;
import com.smsserver.models.site.SmsTemplate;

@Path("/site")
public class Data {


	@Path("/moservices")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ServiceDescription> getServiceDescriptions() {
			return ServicesOnLoad.getDescriptions();
		
	}
	
	@Path("/mtservices")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<SmsTemplate> getMobileTerminatedServices() {
			return ServicesOnLoad.getSmsTemplates();
		
	}
	
	
	
}
