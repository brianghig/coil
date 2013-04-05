package com.brianghig.test.resources;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.brianghig.resources.model.Paging;

public class ResourceModelTest {
	
//	@Test
//	public void testPagingCreationNullStart() {
//		Paging paging = new Paging(null, 0);
//		assertNotNull("Expected non-null paging object returned with null start", paging);
//	}
//	
//	@Test
//	public void testPagingCreationNullLimit() {
//		Paging paging = new Paging(0, null);
//		assertNotNull("Unexpected Paging object returned from parameters with null limit", paging);
//		validatePagingParameters( paging, 0, null );
//	}
//	
//	@Test
//	public void testPagingCreationBothNull() {
//		Paging paging = new Paging(null, null);
//		assertNotNull("Unexpected Paging object returned from both null parameters", paging);
//		validatePagingParameters( paging, null, null );
//	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPagingCreationNegativeStart() {
		new Paging(-5, 10);
		fail("Expected exception from negative start");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPagingCreationNegativeLimit() {
		new Paging(5, -1);
		fail("Expected exception from negative limit");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPagingCreationBothNegative() {
		new Paging(-1, -1);
		fail("Expected exception from negative start and limit");
	}
	
	@Test
	public void testPagingCreationValid() {
		
		int[][] validCombinations = new int[][]{
			{0, 10},	// zero start, non-zero limit
			{5, 20},	// non-zero start and limit
			{0, 0},		// both are zero
			{20, 5},	// non-zero start and limit with start > limit
			{5, 0},		// non-zero start, zero limit
			{500000, 600000},	// larger numbers - only supporting Integer max value
			{8, 8}		// same, non-zero value
		};
		
		for( int i=0; i < validCombinations.length ; i++ ) {
			
			int[] combo = validCombinations[i];
			
			// Extract each of the start and limit tests from the valid combinations
			int testStart = combo[0];
			int testLimit = combo[1];
			
			Paging paging = new Paging(testStart, testLimit);
			validatePagingParameters(paging, testStart, testLimit);
			
		}
		
	}
	
	/**
	 * Helper method to execute assertions over Paging objects constructed
	 * from various inputs
	 * 
	 * @param pagingObject
	 * @param start
	 * @param limit
	 */
	protected void validatePagingParameters(Paging pagingObject, int start, int limit) {
		assertNotNull("Expected non-null paging object", pagingObject);
		assertTrue("Expected start values to match", start == pagingObject.getStart() );
		assertTrue("Expected limit values to match", limit == pagingObject.getLimit() );
	}
	
}
