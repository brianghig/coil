package com.brianghig.resources.model;

/**
 * 
 * @author Brian
 *
 */
public class Paging {

	private int start;
	private int limit;
	
	/**
	 * Returns a Paging object populated with the validated start and limit arguments.
	 * Throws IllegalArgumentException if either argument is invalid.
	 * 
	 * @param start non-negative start value for the paging query 
	 * @param limit non-negative max results value for the paging query
	 * @return validated Paging object 
	 */
	public Paging( int start, int limit ) {
		
		if( start <= -1 ) {
			throw new IllegalArgumentException("Start must be non-negative for pagination");
		}
		
		if( limit < 1 ) {
			throw new IllegalArgumentException("Limit must be non-negative for pagination");
		}
		
		this.start = start;
		this.limit = limit;
	}

	public int getStart() {
		return start;
	}
	
	public int getLimit() {
		return limit;
	}
	
}
