package com.sse.stepdefinitions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sse.pages.Pages;
import com.sse.utilities.ExceptionHandler;
import com.sse.utilities.configuration.PropertiesHolder;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;


/**
 * Step definitions related to the Home page.
 * 
 * @author atul
 */
public final class HomePageSteps {
	
	private static Logger log = LogManager.getLogger(HomePageSteps.class);

	/**
	 * Launches the appropriate Salesforce home page and logs in to Salesforce using
	 * the appropriate role.
	 * 
	 * @param role e.g. SalesUser
	 */
	@Given("I login to Salesforce as a {string}")
	public void iLoginToSalesforceAsA(String role) {
		log.info("Launching Salesforce website as a " + role);
		Pages.loginPage.goTo();
		Pages.loginPage.checkArrivalOnPage();
		Pages.loginPage.login(role);
		
	}

	
	
	/**
	 * Used for any scenario which we know will only work in a specific environment, to skip the remaining steps.
	 * <br>An example is the Viper scenarios which use a product configured in Staging with Fulfilment System of Viper
	 * but is not configured in Viper Demo. It is unlikely that the same product also exists in UAT/SIT etc.
	 * @param environmentToProceedWith the environment which must be being used for the test to continue.
	 */
	@Given("I skip feature unless environment is {string}")
	public void iSkipFeatureUnlessEnvironmentIs(String environmentToProceedWith) {
		String environment = PropertiesHolder.environmentProperties.getProperty("environment");
		if (environment.equalsIgnoreCase(environmentToProceedWith)) {
			log.info("Environment is " + environment + " , proceeding with scenario");
		}
		else {
			String exceptionMessage = "Environment is not " + environment + " so deliberately stopping this test";
			ExceptionHandler.handleException(exceptionMessage, new RuntimeException(exceptionMessage), this);
		}
	}	

}
