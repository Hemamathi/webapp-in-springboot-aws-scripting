package com.csye6225.noteapp.controller;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.csye6225.noteapp.model.UserEntity;
import com.csye6225.noteapp.service.UserService;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private static final StatsDClient statsd = new NonBlockingStatsDClient("csye6225.webapp", "localhost", 8125);

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listUser() {
		log.info("Inside get / mapping");
		statsd.incrementCounter("get /");
		HashMap<String, Object> entities = new HashMap<String, Object>();
		entities.put("Status", "Authenticated");
		entities.put("Date", new Date());

		return ResponseEntity.ok(entities);
	}

	/* to save an user */
	@PostMapping("/user/register")
	public ResponseEntity<Object> createUser(@Valid @RequestBody UserEntity user) {

		log.info("Inside post /user/register mapping");
		statsd.incrementCounter("post /user/register");
		HashMap<String, Object> entities = new HashMap<String, Object>();
		UserEntity ent = null;
		if (validateEmail(user.getEmail()) && validatePassword(user.getPassword())) {
			if (null == userService.findUser(user.getEmail())) {
				ent = userService.save(user);
				String auth = user.getEmail() + ":" + user.getPassword();
				byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
				entities.put("User registered Basic token:", new String(encodedAuth));
				return new ResponseEntity<>(entities, HttpStatus.CREATED);
			} else {
				entities.put("User already exists", "User already exists !!");

				return new ResponseEntity<>(entities, HttpStatus.FORBIDDEN);
			}
		} else {
			entities.put("Validation Error",
					"Please input correct email id and/or a strong Password with atleast 8 chars including 1 number and a special char");

			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
			// return ResponseEntity. .body("Please enter proper email_id and Password");
		}
	}

	public Boolean validatePassword(String password) {
		if (password != null || (!password.equalsIgnoreCase(""))) {
			String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
			return (password.matches(pattern));
		} else {
			return Boolean.FALSE;
		}

	}

	public Boolean validateEmail(String email) {
		if (email != null || (!email.equalsIgnoreCase(""))) {
			String emailvalidator = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
					+ "A-Z]{2,7}$";

			return email.matches(emailvalidator);
		} else {
			return Boolean.FALSE;
		}

	}

}