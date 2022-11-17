package com.sse.utilities.timers;

/**
 * Stop watch for processing when we are awaiting for Salesforce/HTML things to happen, or
 * within WaitUtilities.sleeps
 * @author atul
 *
 */
public class WaitingStopWatch extends AbstractStopWatch {

	/**
	 * Instantiates a new stop watch.
	 *
	 * @param controller the stop watch controller
	 */
	public WaitingStopWatch(StopWatchController controller) {
		super(controller);
	}

}
