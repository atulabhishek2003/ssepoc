package com.sse.utilities.timers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller of all stopwatches for L2I automation.
 * <p>Its main purpose is to ensure that a default stop watch is always active
 * by starting the default watch when another has stopped.
 * @author atul
 *
 */
public class L2IStopWatchController implements StopWatchController {

	/**
	 * StopWatch for when the suite is waiting.
	 */
	public static AbstractStopWatch waitWatch;
	/**
	 * Default StopWatch for when Java is doing something.
	 */
	public static AbstractStopWatch javaWatch;

	private static Logger log = LogManager.getLogger(L2IStopWatchController.class);
	@Override
	public void initialise() {
		javaWatch = new JavaStopWatch(this);
		waitWatch = new WaitingStopWatch(this);
		javaWatch.start();
	}

	@Override
	public void startNotified(AbstractStopWatch watch) {
		//Stop all watches which are not the one which has just started.
		if (!(watch instanceof JavaStopWatch)) {
			javaWatch.stop();
		}
		if (!(watch instanceof WaitingStopWatch) && waitWatch.started) {
			waitWatch.stop();
		}
	}

	@Override
	public void stopNotified(AbstractStopWatch watch) {
		if (!(watch instanceof JavaStopWatch) && !javaWatch.started) {
			javaWatch.start();
		}
	}

	@Override
	public void shutDown() {
		if (javaWatch == null) return; //i.e. controller wasn't initialised in any BeforeClass method
		javaWatch.stop();
		log.info("Java time : " + javaWatch.getElapsedTime()/1000.0 + " seconds (Count : " + javaWatch.getStartCount() + ")");
		log.info("Wait time : " + waitWatch.getElapsedTime()/1000.0 + " seconds (Count : " + waitWatch.getStartCount() + ")");
	}
}
