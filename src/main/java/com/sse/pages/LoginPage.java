package com.sse.pages;

import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.sse.utilities.Browser;
import com.sse.utilities.ExceptionHandler;
import com.sse.utilities.TextBoxUtilities;
import com.sse.utilities.WaitUtilities;
import com.sse.utilities.WebElementUtilities;
import com.sse.utilities.configuration.PropertiesHolder;
import com.sse.utilities.credentials.SecretHandler;

/**
 * Page class to handle the login page.
 * @author Atul
 */
public class LoginPage extends BasePage {

	private static Logger log = LogManager.getLogger(LoginPage.class);

	@FindBy(css="#username") private WebElement usernameTextBox;
	@FindBy(css="#password") private WebElement passwordTextBox;
	@FindBy(xpath="//button[text()=' Submit ']") private WebElement loginButton;
	private String user="Not Assigned";

	private String environment;

	/**
	 * For overnight runs, it is much more usual to have the
	 *  "Something has happened with your session" appearing later in the flow than
	 *  just the homepage. This can scupper tests regularly, so use this value in those
	 *  Run*.java classes to implement a sleep upon getting to the login page.
	 */
	public static long workAroundLoginSessionErrorsSeconds = 0;
	

	/**
	 * Navigate to the login page.
	 */
	public void goTo() {
		Properties currentEnvironmentProperties = PropertiesHolder.environmentProperties;
		if (currentEnvironmentProperties == null)
			throw new RuntimeException("Environment properties not found.");

		environment = currentEnvironmentProperties.getProperty("environment");
		log.info("Environment = " + environment);
		if (environment == null) throw new RuntimeException("Environment not specified in environment.properties file");

		String url = currentEnvironmentProperties.getProperty("URL." + environment);
		log.info("Salesforce URL to launch = " + url);
		if (url == null) throw new RuntimeException("URL not specified in environment.properties file");

		Browser.goTo(url);
	}

	/**
	 * Attempt to login to Salesforce and navigate to the home page.
	 * @param role refers to a specific Salesforce role.<br>
	 * The role must match an entry in environment.properties - e.g. for SalesUser in Systest environment, there must be
	 * two entries for Username_SalesUser.SYSTEST and Password_SalesUser.SYSTEST
	 * <p>NOTE - If there are spaces in the passed value of role (which improve readability in scenarios), these will be removed.
	 * This means that the step <code>And I login to Salesforce as a "Cirium SalesUser" </code> in the QA environment
	 * will require entries for
	 * Username_CiriumSalesUser.QA and Password_CiriumSalesUser.QA
	 */
	public void login(String role) {
		this.user=role;
		String roleSuffix = "_" + role.replace(" ", "") + ".";
		
		//else unchanged so that market contains the last market-specific user to login.
		/*
		 * This is because, as a market-specific user we might have stored different fields during the flow
		 * of a scenario. If we later login as sysadmin, we still might need to use different sets fields
		 * for each market, and we might need to know which market we were in at the time.
		 */

		String userNameProperty = "Username" + roleSuffix + environment;

		Properties currentEnvironmentProperties = PropertiesHolder.environmentProperties;
		String userName = currentEnvironmentProperties.getProperty(userNameProperty);
		if (userName == null) throw new RuntimeException(userNameProperty + " not specified in environment properties file");

		String passwordProperty = "Password" + roleSuffix + environment;
		String password = SecretHandler.decrypt(currentEnvironmentProperties.getProperty(passwordProperty));
		if (password == null) throw new RuntimeException(passwordProperty + " not specified in environment properties file");

		try {
	    	TextBoxUtilities.enterTextIntoTextbox(usernameTextBox, userName);
	    	WebElementUtilities.elementClickSafe(loginButton);
	    	// To do with SSO WaitUtilities.waitForElementVisible(passwordTextBox);
	    	TextBoxUtilities.enterTextIntoTextbox(passwordTextBox, password);
	    	
	    	WebElementUtilities.hitEscapeKey();
		}
		catch (Exception e) {
			ExceptionHandler.handleException("Could not login to Salesforce",e,this);
		}
	}

	/* (non-Javadoc)
	 * @see com.sse.pages.BasePage#checkArrivalOnPage()
	 */
	@Override
	public void checkArrivalOnPage() {
		try {
			WaitUtilities.waitForElementVisible(usernameTextBox);
		}
		catch (@SuppressWarnings("unused") Exception e) {
			/* Because of the way that Single Sign-On works, if a previous scenario has
			 * failed - the "logout" step may not have been run.
			 * This means that a subsequent login will actually launch the
			 * browser pre-logged in as whatever the last use was, as opposed to
			 * presenting the username/password entry boxes.
			 *
			 * SO - try to logout if an exception occurs and retry the login....
			 */
			try {
				log.info("Exception awaiting arrival on login page, attempting logging out....");
				Pages.navigationPanel.logout();
				WaitUtilities.waitForElementVisible(usernameTextBox);
			}
			catch (Exception e1) {
				ExceptionHandler.handleException("Could not navigate to Login page",e1,this);
			}
		}

		if (workAroundLoginSessionErrorsSeconds > 0) {
			log.info("OVERNIGHT SUITE - sleeping for " +
					workAroundLoginSessionErrorsSeconds +
					" seconds to work around Session-related errors");
			WaitUtilities.sleep(workAroundLoginSessionErrorsSeconds);
		}
	}

	
	
}
