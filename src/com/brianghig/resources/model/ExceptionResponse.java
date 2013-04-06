package com.brianghig.resources.model;

public class ExceptionResponse {

	private Boolean success = Boolean.FALSE;
	private String message;
	
	public ExceptionResponse( Exception e ) {
		if( e == null ) {
			this.message = "Unknown error";
		}
		else {
			this.message = e.getMessage();
		}
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
