package com.smsserver.resources;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.security.PermitAll;
import javax.servlet.http.Cookie;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.smsserver.auth.security.Ldap;
import com.smsserver.auth.security.Secured;
import com.smsserver.auth.security.Token;
import com.smsserver.auth.users.Role;
import com.smsserver.models.site.User;



@Path("/authentication")
public class Authentication {


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(User u) throws URISyntaxException {

        try {
            //String access=Ldap.performAuthentication(u.getUsername(), u.getPassword());
        	String access="stud";
            String token = Token.issueToken(u.getUsername(),access);
            
            User returnUser=new User(u.getUsername(),token,access.toLowerCase());
            return Response.ok(returnUser).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
           

        }      
    }
    
    @Path("/validate")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Secured
    public Response validateUser(){
    	return Response.ok().build();
    }
    
    @Path("/validateStaff")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Secured(Role.STAFF)
    public Response validateStaff(){
    	return Response.ok().build();
    }
    
    @Path("/validateAimodosia")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Secured(Role.STUD)
    public Response validateAimodosia(){
    	return Response.ok().build();
    }
    
    




}
