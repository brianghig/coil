package com.brianghig.resources.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ConflictException extends HttpStatusCodeException {
	
	private static final long serialVersionUID = 1L;

	private static HttpStatus status = HttpStatus.CONFLICT;
	
	public ConflictException() {
		super(status);
	}
	
	public ConflictException( String message ) {
		super(status, message);
	}
	
}
