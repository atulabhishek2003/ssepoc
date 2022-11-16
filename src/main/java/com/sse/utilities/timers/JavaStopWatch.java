package com.sse.utilities.timers;

/**
 * Stop watch for "Java" processing.
 * This will be the default running watch when we are not obviously doing anything else
 * (e.g. waiting for Salesforce etc....)
 * @author mitchella3
 *
 */
public class JavaStopWatch extends AbstractStopWatch {

	/**
	 * Instantiates a new java stop watch.
	 *
	 * @param controller the controller
	 */
	public JavaStopWatch(StopWatchController controller) {
		super(controller);
	}

}
