package com.sse.salesforce;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Superclass of any Salesforce object.
 *
 * @author 
 */
public abstract class SalesforceObject implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * The Salesforce object id (15-character).
	 */
	private final String id;

	/**
	 * A description which we can use to help describe this instance of an object.
	 * An example might be "Sold To" and "Bill To" to help distinguish between 2
	 * Contacts.
	 */
	private String description;

	/**
	 * Constructor which takes the Salesforce id.
	 *
	 * @param id the 15-character Salesforce id.
	 */
	protected SalesforceObject(String id) {
		if (id == null) throw new RuntimeException("Attempting to set id to null for Salesforce Object : " + this.getClass());
		this.id = id;
	}

	// Setters and getters
	@SuppressWarnings("javadoc")
	public String getId() {
		return id;
	}

	@SuppressWarnings("javadoc")
	public String getDescription() {
		return description;
	}

	@SuppressWarnings("javadoc")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Uses the Apache commons reflective StringBuilder to generate a default
	 * toString() method.
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
