package com.retail.productservice.security.config;

import com.retail.productservice.security.handler.LoginFailureHandler;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.removeStart;

/**
 * Overriding Spring securities default Authentication processing filter to configure token based authentication
 */
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String BEARER = "Bearer";

	private final static UrlPathHelper urlPathHelper = new UrlPathHelper();

	LoginFailureHandler loginFailureHandler = new LoginFailureHandler();

	TokenAuthenticationFilter(final RequestMatcher requiresAuth) {
		super(requiresAuth);
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
		final String param = ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElse(request.getParameter("t"));
		final String token = ofNullable(param).map(value -> removeStart(value, BEARER)).map(String::trim)
				.orElseThrow(() -> new BadCredentialsException("Missing Authentication Token"));
		final Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
		return getAuthenticationManager().authenticate(auth);
	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                            final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											  AuthenticationException failed) throws IOException, ServletException {
		loginFailureHandler.onAuthenticationFailure(request,response,failed);

	}

}
