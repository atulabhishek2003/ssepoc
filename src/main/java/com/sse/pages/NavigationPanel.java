package com.sse.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.sse.utilities.AssertLogger;
import com.sse.utilities.Browser;
import com.sse.utilities.ExceptionHandler;
import com.sse.utilities.WaitUtilities;
import com.sse.utilities.WebElementUtilities;

/**
 * Page class representing the Navigation pane and Search box/button at the top
 * of most standard Salesforce pages<br>
 *
 * @author atul
 */
public class NavigationPanel extends BasePage {
	private static Logger log = LogManager.getLogger(NavigationPanel.class);
	private final Map<String, WebElement> tabNameToWebElementMap = new HashMap<>(15);

	@FindBy(xpath = "//a[@title='Leads']/span")
	private WebElement leadsTab;
	@FindBy(xpath = "//a[@title='Accounts']/span")
	private WebElement accountsTab;
	@FindBy(xpath = "//a[@title='Contacts']/span")
	private WebElement contactsTab;
	@FindBy(xpath = "//a[@title='Cases']/span")
	private WebElement casesTab;

	//Added as part of salesforce quarterly update
	@FindBy(xpath = "//button[@class='slds-button'][contains(text(),'View All')]")
	private WebElement viewAll;


	private static final String WAFFLE_XPATH = "//div[@class='slds-icon-waffle']";
	private WebElement waffleIcon;

	//the below xpath is changed hence commenting out
	//@FindBy(xpath = "//input[@class='slds-input input']")
	//private WebElement searchAppsTextbox;

	//Added as part of salesforce quarterly update

	@FindBy(xpath = "//input[contains(@placeholder,'Search apps')]")
	private WebElement searchAppsTextbox;

	//Currently unused
	@FindBy(xpath="//input[@title='Search Salesforce']/span") private WebElement searchTextbox;

	//User menu - used to logout
	private static final String USER_MENU_BUTTON_XPATH = "//span/img[@title='User']";
	@FindBy(xpath=USER_MENU_BUTTON_XPATH) private WebElement userMenuButton;
	@FindBy(linkText="Log Out") WebElement logoutBtn;

	//Setup menu - used to find the Developer Console
	@FindBy(xpath="//div[@class='setupGear']") private WebElement setupButton;
	@FindBy(xpath="//span[text()='Developer Console']") WebElement developerConsoleLink;
	@FindBy(xpath="//div[@class='slds-grid']//span[text()='Setup']") WebElement setUpLink;
	
	@FindBy(xpath="//*[@data-key='notification']") WebElement notificationIcon;
	@FindBy(xpath="(//*[@class='notification-content'])[1]") WebElement notification;
	
	private static final String NOTIFICATION_CLEAR_BUTTON = "//div[@class='slds-notification-container unsCardQueue']//button[@title='Dismiss notification']";

	/**
	 * Constructor. Initialises map of tab names to WebElements for the main
	 * navigation tabs at the top of most standard Salesforce pages.
	 */
	public NavigationPanel() {
		super();
		tabNameToWebElementMap.put("Leads", leadsTab);
		tabNameToWebElementMap.put("Contacts", contactsTab);
		tabNameToWebElementMap.put("Accounts", accountsTab);
		tabNameToWebElementMap.put("Cases", casesTab);
	}

	/**
	 * Enter search text into the search box.
	 *
	 * @param searchTerm the characters to search for
	 */
	public void enterTextInSearchBox(String searchTerm) {
		try {
			WebElementUtilities.moveToAndSendKeysUsingActions(searchTextbox, searchTerm + Keys.ENTER);
		} catch (Exception e) {
			ExceptionHandler.handleException("Could not enter " + searchTerm + " into search text box", e, this);
		}
	}

	/**
	 * Click the waffle icon
	 *
	 * @param page the name of the tab to be navigated to.
	 */
	public void clickWaffleAndNavigate(String page) {
		final String pageLinkFromAppLauncher = "//p[@class='slds-truncate']";

		boolean pageLinkAvailable = false;
		try {
			int retryCount = 0;
			int maxRetryCount = 3;
			while (retryCount < maxRetryCount) {


				try {
					log.info("Navigating to page using waffle icon : " + page + " : try " + (retryCount+1) + " of " + maxRetryCount);
					WaitUtilities.waitForElementPresentLocated(By.xpath(WAFFLE_XPATH));
					waffleIcon = Browser.driver.findElement(By.xpath(WAFFLE_XPATH));
					WebElementUtilities.objectClick(waffleIcon);
					WaitUtilities.sleep(2);
					WaitUtilities.waitForWebElementToBeClickable(viewAll, 15);
					WebElementUtilities.objectClick(viewAll);
				} catch(WebDriverException e) {
					log.warn(e.getClass()+" Exception caught!!!, try Again!!!");
					Browser.refreshPage();
					WaitUtilities.sleep(2);
					WaitUtilities.waitForElementPresentLocated(By.xpath(WAFFLE_XPATH));
					waffleIcon = Browser.driver.findElement(By.xpath(WAFFLE_XPATH));
					WaitUtilities.waitForElementVisible(waffleIcon);
					WaitUtilities.waitUntilClick(waffleIcon, 10);
					WaitUtilities.sleep(1);
					WaitUtilities.waitUntilClick(viewAll, 8);
				}
				try {
					WaitUtilities.sleep(1);
					WaitUtilities.waitShortForElementVisible(searchAppsTextbox);
				} catch (TimeoutException | StaleElementReferenceException tse) {
					log.warn("Exception waiting for searchApps text box - retrying click of waffle " + tse);
					WaitUtilities.sleep(10);
					log.info("Just slept to give the page a chance to re-render");
					WaitUtilities.waitForElementPresentLocated(By.xpath(WAFFLE_XPATH));
					waffleIcon = Browser.driver.findElement(By.xpath(WAFFLE_XPATH));
					WebElementUtilities.objectClick(waffleIcon);
					WaitUtilities.waitShortForElementVisible(searchAppsTextbox);
				}
				WebElementUtilities.sendKeysSafe(searchAppsTextbox, page);

				try {
					WaitUtilities.waitForElementPresentLocated(By.xpath(pageLinkFromAppLauncher));
					pageLinkAvailable = true;
					break;
				}
				catch (TimeoutException t) {
					Browser.refreshPage();
					WaitUtilities.sleep(10);
					retryCount++;
					log.error("TimeoutException - Could not launch page on try " + (retryCount) + " of "
							+ maxRetryCount, t);
				}
			}
			if (!pageLinkAvailable) throw new RuntimeException("Maximum retry count exceeded trying to navigate to page " + page);

			WaitUtilities.waitForElementVisible(Browser.driver.findElement(By.xpath(pageLinkFromAppLauncher)));
			for (WebElement link:Browser.driver.findElements(By.xpath(pageLinkFromAppLauncher))) {
				if (link.getText().equals(page)) {
					WaitUtilities.waitForWebElementToBeClickable(link, 5);
					WebElementUtilities.objectClick(link);
					break;
				}
			}

			log.info("Just clicked app link " + page);
		} catch (Exception e) {
			ExceptionHandler.handleException("Could not click app link " + page, e, this);
		}
	}

	/**
	 * Click the tab associated with the passed name.
	 *
	 * @param page the name of the tab to be navigated to.
	 */
	public void clickTab(String page) {
		try {
			log.info("Navigating to page " + page);
			WebElement tab = tabNameToWebElementMap.get(page);

			/*
			 * WebElementUtilities.elementClickSafe() generates :
			 * <org.openqa.selenium.WebDriverException: unknown error: Cannot read property
			 * 'defaultView' of undefined
			 *
			 * so, use objectClick() instead.....
			 */
			// WebElementDebug.dumpWebElementToConsole(tab, page + " tab");
			WebElementUtilities.objectClick(tab);
			log.info("Just clicked tab " + page);
		} catch (Exception e) {
			ExceptionHandler.handleException("Could not click tab " + page, e, this);
		}
	}

	@Override
	public void checkArrivalOnPage() {
		WaitUtilities.waitForElementVisible(accountsTab);
	}

	/**
	 * Click the waffle grid.
	 * <p>This generates a list of 'pages' which can be accessed.
	 */
	public void clickWaffleGrid() {
		try {
			WaitUtilities.waitForElementPresentLocated(By.xpath(WAFFLE_XPATH));
			waffleIcon = Browser.driver.findElement(By.xpath(WAFFLE_XPATH));
			WebElementUtilities.objectClick(waffleIcon);
			try {
				WaitUtilities.waitForWebElementToBeClickable(viewAll, 15);
				viewAll.click();
			}catch(WebDriverException e) {
				log.warn(e.getClass()+" Exception caught!!!, try Again!!!");
				Browser.refreshPage();
				WaitUtilities.sleep(2);
				WaitUtilities.waitForElementPresentLocated(By.xpath(WAFFLE_XPATH));
				waffleIcon = Browser.driver.findElement(By.xpath(WAFFLE_XPATH));
				WaitUtilities.waitUntilClick(waffleIcon, 10);
				WaitUtilities.sleep(1);
				WaitUtilities.waitUntilClick(viewAll, 8);
			}
			try {
				WaitUtilities.sleep(1);
				WaitUtilities.waitShortForElementVisible(searchAppsTextbox);
			} catch (TimeoutException | StaleElementReferenceException tse) {
				log.warn("Exception waiting for searchApps text box - retrying click of waffle " + tse);
				WaitUtilities.sleep(5);
				log.info("Just slept to give the page a chance to re-render");
				WaitUtilities.waitForElementPresentLocated(By.xpath(WAFFLE_XPATH));
				waffleIcon = Browser.driver.findElement(By.xpath(WAFFLE_XPATH));
				WebElementUtilities.objectClick(waffleIcon);
				WaitUtilities.waitShortForElementVisible(searchAppsTextbox);
			}
		} catch (Exception e) {
			ExceptionHandler.handleException("Could not find the app search page ", e, this);
		}
	}

	/**
	 * Logout the current user
	 */
	public void logout() {
		boolean loggedOut=false;
		int count=0;
		while(!loggedOut && count<3) {
			try {
				count++;
				log.info("Logging out - attempt " + count);

				//Check for an alert. Must be done first, otherwise you cannot even try to switch to the parent frame....
				//Likely to be "You have unsaved changes" Leave/Cancel
				WaitUtilities.acceptAlertIfOneExists();

				//Moves to parent frame in case we are in an iframe.
				Browser.driver.switchTo().parentFrame();
				WaitUtilities.waitForElementPresentLocated(By.xpath(USER_MENU_BUTTON_XPATH));

				WebElementUtilities.objectClick(userMenuButton);
				try {
					WaitUtilities.waitForElementToBeClickableSafe(logoutBtn, 10);
					WebElementUtilities.objectClick(logoutBtn);
					WaitUtilities.sleep(1); //unavoidable as we don't know whether we can wait for an alert if none will ever appear.
					WaitUtilities.acceptAlertIfOneExists();
				} catch(Exception e) {
					log.warn("Logout Failed. Try again!!! " + e);
					if(!WaitUtilities.doesElementExist(logoutBtn)) {
						WebElementUtilities.objectClick(userMenuButton);
						WaitUtilities.sleep(2);
					}
						WaitUtilities.waitForWebElementToBeClickable(logoutBtn, 5);
						WebElementUtilities.objectClick(logoutBtn);
				}
				loggedOut=true;
				log.info("Successfully logged out");
			} catch (Exception e) {
				log.warn("Couldnt log out!!! >> "+count+"/3");
				if(!loggedOut&&count==3)
					ExceptionHandler.handleException("Could not logout", e, this);
			}

		}

	}
	
	/**
	 * To prevent notifications (e.g. "Training call" etc.) causing issues with buttons, especially in the Configuration and Cart pages,
	 * we can clear them if there are any.
	 * There is no guarantee that a new one won't appear just after, but this will limit the chance to close to zero.
	 * There can be a "Clear All" button if there are 3+ notifications, but for less than 3, there is no such button.
	 * To be safe, we individually clear each notification.
	 */
	
	public void clearNotifications() {
		try {
			List<WebElement> dismissButtons = Browser.driver.findElements(By.xpath(NOTIFICATION_CLEAR_BUTTON));
			if (dismissButtons.size() > 0)
				log.info(dismissButtons.size() + " notification messages found. Attempting to clear them.");
			for (WebElement button : dismissButtons) {
				button.click();
			}
		} catch (Exception e) {
			log.warn("Issue clearing notifications", e);
		}
	}

	/**
	 * To navigate to the notification icon and then click on the notification received
	 * it takes you to the notification page - used by Cancellations.
	 */
	
	public void iNavigateToNotifications() {
		try {
			notificationIcon.click();
			WebElementUtilities.elementClickSafe(notification);

		} catch (Exception e) {
			log.warn("Issue locating the notifications", e);

		}
	}	
	
	/**
	 * Navigate to the Developer Console
	 * <p>
	 * Starts on any Salesforce page, and finishes on the same page. It is usually
	 * invoked from the home page anyway
	 */
	public void navigateToDeveloperConsole() {
		try {
			log.info("Navigating to developer console");
			try {
				WaitUtilities.waitForElementToBeClickableSafe(setupButton, 6);
			} catch (Exception e) {
				log.warn("Issue locating the setup gear, so re-refreshing again", e);
				Browser.refreshPage();
				Pages.sseHomePage.checkArrivalOnPage();
				WaitUtilities.waitForElementToBeClickableSafe(setupButton, 6);
			}
			setupButton.click();
			WaitUtilities.sleep(3);
			WaitUtilities.waitForElementToBeClickableSafe(developerConsoleLink, 20); // sometimes a BIG delay!
			WebElementUtilities.objectClick(developerConsoleLink);
		} catch (Exception e) {
			ExceptionHandler.handleException("Could not navigate to the Developer Console", e, this);
		}
	}

	/**
	 * Navigate to setup
	 * <p>
	 * Starts on any Salesforce page, and finishes on the same page. It is usually
	 * invoked from the home page anyway
	 */
	public void navigateToSetUp() {
		try {
			log.info("Navigating to set up");
			try {
				WaitUtilities.waitForElementToBeClickableSafe(setupButton, 6);
			} catch (Exception e) {
				log.warn("Issue locating the setup gear, so re-refreshing again", e);
				Browser.refreshPage();
				Pages.sseHomePage.checkArrivalOnPage();
				WaitUtilities.waitForElementToBeClickableSafe(setupButton, 6);
			}
			setupButton.click();
			WaitUtilities.sleep(3);
			WaitUtilities.waitForElementToBeClickableSafe(setUpLink, 40); // sometimes a BIG delay!

			WebElementUtilities.elementClickSafe(setUpLink);

		} catch (Exception e) {
			ExceptionHandler.handleException("Could not navigate to the Developer Console", e, this);
		}
	}


	/**
	 * From waffle grid click, check whether the passed page exists and whether we can follow the link to it.
	 * @param can "true" means that the link should appear, otherwise the link should not appear.
	 * @param pageName the name of the page link to validate (e.g. "Invoices")
	 */
	public void validatePageVisibility(boolean can, String pageName) {
		try {
			String pageXpath = "//p[@class='slds-truncate'][text()='" + pageName + "']";

			if (!can) {
				AssertLogger.assertTrue(pageName + " page link should not exist",!WaitUtilities.doesElementExist(pageXpath));
			}
			else {
				try {
					WaitUtilities.waitShortForElementVisibleLocated(By.xpath(pageXpath));
				}catch(Exception e) {
					log.warn("Couldnt locate the xpath of the "+pageName + " Exception " + e);
					Browser.refreshPage();
					WaitUtilities.waitForWebElementToBeClickableByLocator(By.xpath("//div[@class='slds-icon-waffle']"), 20);
					Pages.navigationPanel.clickWaffleGrid();
					WaitUtilities.waitShortForElementVisibleLocated(By.xpath(pageXpath));
				}
				AssertLogger.assertTrue(pageName + " page link exists",WaitUtilities.doesElementExist(pageXpath));
				WaitUtilities.waitForElementPresentLocated(By.xpath(pageXpath));
				WaitUtilities.waitForElementVisibleLocated(By.xpath(pageXpath));
				WaitUtilities.sleep(5);
				WebElement pageLink = Browser.driver.findElement(By.xpath(pageXpath));
				WebElementUtilities.objectClick(pageLink);
				WaitUtilities.sleep(5);

				switch (pageName) {
				
			}
			}
			
			
		}
		
		catch (Exception e) {
			ExceptionHandler.handleException("Issue checking whether Invoice option exists", e, this);
		}
	}
}


