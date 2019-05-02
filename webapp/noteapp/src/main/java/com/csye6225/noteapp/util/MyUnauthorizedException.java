package com.csye6225.noteapp.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class MyUnauthorizedException extends RuntimeException {
	public MyUnauthorizedException(String message) {
		super(message);
	}

	public MyUnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

}
