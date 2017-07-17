package com.smsserver.controllers.resources;

import java.net.URISyntaxException;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smsserver.controllers.filters.Secured;
import com.smsserver.services.auth.AuthenticatedUsers;
import com.smsserver.services.auth.Ldap;
import com.smsserver.services.auth.Role;
import com.smsserver.services.auth.Token;
import com.smsserver.services.auth.User;



@Path("/authentication")
public class Authentication {
	@EJB
	Ldap ldap;
	@EJB
	AuthenticatedUsers authUsers;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(User user) throws URISyntaxException {

        try {

        	user.setRole(ldap.performAuthentication(user).toLowerCase());          
            user.setToken(Token.issueToken(user)); 
            authUsers.put(user);
            return Response.ok(user).build();
            
        } catch (Exception e) {
        	e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).build();
           

        }      
    }
    
   
    




}
