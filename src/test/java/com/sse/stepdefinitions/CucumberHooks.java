package com.sse.stepdefinitions;

import static com.sse.utilities.ExceptionHandler.RUN_SUMMARY;

import java.io.File;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sse.pages.Pages;
import com.sse.utilities.Browser;
import com.sse.utilities.DateUtilities;
import com.sse.utilities.configuration.TestConfiguration;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
/**
 * A 'step definition' class purely to hold the Cucumber hooks.
 * @author Atul
 *
 */
public final class CucumberHooks {
	private static Logger log = LogManager.getLogger(CucumberHooks.class);

	/**
	 * This method runs before processing every scenario/example.
	 * @param scenario the current Scenario
	 */
	@Before
	public void setUp(Scenario scenario) {

		checkForLock();

		Pages.initialise(scenario);
		log.info("------------------------------------------------------------");
		log.info("Starting - " +
				Pages.scenarioData.getScenarioName());
		log.info("  (Feature : " +
				Pages.scenarioData.getScenarioFeatureTag() +
				" ,Scenario : " +
				Pages.scenarioData.getScenarioScenarioTag() + ") ");
		log.info("------------------------------------------------------------");

		RUN_SUMMARY.info("------------------------------------------------------------");
		RUN_SUMMARY.info(Pages.scenarioData.getScenarioFeatureTag() + ":" +
				Pages.scenarioData.getScenarioScenarioTag() + " started");
	}

	/**
	 * This method runs at the end of processing every scenario/example.
	 * <p>Provided a browser has been initialised, it attempts to grab a screenshot if the scenario has not passed.
	 * <p>It also logs details of the scenario.
	 * @param scenario the finished Scenario
	 */
	@After
	public void tearDown(Scenario scenario) {
		if (Browser.driver!=null && Status.PASSED != scenario.getStatus()) {
			try {
				String path =
						TestConfiguration.USER_DIR +
						"/target/screenshot/" +
						Pages.scenarioData.getScenarioFeatureTag() + "_" +
						replaceDodgyFilenameCharacters(scenario.getName()) +
						DateUtilities.formatLocalDateTime(LocalDateTime.now(), "_yyyyMMdd-HHmm") +
						"_Error.png";
				log.info("Attempting to capture a screenshot at location " + path);
				Browser.screenShot(path);

				/* Note, the line at the bottom of this comment block has been used in other projects....
				 * This does create an image in /reports/ but appears not to honour the ExtendedCucumberOptions
				 * which specify :
				 * outputFolder = "reports/DATE(yyyy-MM-dd-HH-mm)",
				 *
				 * as the HTML source of (e.g.) /reports/2018-08-22-11-12/cucumber-results-test-results.html
				 * has an img src attribute pointing to the 'current' directory to locate the image, rather than
				 * the directory above.
				 *
				 * scenario.embed(((TakesScreenshot)Browser.driver).getScreenshotAs(OutputType.BYTES), "image/png");
				 */

//				Reporter.addScreenCaptureFromPath(path, "Error " + scenario.getName());
			} catch (Exception e) {
				log.error("Teardown error during screen capture", e);
			}
		}

		log.info("------------------------------------------------------------");
		log.info(scenario.getName() + 
				"  (Feature : " + 
				Pages.scenarioData.getScenarioFeatureTag() + 
				" ,Scenario : " + 
				Pages.scenarioData.getScenarioScenarioTag() + ")" + 
				" complete. Status - " + scenario.getStatus());
		log.info("------------------------------------------------------------");
		
		String summaryOutput = Pages.scenarioData.getScenarioFeatureTag() + ":" + 
				Pages.scenarioData.getScenarioScenarioTag() + 
				" Status - " + scenario.getStatus();
			if (Status.PASSED == scenario.getStatus()) {
			RUN_SUMMARY.info(summaryOutput);
		}
		else {
			RUN_SUMMARY.error(summaryOutput);
		}
		//RUN_SUMMARY.info("------------------------------------------------------------");
	}

	private String replaceDodgyFilenameCharacters(String fileName) {
		String newFileName = fileName.replace("/", "_").replace(".", "_").replace(":", "").replace("<", "_").replace(">", "_")
				.replace("\\", "_").replace("?", "").replace("\"", "").replace("|", "_").replace("*", "");
		//Could be a regex instead, but this will probably do for now.
		if (newFileName.length() > 40) newFileName = newFileName.substring(0,40);
		return newFileName;
	}

	/**
	 * Looks for a "LOCK" file. If it exists, the suite sleeps and polls until the lock is released.
	 * This can be used to "pause" a suite in between scenarios.
	 * <p>The file must be called LOCK and reside in the project root directory.
	 */
	private void checkForLock() {
		boolean isLockInvoked = false;
		File lockFile = new File(TestConfiguration.USER_DIR + File.separator + "LOCK");
		if (lockFile.exists()) {
			isLockInvoked = true;
			log.warn("LOCK file exists so suite is paused. Polling until the file is removed");
		}
		while (lockFile.exists()) {
			try {

				Thread.sleep(5000);
			} catch (@SuppressWarnings("unused") InterruptedException e) {}
		}
		if (isLockInvoked) { // which must be cleared for us to get here
			log.info("LOCK file no longer exists so suite is continuing");
		}
	}
}
