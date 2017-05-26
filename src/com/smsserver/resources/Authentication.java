package com.smsserver.resources;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smsserver.auth.security.Ldap;
import com.smsserver.auth.security.Token;
import com.smsserver.models.NocSite.User;


@PermitAll
@Path("/authentication")
public class Authentication {


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(User u) {

        try {
        	System.out.println("Ok");
            // Authenticate the user using the credentials provided
            String access=Ldap.performAuthentication(u.getUsername(), u.getPassword());
            
            String token = Token.issueToken(u.getUsername(),access);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
        	e.printStackTrace();
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }




}
