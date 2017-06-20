package com.smsserver.controllers.resources;

import java.security.Principal;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.smsserver.controllers.filters.Secured;
import com.smsserver.controllers.models.gunetapi.DlrRequestModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardResponseModel;
import com.smsserver.services.Logs;
import com.smsserver.services.MobileOriginated;
import com.smsserver.services.auth.Role;

@Path("/")
public class Gunet {
	
	@Context
	SecurityContext securityContext;


	
	@Path("dlr/")
	@POST
	public void getDlr(DlrRequestModel dlrReq) {// methodos gia na pairnoyme
													// plirofories meta tin
													// apostoli
		System.out.println(dlrReq);
		Logs.logDlr(dlrReq);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SmsForwardResponseModel smsForward(SmsForwardModel smsRequest) {	
		
		System.out.println(smsRequest);
		SmsForwardResponseModel smsResponse=MobileOriginated.reply(smsRequest);
		//smsResponse=Logic.epistrofi response
		//isws prepei na epistrefei builder
		return smsResponse;
	}
	
	

	@Secured({Role.STAFF})
	@Path("test")
	@GET
	@Produces("text/plain")
	public String testService(@PathParam("input") String input){
    	Principal principal = securityContext.getUserPrincipal();
    	String username = principal.getName();

    	return username;
	}

}

