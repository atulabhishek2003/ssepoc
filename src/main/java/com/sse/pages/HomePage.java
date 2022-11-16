package com.sse.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.sse.utilities.Browser;
import com.sse.utilities.ExceptionHandler;
import com.sse.utilities.StringUtilities;
import com.sse.utilities.TextBoxUtilities;
import com.sse.utilities.WaitUtilities;

/**
 * Page class representing the Salesforce home page
 *
 * @author mitchella3
 */
public class HomePage extends BasePage {

	private static Logger log = LogManager.getLogger(HomePage.class);
	private static final String TITLE = "Home | Salesforce";

	@FindBy(className = "slds-global-header__logo")
	private WebElement homePageLogo;

	// For scheduled maintenance box..... should it appear.
	@FindBy(xpath = "//div[@id='alert']//span[text()='Scheduled Maintenance']")
	private WebElement scheduledMaintenanceBox;
	@FindBy(xpath = "//div[@id='message']/form/p/a")
	private WebElement scheduledMaintenanceBoxContinueLink;
	@FindBy(xpath = "//input[@title='Search Salesforce']")
	private WebElement searchSalesforceTab;

	// Encountered when logging out and logging in quickly. It seems the "old"
	// session is still "somehow active".
	private static final String SESSION_ERROR_XPATH = "//h2/lightning-formatted-text[text()='Your session has ended']";

	/**
	 * Globally accessible String containing the URL of the SalesForce instance.
	 * This includes the protocol and domain with a trailing slash. E.g.
	 * https://cs88.salesforce.com/
	 */
	public String instanceURL;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sse.pages.BasePage#checkArrivalOnPage()
	 */
	@Override
	public void checkArrivalOnPage() {
		try {
			/*
			 * Occasionally you will get directed to "Scheduled Maintenance Notification"
			 * page. In this instance, we need to hit Continue to proceed.
			 */
			if (WaitUtilities.doesElementExist(scheduledMaintenanceBox)) {
				log.warn("*** Scheduled maintenance page encountered. Trying to click Continue..... ***");
				scheduledMaintenanceBoxContinueLink.click();
				WaitUtilities.sleep(2);
			}
			WaitUtilities.sleep(5); // Give home page and dashboard a chance to arrive.....

			/*
			 * Sometimes, after logging in, the animated Salesforce icon just stays there
			 * forever.....
			 */
			if (!WaitUtilities.doesElementExist(homePageLogo)) {
				log.info("Home page logo does not exist, refreshing until it does...");
				WaitUtilities.refreshUntilWebElementVisible(homePageLogo, 5, "Home page logo");
			}
			String[] currentURLBits = StringUtilities.splitURL(Browser.url());
			instanceURL = currentURLBits[0] + "//" + currentURLBits[2] + "/";
			log.info("Instance URL = " + instanceURL);

			if (WaitUtilities.doesElementExist(SESSION_ERROR_XPATH)) {
				log.info("Session ended error box on page, refreshing until it disappears...");
				WaitUtilities.refreshUntilWebElementDoesNotExist(SESSION_ERROR_XPATH, 5, "Session Ended error");
				// Re-refresh to see the home page logo here as well
				if (!WaitUtilities.doesElementExist(homePageLogo)) {
					log.info("Home page logo does not exist after session error, refreshing until it does...");
					WaitUtilities.refreshUntilWebElementVisible(homePageLogo, 5, "Home page logo");
				}
			}

			try {
				WaitUtilities.waitForPageTitle(HomePage.TITLE);
			} catch (@SuppressWarnings("unused") Exception noTitle) {
				log.info("Title not correct. Refreshing up to 3 times.");
				WaitUtilities.refreshUntilPageTitleCorrect(HomePage.TITLE, 3, "Home page title");
			}

		} catch (Exception e) {
			ExceptionHandler.handleException("Salesforce home page not loaded", e, this);
		}
	}

	/**
	 * Search for element and clicks
	 * 
	 * @param searchFor element we want to navigate
	 */
	public void searchFor(String searchFor) {
		try {
			TextBoxUtilities.enterTextIntoTextbox(searchSalesforceTab, searchFor);
			searchSalesforceTab.click();
			WaitUtilities.waitShortForElementVisibleLocated(By.xpath("//mark[text()='" + searchFor + "']"));
			WebElement searchElement = Browser.driver.findElement(By.xpath("//mark[text()='" + searchFor + "']"));
			WaitUtilities.waitForWebElementToBeClickable(searchElement, 7);
			searchElement.click();
		} catch (Exception e) {
			ExceptionHandler.handleException("Issue searching for: " + searchFor, e, this);
		}
	}

}
