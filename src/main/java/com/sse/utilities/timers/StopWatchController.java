package com.sse.utilities.timers;

/**
 * Stop watch controller interface
 * @author atul
 *
 */
public interface StopWatchController  {

	/**
	 * Initialises the controller - usually by configuring each Stop Watch.
	 */
	void initialise();

	/**
	 * Receives notification that a Watch has started
	 * @param watch the Stop Watch which has just started
	 */
	void startNotified(AbstractStopWatch watch);

	/**
	 * Receives notification that a Watch has stopped
	 * @param watch the Stop Watch which has just stopped
	 */
	void stopNotified(AbstractStopWatch watch);

	/**
	 * Shuts down the controller, and potentially do something with the results.
	 */
	void shutDown();
}
