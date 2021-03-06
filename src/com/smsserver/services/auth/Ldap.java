package com.smsserver.services.auth;

import java.text.Normalizer;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.smsserver.services.GetPropertyValues;

@Stateless
public class Ldap {

	private String serviceUserDN;
	private String serviceUserPassword;
	private String ldapUrl;
	private String identifyingAttribute;
	private String base;

	public String performAuthentication(User user) throws Exception {

		serviceUserDN = GetPropertyValues.getProperties().getProperty("serviceUserDN");
		serviceUserPassword = GetPropertyValues.getProperties().getProperty("serviceUserPassword");
		ldapUrl = GetPropertyValues.getProperties().getProperty("ldapUrl");
		identifyingAttribute = "uid";
		base = "ou=people,dc=teithe,dc=gr";
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
			String[] attributeFilter = { identifyingAttribute, "cn;lang-el" };
			SearchControls sc = new SearchControls();
			sc.setReturningAttributes(attributeFilter);
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

			// use a search filter to find only the user we want to authenticate
			String searchFilter = "(" + identifyingAttribute + "=" + user.getUsername() + ")";
			NamingEnumeration<SearchResult> results = serviceCtx.search(base, searchFilter, sc);

			if (results.hasMore()) {
				// get the users DN (distinguishedName) from the result
				SearchResult result = results.next();
				String distinguishedName = result.getNameInNamespace();
				Attributes attributes = result.getAttributes();
				Attribute attribute = attributes.get("cn;lang-el");
				if (attribute != null) {
					Object o = attribute.get();
					if (o != null) {
						String fullname = String.valueOf(o);
						String s = Normalizer.normalize(fullname, Normalizer.Form.NFD);
						s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
						user.setFullname(s.toUpperCase());
						System.out.println(s);
					}
				}

				 Properties authEnv = new Properties();
				 authEnv.put(Context.INITIAL_CONTEXT_FACTORY,
				 "com.sun.jndi.ldap.LdapCtxFactory");
				 authEnv.put(Context.PROVIDER_URL, ldapUrl);
				 authEnv.put(Context.SECURITY_PRINCIPAL, distinguishedName);
				 authEnv.put(Context.SECURITY_CREDENTIALS,
				 user.getRealPassword());
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
		System.out.println("Authentication failed");
		throw new Exception();
	}

}
