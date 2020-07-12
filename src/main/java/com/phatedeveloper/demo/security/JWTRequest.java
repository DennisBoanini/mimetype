package com.phatedeveloper.demo.security;

import java.io.Serializable;

public class JWTRequest implements Serializable {

	private static final long serialVersionUID = -4618547175652667051L;

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
