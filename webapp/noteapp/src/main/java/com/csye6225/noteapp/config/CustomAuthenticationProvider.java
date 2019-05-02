package com.csye6225.noteapp.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.csye6225.noteapp.Dao.UserDao;
import com.csye6225.noteapp.model.UserEntity;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	public CustomAuthenticationProvider() {
		super();
	}

	@Autowired
	UserDao userDao;

	@Autowired
	PasswordEncoder passwordEncoder;

	// API

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
		final String name = authentication.getName();
		final String password = authentication.getCredentials().toString();
		UserEntity userEntity = new UserEntity();
		userEntity = userDao.findUser(name);
		// System.err.println(userEntity.getPassword());
		// System.err.println(userEntity.getEmail());

		if (null != userEntity && name.equals(userEntity.getEmail())
				&& BCrypt.checkpw(password, userEntity.getPassword())) {
			final List<GrantedAuthority> grantedAuths = new ArrayList<>();
			grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
			final UserDetails principal = new User(name, password, grantedAuths);
			final Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
			return auth;
		} else {
			//usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("Bad Credentials",null);
			return usernamePasswordAuthenticationToken;
		}
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
