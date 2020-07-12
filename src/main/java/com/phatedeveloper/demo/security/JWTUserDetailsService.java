package com.phatedeveloper.demo.security;

import com.phatedeveloper.demo.models.ApplicationUser;
import com.phatedeveloper.demo.models.UserSave;
import com.phatedeveloper.demo.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@Service
public class JWTUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public JWTUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		ApplicationUser applicationUser = this.userRepository.findByUsername(username);

		if (applicationUser == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		return new User(applicationUser.getUsername(), applicationUser.getPassword(), new ArrayList<>());
	}

	public ApplicationUser register(UserSave userSave) {
		if (!StringUtils.hasText(userSave.getPassword()) || !StringUtils.hasText(userSave.getUsername())) {
			throw new IllegalArgumentException("Both username and password are mandatory");
		}
		var user = new ApplicationUser();
		user.setUsername(userSave.getUsername());
		user.setPassword(this.passwordEncoder.encode(userSave.getPassword()));

		return this.userRepository.save(user);
	}

}
