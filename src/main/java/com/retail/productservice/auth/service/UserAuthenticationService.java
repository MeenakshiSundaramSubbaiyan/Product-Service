package com.retail.productservice.auth.service;

import com.retail.productservice.auth.vo.User;

import java.util.Optional;

public interface UserAuthenticationService {
	
	 /**
	   * Logs in with the given {@code username} and {@code password}.
	   *
	   * @param username
	   * @param password
	   * @return an {@link Optional} of a user when login succeeds
	   */
	  Optional<String> login(String username, String password);

	  /**
	   * Finds a user by its dao-key.
	   *
	   * @param token user dao key
	   * @return
	   */
	  Optional<User> findByToken(String token);

}
