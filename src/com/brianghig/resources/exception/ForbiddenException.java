package com.brianghig.resources.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ForbiddenException extends HttpStatusCodeException {

	private static final long serialVersionUID = 1L;

	private static HttpStatus status = HttpStatus.FORBIDDEN;
	
	public ForbiddenException() {
		super(status);
	}
	
	public ForbiddenException( String message ) {
		super(status, message);
	}
	
}
