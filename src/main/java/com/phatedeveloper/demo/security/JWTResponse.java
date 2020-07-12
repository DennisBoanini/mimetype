package com.phatedeveloper.demo.security;

import java.io.Serializable;

public class JWTResponse implements Serializable {

	private static final long serialVersionUID = -542456901532943638L;

	private final String jwtToken;
	private final String jwtTokenType;

	public JWTResponse(String jwtToken, String jwtTokenType) {
		this.jwtToken = jwtToken;
		this.jwtTokenType = jwtTokenType;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public String getJwtTokenType() {
		return jwtTokenType;
	}

}
