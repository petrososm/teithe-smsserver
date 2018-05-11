package com.smsserver.services.gunetservices;


import javax.ejb.Stateful;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsserver.controllers.models.gunetapi.SendSmsModel;
import com.smsserver.controllers.models.gunetapi.SmsResponseModel;
import com.smsserver.services.GetPropertyValues;

@Stateful
public class GunetServices {

    
    
	Client client;
        
        public SmsResponseModel testSend(SendSmsModel sendSms){
            System.out.println(sendSms);
            return new SmsResponseModel(sendSms.getServiceId(),"","");
        }
	public SmsResponseModel sendSingleSms (SendSmsModel sendSms) {
		  try {
		        
			client = TrustAllClient.IgnoreSSLClient();
			WebTarget webTarget = client.target(GetPropertyValues.getProperties().getProperty("gunetUrl"));
			  
		        String json= new ObjectMapper().writeValueAsString(sendSms);
		        // POST method
		        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON)
                                .post(Entity.entity(json, MediaType.APPLICATION_JSON), Response.class);
		        // check response status code
		        if (response.getStatus() != 200) {
		            throw new RuntimeException("Failed : HTTP error code : "
		                    + response.getStatus());
		        }
		        // display response
		        SmsResponseModel smsResponse = response.readEntity(SmsResponseModel.class);
		        client.close();
		        return smsResponse;
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		        return null;
		    }
	}
	

}
