package com.smsserver.auth.security;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.smsserver.auth.users.Role;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchScope;

public class Ldap {

	static String serviceUserDN = "";
	static String serviceUserPassword = "";
	static String ldapUrl = "ldap://195.251.240.232:389";
	static String identifyingAttribute = "uid";
	static String base = "ou=people,dc=teithe,dc=gr";

	public static String performAuthentication(String identifier, String password) throws Exception {

		// first create the service context
		DirContext serviceCtx = null;
		try {
			// use the service user to authenticate
			Properties serviceEnv = new Properties();
			serviceEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			serviceEnv.put(Context.PROVIDER_URL, ldapUrl);
			serviceEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			serviceEnv.put(Context.SECURITY_PRINCIPAL, serviceUserDN);
			serviceEnv.put(Context.SECURITY_CREDENTIALS, serviceUserPassword);
			serviceCtx = new InitialDirContext(serviceEnv);

			// we don't need all attributes, just let it get the identifying one
			String[] attributeFilter = { identifyingAttribute };
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(attributeFilter);
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// use a search filter to find only the user we want to authenticate
			String searchFilter = "(" + identifyingAttribute + "=" + identifier + ")";
			NamingEnumeration<SearchResult> results = serviceCtx.search(base, searchFilter, sc);

			if (results.hasMore()) {
				// get the users DN (distinguishedName) from the result
				SearchResult result = results.next();
				String distinguishedName = result.getNameInNamespace();
				System.out.println(distinguishedName);

				// attempt another authentication, now with the user
				Properties authEnv = new Properties();
				authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
				authEnv.put(Context.PROVIDER_URL, ldapUrl);
				authEnv.put(Context.SECURITY_PRINCIPAL, distinguishedName);
				authEnv.put(Context.SECURITY_CREDENTIALS, password);
				new InitialDirContext(authEnv);

				System.out.println("Authentication successful");

				// Find if user == staff or stud

				String[] split = distinguishedName.split(",");
				for (String s : split)
					for (Role r : Role.values())
						if (s.equalsIgnoreCase("ou=" + r.name()))
							return r.name();

				return "Unicorn";
			}
		} finally {
			if (serviceCtx != null) {
				serviceCtx.close();

			}
		}
		System.err.println("Authentication failed");
		throw new Exception();
	}

}
