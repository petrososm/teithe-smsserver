package com.smsserver.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.smsserver.doa.Logs;
import com.smsserver.models.NocSite.SendSmsRequest;

@Path("/sendMass")
public class NocSite {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void getDlr(SendSmsRequest dlrReq) {// methodos gia na pairnoyme
													// plirofories meta tin
													// apostoli

		
	}
	
	
	
}
