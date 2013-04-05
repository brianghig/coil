package com.brianghig.base.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.brianghig.resources.model.Paging;

@Repository("daoBase")
public abstract class BaseDAOImpl {

	/**
	 * EntityManager tied into the persistence context of JPA,
	 * loaded via the application context and wired in via Spring.
	 * 
	 * Provide for DAOs in order to query available data source(s).
	 */
	@PersistenceContext
	protected EntityManager em;
	
	/**
	 * Based on the state of the paging parameter, will set the
	 * firstResult and maxRecords properties of the query object.
	 * 
	 * @param query if paging is non-null, object is modified to set start and limit
	 * @param paging optional paging criteria.
	 */
	protected void applyPagingToQuery( Query query, final Paging paging ) {
		
		/*
		 * If the query and paging information is provided,
		 * double-check to make sure that both
		 * start and limit parameters are valid, non-null integers before
		 * applying them to the query construct
		 */
		if( query != null && paging != null ) {
			
			// "start" maps to "firstResult"
			query.setFirstResult(paging.getStart());
			
			// "limit" maps to "maxResults", only if it is non-zero
			int limit = paging.getLimit();
			if( limit != 0 ) {
				query.setMaxResults(limit);
			}
			
		}
		
		/*
		 * Else, paging is either null or invalid, so do nothing,
		 * since it shouldn't be applied to the query
		 */
	}
}
