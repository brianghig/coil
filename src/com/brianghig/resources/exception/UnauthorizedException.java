package com.brianghig.resources.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class UnauthorizedException extends HttpStatusCodeException {
	
	private static final long serialVersionUID = 1L;

	private static HttpStatus status = HttpStatus.UNAUTHORIZED;
	
	public UnauthorizedException() {
		super(status);
	}
	
	public UnauthorizedException( String message ) {
		super(status, message);
	}
	
}
