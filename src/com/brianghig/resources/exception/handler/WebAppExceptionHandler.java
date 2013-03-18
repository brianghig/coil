package com.brianghig.resources.exception.handler;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Provider
@Component
public class WebAppExceptionHandler implements ExceptionMapper<Exception> {
	
	protected static final int DEFAULT_EXCEPTION_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR.value();
	
	/**
	 * Catch all exceptions at the Jersey Provider layer,
	 * and convert those into Response objects that match
	 * the view we expect for the client.
	 */
	@Override
	public Response toResponse(Exception ex) {
		
		int responseStatus = getStatusFromException( ex );
		
		Map<String, Object> model = getResponseModelFromException( ex );
		
		Response r = createResponseFromExceptionModel( model, responseStatus );
		
		return r;
	}

	/**
	 * Creates a model object that will be returned to the client
	 * with information extracted from the exception that was caught
	 * by this provider
	 * 
	 * @param ex
	 * @return
	 */
	protected Map<String, Object> getResponseModelFromException(Exception ex) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("success", Boolean.FALSE);
		model.put("message", ex.getMessage());
		
		return model;
		
	}

	/**
	 * Create a response object from the input exception model and http status,
	 * providing defaults if there are any errors
	 * 
	 * @param model
	 * @param httpStatus
	 * @return
	 */
	protected Response createResponseFromExceptionModel(Map<String, Object> model, int httpStatus) {
		
		Response r;
		
		try {
			// Attempt to create a response with the details of the exception
			r = Response
					//Set status based on exception
					.status( httpStatus )
					.entity( model ).build();
			
		} catch( Exception e ) {
			e.printStackTrace();
			// Something went horribly wrong, so fall back on something that hopefully won't
			r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		return r;
	}

	/**
	 * Interprets the exception that was thrown from the framework,
	 * and translates it to a respective HTTP Status code, either
	 * derived from the internals of the exception, or some other
	 * custom mapping
	 * 
	 * @param ex The exception thrown from the framework
	 * @return HTTP Status code representing the exception, defaulting to 500 if none are found
	 */
	protected int getStatusFromException(Exception ex) {
		
		// the status to return
		int responseStatus;
		
		/*
		 * If it is an exception with a known internal mapping
		 * to an HTTP status code, attempt to get that status code.
		 */
		if( ex instanceof HttpStatusCodeException ) {
			
			try {
				
				HttpStatusCodeException httpEx = (HttpStatusCodeException) ex;
				HttpStatus status = httpEx.getStatusCode();
				responseStatus = status.value();
				
			} catch( Exception e ) {
				
				/*
				 * TODO Log an error that we weren't able to retrieve
				 * or convert the http status code, but fall back on the
				 * 500 internal server error to still provide a valid response
				 */
				responseStatus = DEFAULT_EXCEPTION_HTTP_STATUS;
				
			}
			
		}
		// Add other custom Exception mappings ??
//		else if ( /* Other exception interpretations */ ) {
//			
//		}
		else {
			
			/*
			 * If we don't have a clear mapping from the exception
			 * to an HTTP status code, provide a valid default.
			 */
			responseStatus = DEFAULT_EXCEPTION_HTTP_STATUS;
			
		}
		
		return responseStatus;
		
	}
}
