package com.csye6225.noteapp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.csye6225.noteapp.service.PasswordResetService;
import com.csye6225.noteapp.service.UserService;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

@RestController
public class PasswordResetController {

	private static final StatsDClient statsd = new NonBlockingStatsDClient("csye6225.webapp", "localhost", 8125);
	private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

	@Autowired
	PasswordResetService passwordResetService;

	@Autowired
	UserService userService;

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public ResponseEntity<Object> registerUser(@Valid @RequestBody Map<String, String> email) {

		logger.info("Resetting password for user");
		statsd.incrementCounter("POST /reset");
		HashMap<String, Object> entities = new HashMap<String, Object>();

		Map.Entry<String, String> entry = email.entrySet().iterator().next();
		// String key = entry.getKey();
		String value = entry.getValue();
		if (null != userService.findUser(value)) {
			return this.passwordResetService.sendResetEmail(value);
		} else {
			entities.put("Message", "User does not exist!!");

			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		}

	}

}
