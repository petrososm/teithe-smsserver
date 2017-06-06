package com.smsserver.gunetservices;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsserver.configuration.GetPropertyValues;
import com.smsserver.models.gunetapi.SendSmsModel;
import com.smsserver.models.gunetapi.SmsResponseModel;


public class GunetServices {


	public static SmsResponseModel testSend(SendSmsModel sendSms){
		System.out.println(sendSms);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       SmsResponseModel s= new SmsResponseModel();
       s.error="";
       return s;

	}
	
	public static SmsResponseModel sendSingleSms (SendSmsModel sendSms) {
		  try {
		        
				Client client = ClientHelper.IgnoreSSLClient();
			    WebTarget webTarget = client.target(GetPropertyValues.getProperties().getProperty("gunetUrl"));
			  
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
		        
		        client.close();
		        return smsResponse;
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		        return null;//prp na girnaei smsresponse 
		    }
	}
	

}
