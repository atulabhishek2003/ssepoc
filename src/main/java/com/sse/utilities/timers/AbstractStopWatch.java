package com.sse.utilities.timers;

/**
 * Superclass of other "stop watch" classes - used to accumulate the elapsed time
 * of different types of activity within the automation suite.
 * @author mitchella3
 *
 */
public class AbstractStopWatch {

	protected StopWatchController controller;
	protected long elapsedTime = 0;
	protected long mostRecentStartTime = 0;
	protected long mostRecentStopTime = 0;
	protected int startCount = 0;
	protected boolean started = false;

	/**
	 * Constructor which take a reference to the "parent" StopWatch controller
	 * @param controller the StopWatch Controller.
	 */
	public AbstractStopWatch(StopWatchController controller) {
		this.controller = controller;
	}

	/**
	 * Start the stop watch.
	 * If it's already started, do nothing - otherwise notify the controller so that
	 * it can stop other watches.
	 */
	public void start() {
		if (started) return;
		started = true;
		startCount++;
		mostRecentStartTime = System.currentTimeMillis();
		controller.startNotified(this);
	}

	/**
	 * Stop the stop watch.
	 * If it's already stopped, do nothing - otherwise notify the controller so that
	 * it can start the default watch.
	 */
	public void stop() {
		if (!started) return;
		started = false;
		mostRecentStopTime = System.currentTimeMillis();
		elapsedTime += mostRecentStopTime - mostRecentStartTime;
		controller.stopNotified(this);
	}

	@SuppressWarnings("javadoc")
	public long getElapsedTime() {
		return elapsedTime;
	}

	@SuppressWarnings("javadoc")
	public int getStartCount() {
		return startCount;
	}

}
