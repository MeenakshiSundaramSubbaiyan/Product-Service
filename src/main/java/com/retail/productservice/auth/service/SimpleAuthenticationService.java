package com.retail.productservice.auth.service;

import com.retail.productservice.auth.vo.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class to generate and validate the token for the request
 */
@Service
public class SimpleAuthenticationService implements UserAuthenticationService {

	public static Map<String, User> users = new HashMap<>();

	/**
	 * Method generates the bearer token for the given username and password
	 * @param username
	 * @param password
	 * @return
	 */
	@Override
	public Optional<String> login(String username, String password) {
		final String token = UUID.randomUUID().toString();
		final User user = User.builder().id(token).username(username).password(password).build();
		users.put(token, user);
		return Optional.of(token);
	}

	/**
	 * Check whether the token and username combination is present in the users map
	 * @param token user dao key
	 * @return
	 */
	@Override
	public Optional<User> findByToken(String token) {
		return Optional.ofNullable(users.get(token));
	}

}
