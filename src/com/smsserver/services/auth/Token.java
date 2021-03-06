package com.smsserver.services.auth;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Token {

	public static String issueToken(User u) {
		String jwtToken = createJWT("ATEITH SMS SERVER", u.getUsername());
		return jwtToken;
	}

	public static String validateToken(String token) throws Exception {
		return parseJWT(token);
	}

	private static String createJWT(String issuer, String subject) {
		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		String signingKey = "secr3tkey";
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		long ttlMillis = 7200000;// 2 hours

		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setIssuedAt(now).setSubject(subject).setIssuer(issuer)
				.signWith(signatureAlgorithm, signingKey);

		// if it has been specified, let's add the expiration
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	private static String parseJWT(String jwt) {

		// This line will throw an exception if it is not a signed JWS (as
		// expected)
		Claims claims = Jwts.parser().setSigningKey("secr3tkey").parseClaimsJws(jwt).getBody();
		return claims.getSubject();

	}

}
