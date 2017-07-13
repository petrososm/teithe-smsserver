package com.smsserver.controllers.resources;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.smsserver.controllers.models.gunetapi.DlrRequestModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardResponseModel;
import com.smsserver.services.Logs;
import com.smsserver.services.MobileOriginated;

@Path("/")
public class Gunet {
	

	@EJB
	MobileOriginated mobileOriginated;
	@EJB
	Logs logs;

	
	@Path("dlr/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void getDlr(DlrRequestModel dlrReq) {// methodos gia na pairnoyme
													// plirofories meta tin
													// apostoli
		System.out.println(dlrReq);
		logs.logDlr(dlrReq);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public SmsForwardResponseModel smsForward(SmsForwardModel smsRequest) {	
		
		System.out.println(smsRequest);
		SmsForwardResponseModel smsResponse=mobileOriginated.reply(smsRequest);
		return smsResponse;
	}
	

	



}

