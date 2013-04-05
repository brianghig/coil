package com.brianghig.test.resources.exception.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.springframework.web.client.HttpStatusCodeException;

import com.brianghig.resources.exception.ConflictException;
import com.brianghig.resources.exception.ForbiddenException;
import com.brianghig.resources.exception.ServiceUnavailableException;
import com.brianghig.resources.exception.UnauthorizedException;
import com.brianghig.test.resources.exception.handler.mock.WebAppExceptionHandlerMock;

public class WebAppExceptionHandlerStatusTest {

	protected WebAppExceptionHandlerMock exHandler = new WebAppExceptionHandlerMock();
	
	protected int[] validHttpStatuses = new int[]{
		200, 202, 300, 302, 400, 403, 500
	};
	
	protected Exception[] exceptions = new Exception[]{
		new UnauthorizedException(),
		new ForbiddenException(),
		new ConflictException(),
		new UnauthorizedException(),
		new ServiceUnavailableException(),
		new IllegalArgumentException(),
		new NullPointerException()
	};
	
	@Test
	public void testExceptionStatus() {
		
		Map<Integer, Exception> exceptionStatusMap = new HashMap<Integer, Exception>();
		
		// Known HttpStatus-mapped Exceptions
		exceptionStatusMap.put( 401, new UnauthorizedException() );
		exceptionStatusMap.put( 403, new ForbiddenException() );
		exceptionStatusMap.put( 409, new ConflictException() );
		exceptionStatusMap.put( 401, new UnauthorizedException() );
		exceptionStatusMap.put( 503, new ServiceUnavailableException() );
		
		// Couple of exceptions that may be thrown from the application
		exceptionStatusMap.put( 500, new IllegalArgumentException() );
		exceptionStatusMap.put( 500, new NullPointerException() );
		
		for( Entry<Integer, Exception> entry : exceptionStatusMap.entrySet() ) {
			
			Integer expectedStatus = entry.getKey();
			Exception e = entry.getValue();
			
			int actual = exHandler.getStatusFromException(e);
			
			assertEquals("Retrieved different status than expected", expectedStatus, Integer.valueOf(actual) );
			
		}
		
	}
	
	@Test
	public void testCreateResponseFromModel() throws Exception {
		
		for( int i=0; i < 50 ; i++ ) {
			
			Map<String, Object> model = new HashMap<String, Object>();
			
			String key01 = UUID.randomUUID().toString();
			String value01 = UUID.randomUUID().toString();
			model.put(key01, value01);
			
			String key02 = UUID.randomUUID().toString();
			String value02 = UUID.randomUUID().toString();
			model.put(key02, value02);
			
			int randomHttpStatusIndex = (int) (Math.random() * validHttpStatuses.length);
			int randomHttpStatus = validHttpStatuses[randomHttpStatusIndex];
			
			Response r = exHandler.createResponseFromExceptionModel(model, randomHttpStatus);
			
			assertNotNull("Expected non-null response", r);
			
			assertEquals("Expected HTTP Status to match", randomHttpStatus, r.getStatus());
			
			assertNotNull("Expected non-null response entity", r.getEntity());
			String entityString = r.getEntity().toString();
			
			assertTrue("Expected first key to be in response entity", entityString.contains(key01) ) ;
			assertTrue("Expected first value to be in response entity", entityString.contains(value01) ) ;
			
			assertTrue("Expected second key to be in response entity", entityString.contains(key02) ) ;
			assertTrue("Expected second value to be in response entity", entityString.contains(value02) ) ;
			
		}
		
	}
	
	@Test
	public void testExceptionModelCreation() throws Exception {
		
		Map<String, Exception> exceptionStatusMap = new HashMap<String, Exception>();
		
		// Known HttpStatus-mapped Exceptions
		List<Class<? extends Exception>> exceptionClasses = new ArrayList< Class<? extends Exception> >();
		exceptionClasses.add( ForbiddenException.class );
		exceptionClasses.add( ConflictException.class );
		exceptionClasses.add( UnauthorizedException.class );
		exceptionClasses.add( ServiceUnavailableException.class );
		exceptionClasses.add( IllegalArgumentException.class );
		exceptionClasses.add( NullPointerException.class );
		
		// Set up the exceptions with a bunch of random string messages
		for( Class<? extends Exception> clazz : exceptionClasses ) {
			
			String uniqueMessage = UUID.randomUUID().toString();
			
			exceptionStatusMap.put(
				uniqueMessage, 
				clazz.getConstructor(String.class).newInstance(uniqueMessage) );
			
		}		
		
		for( Entry<String, Exception> entry : exceptionStatusMap.entrySet() ) {
			
			String expectedMessage = entry.getKey();
			Exception expectedException = entry.getValue();
			
			Map<String, Object> model = exHandler.getResponseModelFromException(expectedException);
			
			assertNotNull("Expected non-null model from exception", model);
			
			assertTrue( "Expected to see 'success' attribute", model.containsKey("success") );
			assertTrue( "Expected to see 'message' attribute", model.containsKey("message") );
			
			Object msgResponse = model.get("message");
			assertTrue( "Expected message response to be a String", msgResponse instanceof String );
			
			String msgString = (String) msgResponse;
			assertTrue("Expected message string to match exception message", msgString.contains(expectedMessage));
			
		}
		
	}
	
	@Test
	public void testModelWithException() throws Exception {
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("someObject", new ObjectWithException() );
		
		Response r = exHandler.createResponseFromExceptionModel(model, 200);
		
		assertNotNull("Expected non-null response", r);
		
		assertEquals("Expected 500 status response", 500, r.getStatus());
	}
	
	
	@Test
	public void testFullExceptionConversion() throws Exception {
		
		Exception[] exs = new Exception[]{
			new IllegalArgumentException(UUID.randomUUID().toString()),
			new ConflictException(UUID.randomUUID().toString()),
			new ForbiddenException(UUID.randomUUID().toString()),
			new NullPointerException(UUID.randomUUID().toString()),
			new NoResultException(UUID.randomUUID().toString()),
			new IndexOutOfBoundsException(UUID.randomUUID().toString())
		};
		
		for( int i=0; i < exs.length; i++ ) {
			Exception ex = exs[i];
			String expectedMessage = ex.getMessage();
			
			int expectedStatus;
			try {
				expectedStatus = ((HttpStatusCodeException)ex).getStatusCode().value();
			} catch(Exception e) {
				expectedStatus = 500;
			}
			
			Response r = exHandler.toResponse(ex);
			
			assertNotNull("Expected non-null Response", r);
			
			assertEquals("Expected status code to match: " + expectedStatus, expectedStatus, r.getStatus());
			
			String responseText = r.getEntity().toString();
			JSONObject json = new JSONObject(responseText);
			
			// Make sure we've got the success=false and message attributes as appropriate
			assertTrue("Expected success attribute returned", json.has("success"));
			assertTrue("Expected message attribute returned", json.has("message"));
			
			assertFalse("Expected success to be false", json.getBoolean("success"));
			assertEquals("Expected message to match", expectedMessage, json.getString("message"));
		}
		
	}
}

class ObjectWithException {
	
	public String getSomeException() {
		throw new IllegalArgumentException("Some exception that's thrown during model creation");
	}
	
}
