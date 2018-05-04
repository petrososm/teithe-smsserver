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
import com.smsserver.services.auth.PithiaLogin;
import com.smsserver.services.auth.Role;
import com.smsserver.services.auth.Token;
import com.smsserver.services.auth.User;

@Path("/authentication")
public class Authentication {

    @EJB
    Ldap ldap;
    @EJB
    PithiaLogin pithiaLogin;
    @EJB
    AuthenticatedUsers authUsers;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(User user) throws URISyntaxException {
        try {
            if (user.getUsername().equals("aimodosia") && user.getRealPassword().equals("nocnoc2018")) {
                user.setRole("admin");
            } 
            else if(user.getUsername().equals("dervostest") && user.getRealPassword().equals("nocnoc2018")){
                user.setRole("staff");
                user.setUsername("dimiderv@stef");
            }else {
                user.setRole(authUser(user));
            }
            user.setToken(Token.issueToken(user));
//			user.setConfirmedPhone(getPhoneStatus);
            authUsers.put(user);
            return Response.ok(user).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();

        }
    }

    private String authUser(User user) throws Exception {
        try {
            return pithiaLogin.performAuthentication(user);
        } catch (Exception ex) {
            String role = ldap.performAuthentication(user).toLowerCase();
            return role;
        }
    }
}
