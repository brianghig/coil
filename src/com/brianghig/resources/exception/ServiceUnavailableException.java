package com.brianghig.resources.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class ServiceUnavailableException extends HttpStatusCodeException {

	private static final long serialVersionUID = 1L;

	private static HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
	
	public ServiceUnavailableException() {
		super(status);
	}
	
	public ServiceUnavailableException( String message ) {
		super(status, message);
	}
	
}
