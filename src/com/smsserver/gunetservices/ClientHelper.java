package com.smsserver.gunetservices;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;


public class ClientHelper
{
	 public static Client IgnoreSSLClient() throws Exception {
		    SSLContext sslcontext = SSLContext.getInstance("TLS");
		    sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
		        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
		        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
		        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }

		    }}, new java.security.SecureRandom());
		    return ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier((s1, s2) -> true).build();
		}
}