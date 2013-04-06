package com.brianghig.resources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;

import com.brianghig.resources.model.ExceptionResponse;


public abstract class AbstractResource {
	
	private static final int DEFAULT_HTTP_ERROR_STATUS = HttpStatus.INTERNAL_SERVER_ERROR.value();
	
	@ExceptionHandler(Throwable.class)
	public @ResponseBody ExceptionResponse handleException(final Exception e, final HttpServletRequest request, final HttpServletResponse response) {
		
		if( e instanceof HttpStatusCodeException ) {
			
			//Retrieve the HttpStatus from the targeted HTTP Exception
			HttpStatusCodeException httpException = (HttpStatusCodeException) e;
			HttpStatus httpStatus = httpException.getStatusCode();
			
			// Protect against bad code that returns a null status code
			if( httpStatus == null ) {
				response.setStatus(DEFAULT_HTTP_ERROR_STATUS);
			}
			else {
				// Use the proper HTTP Status from the specific exception
				response.setStatus(httpStatus.value());
			}
			
		}
		else {
			// Not a known HTTP Exception, so default to server error status
			response.setStatus(DEFAULT_HTTP_ERROR_STATUS);
		}
		
		// Construct the information to send back with the exception
		return new ExceptionResponse( e );
	}
	
}
