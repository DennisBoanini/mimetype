package com.phatedeveloper.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JWTUtils implements Serializable {

	private static final long serialVersionUID = 5461442512843196704L;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.token.issuer}")
	private String issuer;

	//retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		Jws<Claims> claimsJws = parseAccessToken(token, this.issuer);
		return claimsResolver.apply(claimsJws.getBody());
	}

	//for retrieving any information from token we will need the secret key
	private Jws<Claims> parseAccessToken(String accessToken, String issuerId) {
		return Jwts.parser().setSigningKey(this.secret).requireIssuer(issuerId)
				.parseClaimsJws(accessToken);
	}

	//check if the token is expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	//generate token for user
	public String generateToken(String username, Date creationDate, long expirationTime) {
		Map<String, Object> claims = new HashMap<>();

		return doGenerateToken(claims, username, creationDate, expirationTime);
	}

	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, String subject, Date creationDate, long expirationTime) {

		return Jwts.builder()
				.setId(UUID.randomUUID().toString())
				.setClaims(claims)
				.setIssuer(this.issuer)
				.setSubject(subject)
				.setIssuedAt(creationDate)
				.setExpiration(new Date(creationDate.getTime() + expirationTime))
				.signWith(SignatureAlgorithm.HS512, this.secret).compact();
	}

	//validate token
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
