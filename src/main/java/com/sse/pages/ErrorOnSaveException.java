/**
 * 
 */
package com.sse.pages;

/**
 * A RuntimeException indicating that errors have occurred when trying to Save a Salesforce page.
 * @author mitchella3
 *
 */
public class ErrorOnSaveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public ErrorOnSaveException() {
		super();
	}

	/**
	 * Constructor with message. Invokes superclass constructor
	 * @param message detail of the Exception.
	 */
	public ErrorOnSaveException(String message) {
		super(message);
	}
}
