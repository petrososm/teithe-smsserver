package com.smsserver.resources;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.smsserver.configuration.ServicesOnLoad;
import com.smsserver.doa.Logs;
import com.smsserver.models.NocSite.SendSmsRequest;
import com.smsserver.models.NocSite.ServiceDescription;

@Path("/site")
public class NocSite {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void getDlr(SendSmsRequest dlrReq) {// methodos gia na pairnoyme
													// plirofories meta tin
													// apostoli

		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ServiceDescription> getServiceDescriptions() {
			return ServicesOnLoad.descriptions;
		
	}
	
	
	
}
