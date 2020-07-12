package com.phatedeveloper.demo.controllers;

import com.phatedeveloper.demo.models.ApplicationUser;
import com.phatedeveloper.demo.models.UserSave;
import com.phatedeveloper.demo.security.JWTRequest;
import com.phatedeveloper.demo.security.JWTResponse;
import com.phatedeveloper.demo.security.JWTUserDetailsService;
import com.phatedeveloper.demo.security.JWTUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class JwtAuthenticationController {

	private final AuthenticationManager authenticationManager;
	private final JWTUtils jwtTokenUtil;
	private final JWTUserDetailsService userDetailsService;

	public JwtAuthenticationController(AuthenticationManager authenticationManager, JWTUtils jwtTokenUtil, JWTUserDetailsService userDetailsService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userDetailsService = userDetailsService;
	}

	@PostMapping("/token")
	public ResponseEntity<JWTResponse> createAuthenticationToken(@RequestBody JWTRequest authenticationRequest) {

		this.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails.getUsername(), new Date(), 18_000_000L);

		return ResponseEntity.ok(new JWTResponse(token, "Bearer"));
	}

	@PostMapping("/register")
	public ResponseEntity<ApplicationUser> register(@RequestBody UserSave userSave) throws URISyntaxException {
		final ApplicationUser applicationUser = this.userDetailsService.register(userSave);

		return ResponseEntity.created(new URI(String.valueOf(applicationUser.getId()))).body(applicationUser);
	}

	private void authenticate(String username, String password) {
		this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}
}
