package com.brianghig.resources.model;

/**
 * 
 * @author Brian
 *
 */
public class Paging {

	private Integer start;
	private Integer limit;
	
	/**
	 * Returns a Paging object populated with the validated start and limit arguments.
	 * Throws IllegalArgumentException if either argument is invalid.
	 * 
	 * @param start non-negative start value for the paging query 
	 * @param limit non-negative max results value for the paging query
	 * @return validated Paging object 
	 */
	public Paging( Integer start, Integer limit ) {
		
		if( start != null && start < 0 ) {
			throw new IllegalArgumentException("Start must be non-negative for pagination");
		}
		
		if( limit != null && limit < 0 ) {
			throw new IllegalArgumentException("Limit must be non-negative for pagination");
		}
		
		this.start = start;
		
		
		this.limit = limit;
	}

	public Integer getStart() {
		return start;
	}
	
	public Integer getLimit() {
		return limit;
	}
	
}
