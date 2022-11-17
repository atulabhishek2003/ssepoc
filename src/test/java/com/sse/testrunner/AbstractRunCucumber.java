package com.sse.testrunner;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.sse.sseGeneral.enterprise.ProfileChanger;
import com.sse.utilities.configuration.ConnectionUtilities;
import com.sse.utilities.Browser;
import com.sse.utilities.ExceptionHandler;
import com.sse.utilities.WaitUtilities;
import com.sse.utilities.configuration.TestConfiguration;
import com.sse.utilities.timers.L2IStopWatchController;
import com.sse.utilities.timers.StopWatchController;

/**
 * An abstract superclass of all test runners, used to hold the setup() and after() static methods.
 * @author atul
 *
 */
public class AbstractRunCucumber {
	private static Logger log = LogManager.getLogger(AbstractRunCucumber.class);
	protected static StopWatchController controller = new L2IStopWatchController();
	
	/**
	 * This method runs before any tests are invoked. It sets up the configuration of reports, browsers etc.
	 * @throws IOException if the configuration files could not be read successfully.
	 */
	@BeforeClass
	public static void setup() throws IOException {
		controller.initialise();
		TestConfiguration.initialise(true);
	}

	/**
	 * Checks for the overridden value of passed 'startAt' to determine whether to delay the start of the suite.
	 * To use this, the concrete runner should use code in its "@Before"-annotated method similar to :
	 * <pre>
		waitForStartTime(DateUtilities.parseDateTimeFromString("21/10/2019 16:20","dd/MM/yyyy HH:mm"));
	 * </pre>
	 *	or
	 * <pre>
		waitForStartTime(LocalDateTime.now().plusMinutes(42));
	 * </pre>
	 *
	 * If this method is not called or the value of startAt is null OR in the past,
	 * then the suite will continue immediately.
	 * @param startAt the LocalDateTime specifying when to start the suite.
	 */
	protected static void waitForStartTime(LocalDateTime startAt) {
		if (startAt == null) {
			log.info("No override of start time specified so tests are starting immediately.");
		}
		else {
			LocalDateTime now = LocalDateTime.now();
			Duration timeToGo = Duration.between(now, startAt);
			long numberOfSeconds = timeToGo.getSeconds();
			if (numberOfSeconds > 0) {
				log.warn("Start time overridden to " + startAt);
				log.warn("Waiting for " + numberOfSeconds + " seconds.");
				WaitUtilities.sleep(numberOfSeconds);
				log.info("Start time now reached. Rerunning profile changer, and beginning suite.");
				ProfileChanger.prepareForAutoRun();
			}
			else {
				log.info("Not waiting as startTime " + startAt + " is in the past.");
			}
		}
	}

	/**
	 * This class runs after the completion of the final test. It populates the reports and closes the browser driver.
	 */
	@AfterClass
	public static void after() {
		Browser.close();
		controller.shutDown();
		ProfileChanger.setSSO(true);
		ConnectionUtilities.logoutAllUsers();
		if (ExceptionHandler.displaySplash) ExceptionHandler.splash("COMPLETED", null);
	}

	void dummy() {} //to bypass PMD warning about utility classes etc....
	
}