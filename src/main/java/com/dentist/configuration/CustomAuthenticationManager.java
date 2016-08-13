package com.dentist.configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


/**
 * This implementation is required for making the Expression-Based Access
 * Control work. @EnableGlobalMethodSecurity requires a bean of
 * AuthenticationManager.Add @EnableGlobalMethodSecurity(prePostEnabled = true)
 * to WebMvcConfigurerAdapter so that @@PreAuthorize work.
 **/

public class CustomAuthenticationManager implements AuthenticationManager {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(),
				authentication.getAuthorities());
	}

}
