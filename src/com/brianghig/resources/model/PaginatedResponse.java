package com.brianghig.resources.model;

import java.util.List;

public class PaginatedResponse<T> {

	private boolean success;
	private long count;
	private List<T> rows;
	
	public PaginatedResponse(boolean success, List<T> rows, long count) {
		this.success = success;
		this.rows = rows;
		this.count = count;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public long getCount() {
		return count;
	}
	
	public List<T> getRows() {
		return rows;
	}
	
}
