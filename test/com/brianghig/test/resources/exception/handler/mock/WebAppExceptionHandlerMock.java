package com.brianghig.test.resources.exception.handler.mock;

import java.util.Map;

import javax.ws.rs.core.Response;

import com.brianghig.resources.exception.handler.WebAppExceptionHandler;

public class WebAppExceptionHandlerMock extends WebAppExceptionHandler {
	
	@Override
	public Response createResponseFromExceptionModel(Map<String, Object> model, int httpStatus) {
		return super.createResponseFromExceptionModel(model, httpStatus);
	}
	
	@Override
	public int getStatusFromException(Exception ex) {
		return super.getStatusFromException(ex);
	}
	
	@Override
	public Map<String, Object> getResponseModelFromException(Exception ex) {
		return super.getResponseModelFromException(ex);
	}
	
}
