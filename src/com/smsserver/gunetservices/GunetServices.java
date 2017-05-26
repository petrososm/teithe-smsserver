package com.smsserver.gunetservices;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsserver.models.gunetapi.*;


public class GunetServices {

	public static SmsResponseModel sendSms (SendSmsModel sendSms) {
		  try {
		        Client client = ClientHelper.IgnoreSSLClient();

		        WebTarget webTarget = client.target("https://sms-services.gunet.gr:9999/sendSMS");
		        
		        
		        String json= new ObjectMapper().writeValueAsString(sendSms);
		        System.out.println(json);

		        // POST method
		        Response response = webTarget.request().accept(MediaType.APPLICATION_JSON).post(Entity.entity(json, MediaType.APPLICATION_JSON), Response.class);


		        // check response status code
		        if (response.getStatus() != 200) {
		            throw new RuntimeException("Failed : HTTP error code : "
		                    + response.getStatus());
		        }

		        // display response
		        SmsResponseModel smsResponse = response.readEntity(SmsResponseModel.class);
		        System.out.println(smsResponse);
		        
		        return smsResponse;
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		        return null;//prp na girnaei smsresponse 
		    }


	}

}
