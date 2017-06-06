package com.smsserver.resources;

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

import com.smsserver.auth.security.Secured;
import com.smsserver.auth.users.Role;
import com.smsserver.doa.Logs;
import com.smsserver.doa.MobileOriginated;
import com.smsserver.models.gunetapi.DlrRequestModel;
import com.smsserver.models.gunetapi.SmsForwardModel;
import com.smsserver.models.gunetapi.SmsForwardResponseModel;

@Path("/")
public class Gunet {
	
	@Context
	SecurityContext securityContext;


	
	@Path("dlr/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void getDlr(DlrRequestModel dlrReq) {// methodos gia na pairnoyme
													// plirofories meta tin
													// apostoli

		Logs.logDlr(dlrReq);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SmsForwardResponseModel smsForward(SmsForwardModel smsRequest) {	

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

