package com.sse.utilities;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfAllElements;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sse.utilities.timers.L2IStopWatchController;
/**
 * A collection of utilities associated with waiting strategies.
 * Some are potentially UNTESTED and most are probably improvable, but this is a starting point!!!
 * @author atul
 */
public final class WaitUtilities {
	private static Logger log = LogManager.getLogger(WaitUtilities.class);
	
	private WaitUtilities() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}	
	
    /**
     * Tries to wait for an Element to be clickable.
     * <p>If a StaleElementReferenceException occurs, will <br>
     * retry 15 times. A RuntimeException will be thrown if the retry count is exceeded.
     * @param element the WebElement
     * @param waitTimeInSeconds number of seconds to wait for element to be clickable.
     */
    public static void waitForElementToBeClickableSafe(WebElement element, int waitTimeInSeconds) {
    	L2IStopWatchController.waitWatch.start();
        WaitUtilities.waitForElementWithSleep(element, waitTimeInSeconds);
        int count = 0;
        boolean continueOn = false;
        while (!continueOn)
        {
            try
            {
                waitForWebElementToBeClickable(element, waitTimeInSeconds);
                continueOn = true;
            }
            catch (StaleElementReferenceException e)
            {
                count++;
                if (count == 16)
                    throw new RuntimeException("StaleElementReferenceException not going away when waiting for element to become clickable.", e);
                // Do not remove
                sleepMillis(250);
            }
        }
    	L2IStopWatchController.waitWatch.stop();
    }



    /**
     * Waits for a WebElement to be visible.
     * <br>Requires that the element already exists in the current DOM.
     * <p>The underlying Selenium code mentions :
     * <p>An expectation for checking that an element, known to be present on the DOM of a page, is visible.
     * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
     *
     * @param element the WebElement
     */
    public static void waitForElementVisible(WebElement element) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.defaultDriverWait.until(visibilityOf(element));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for a WebElement to be visible USING A SHORTER WAIT TIME.
     * <br>Requires that the element already exists in the current DOM.
     * <p>The underlying Selenium code mentions :
     * <p>An expectation for checking that an element, known to be present on the DOM of a page, is visible.
     * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
     *
     * @param element the WebElement
     */
    public static void waitShortForElementVisible(WebElement element) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.shortDriverWait.until(visibilityOf(element));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for a WebElement to be <b>invisible</b>.
     * <br>Requires that the element already exists in the current DOM.
     * <p>The underlying Selenium code mentions :
     * <p>An expectation for checking that an element, known to be present on the DOM of a page, is visible.
     * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
     *
     * @param element the WebElement
     */
    public static void waitForElementInvisible(WebElement element) {
    	L2IStopWatchController.waitWatch.start();
    	try {
	    	List<WebElement> list = new ArrayList<>();
	    	list.add(element);
	    	//LONGTERM invisibilityOf(element) is in current Selenium API but perhaps a different version???
	    	//To be investigated much later!
	    	Browser.defaultDriverWait.until(invisibilityOfAllElements(list));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for a WebElement to be <b>invisible</b> using a LONGER wait time.
     * <br>Requires that the element already exists in the current DOM.
     * <p>The underlying Selenium code mentions :
     * <p>An expectation for checking that an element, known to be present on the DOM of a page, is visible.
     * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
     *
     * @param element the WebElement
     */
    public static void waitLongForElementInvisible(WebElement element) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		List<WebElement> list = new ArrayList<>();
    		list.add(element);
    		//LONGTERM invisibilityOf(element) is in current Selenium API but perhaps a different version???
    		//To be investigated much later!
    		Browser.longDriverWait.until(invisibilityOfAllElements(list));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for a WebElement to be <b>invisible</b> using a SHORTER wait time.
     * <br>Requires that the element already exists in the current DOM.
     * <p>The underlying Selenium code mentions :
     * <p>An expectation for checking that an element, known to be present on the DOM of a page, is visible.
     * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
     *
     * @param element the WebElement
     */
    public static void waitShortForElementInvisible(WebElement element) {
    	L2IStopWatchController.waitWatch.start();
    	try {
	    	List<WebElement> list = new ArrayList<>();
	    	list.add(element);
	    	//LONGTERM invisibilityOf(element) is in current Selenium API but perhaps a different version???
	    	//To be investigated much later!
	    	Browser.shortDriverWait.until(invisibilityOfAllElements(list));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for the element located from the passed By locator to be visible.
     * <br>Doesn't require that the element already exists in the current DOM, as the call waits for this to occur too.
     * <p>The underlying Selenium code mentions :
     * An expectation for checking that an element is present on the DOM of a page and visible.
     * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
     * @param locator the By locator
     */
    public static void waitForElementVisibleLocated(By locator) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.defaultDriverWait.until(visibilityOfElementLocated(locator));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for the element located from the passed By locator to present on the DOM of a page, but not necessarily visible.
     * <br>Doesn't require that the element already exists in the current DOM, as the call waits for this to occur too.
     * <p>The underlying Selenium code mentions :
     * An expectation for checking that an element is present on the DOM of a page. This does not necessarily mean that the element is visible.
     * @param locator the By locator
     */

    public static void waitForElementPresentLocated(By locator) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.defaultDriverWait.until(presenceOfElementLocated(locator));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for the element located from the passed By locator to present on the DOM of a page USING A LONGER WAIT TIME
     * , but not necessarily visible.
     * <br>Doesn't require that the element already exists in the current DOM, as the call waits for this to occur too.
     * <p>The underlying Selenium code mentions :
     * An expectation for checking that an element is present on the DOM of a page. This does not necessarily mean that the element is visible.
     * @param locator the By locator
     */
    public static void waitLongForElementPresentLocated(By locator) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.longDriverWait.until(presenceOfElementLocated(locator));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for the element located from the passed By locator to be visible USING A LONGER WAIT TIME.
     * <br>Doesn't require that the element already exists in the current DOM, as the call waits for this to occur too.
     * <p>The underlying Selenium code mentions :
     * An expectation for checking that an element is present on the DOM of a page and visible.
     * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
     * @param locator the By locator
     */
    public static void waitLongForElementVisibleLocated(By locator) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.longDriverWait.until(visibilityOfElementLocated(locator));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for the element located from the passed By locator to be visible USING A SHORTER WAIT TIME.
     * <br>Doesn't require that the element already exists in the current DOM, as the call waits for this to occur too.
     * <p>The underlying Selenium code mentions :
     * An expectation for checking that an element is present on the DOM of a page and visible.
     * Visibility means that the element is not only displayed but also has a height and width that is greater than 0.
     * @param locator the By locator
     */
    public static void waitShortForElementVisibleLocated(By locator) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.shortDriverWait.until(visibilityOfElementLocated(locator));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for the element located from the passed By locator to be invisible USING A SHORTER WAIT TIME.
     * Wait for an Element specified by the given By locator to be EITHER invisible or not present on the DOM.
     * @param locator the By locator
     */
    public static void waitShortForElementInvisibleLocated(By locator) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.shortDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Makes the current Thread sleep for a number of seconds.
     * @param timeSeconds - time in seconds
     */
    public static void sleep(long timeSeconds) {
   		sleepMillis(1000*timeSeconds);
    }

    /**
     * Makes the current Thread sleep for a number of milliseconds.
     * @param timeMillis - time in milliseconds
     */
    public static void sleepMillis(long timeMillis) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Thread.sleep(timeMillis);
    	}
    	catch (InterruptedException ie) {
    		log.warn("InterruptedException thrown during sleep",ie);
    	}
   		L2IStopWatchController.waitWatch.stop();
    }

    /**
     * Wait for an Element to become invisible by checking that the given text which is in the Element becomes not present.
     * @param element the WebElement
     * @param textToBePresent text in the element
     */
    public static void waitForElementInvisible(WebElement element, String textToBePresent) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.defaultDriverWait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(element, textToBePresent)));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Wait for an Element specified by the given By locator to be EITHER invisible or not present on the DOM.
     * No exception is thrown by this method
     * @param webElementBy the By locator for the given element.
     */
    public static void waitForElementInvisibleBy(By webElementBy) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.defaultDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(webElementBy));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Wait until the current page title contains the passed String.
     * @param title the page title
     */
    public static void waitForPageTitle(String title) {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.defaultDriverWait.until(ExpectedConditions.titleContains(title));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Taken from https://www.testingexcellence.com/webdriver-wait-page-load-example-java/
     * Waits until the page status is 'complete' before returning.
     * UNTESTED but initially unclear if it works.
     */
    public static void waitForPageToLoad() {
    	L2IStopWatchController.waitWatch.start();
    	try {
    		ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    @Override
					public Boolean apply(WebDriver driver) {
                        return Boolean.valueOf("complete".equals(((JavascriptExecutor)driver).executeScript("return document.readyState")));
                    }
                };
                Browser.defaultDriverWait.until(pageLoadCondition);
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits until the current page's URL contains a given String.
     * <b>Useful in checking that a page has started to load.
     * @param partialURL a String which should exist in the URL
     */
    public static void waitForUrlToContain(String partialURL) {
    	log.info("Waiting for URL to contain the string : " + partialURL);
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Browser.longDriverWait.until(ExpectedConditions.urlContains(partialURL));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Explicit wait for a WebElement to be clickable.
     * @param webElement the WebElement to be waited on.
     * @param waitTimeInSeconds how long to wait
     */
    public static void waitForWebElementToBeClickable(WebElement webElement, int waitTimeInSeconds){
        int sleepTimeOutMillis = 250;
    	L2IStopWatchController.waitWatch.start();
    	try {
    		Wait<WebDriver> waitLocal = new WebDriverWait(Browser.driver, waitTimeInSeconds, sleepTimeOutMillis);
    		waitLocal.until(ExpectedConditions.elementToBeClickable(webElement));
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Explicit wait for a locator of webelement to be clickable.
     * @param locator the locator of WebElement to be waited on.
     * @param waitTimeInSeconds how long to wait
     */
    public static void waitForWebElementToBeClickableByLocator(By locator, int waitTimeInSeconds){
    	L2IStopWatchController.waitWatch.start();
    	try {
    		waitForElementVisibleLocated(locator);
    		WebElement waitForElement=Browser.driver.findElement(locator);
    		waitForWebElementToBeClickable(waitForElement,waitTimeInSeconds);
    	}catch(Exception e) {
    		log.warn(e.getClass()+" Exception caught!!! Waiting for element to be clickable failed!!! >> "+locator);
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for a given String to appear in the HTML body of a page.
     * @param textToFind the text which a page should contain
     */
    public static void waitforTextToAppear(String textToFind) {
    	L2IStopWatchController.waitWatch.start();
    	try {
	        if (!doesElementExist(By.cssSelector("body"), 2000))
	            throw new RuntimeException("Cannot find 'body' region on the page");

	        boolean textFound = false;
	        for (int count = 0; count < 12; count++) {
	            textFound = Browser.driver.findElement(By.cssSelector("body")).getText().contains(textToFind);
	            if (textFound) {
	                break;
	            }
	            sleepMillis(250);
	        }
	        if (!textFound)
	            throw new RuntimeException("Searching for string '" + textToFind + "' on the current page but it appears to not be present.");
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Returns true if the Element exists after waiting for up to timeInMilliseconds.
     * @param webElementBy - a By locator for the Element
     * @param timeInMilliseconds number of milliseconds to wait
     * @return true if the Element exists, false otherwise.
     */
    public static boolean doesElementExist(By webElementBy, int timeInMilliseconds) {
    	L2IStopWatchController.waitWatch.start();
        // Wait for a maximum specified amount of time for an element to exist and return true or false depending on the outcome

        int timeElapsedInMilliseconds = 0;
        boolean elementFound = false;

        // Unavoidable thread sleep - to give the screen time to update
        sleepMillis(500);

        while (timeElapsedInMilliseconds < timeInMilliseconds)
        {
            try {
        		List<WebElement> elements = Browser.driver.findElements(webElementBy);
                elementFound = elements.size() > 0;
                break;
                //TODO There is a bug here! the catch block cannot ever be entered. 
                // It is used in 5+ places apparently without issue, so need to review this!!!!! 
            }
            catch (@SuppressWarnings("unused") NoSuchElementException ex) {
            	sleepMillis(250);
                timeElapsedInMilliseconds += 250;
            }
        }
        L2IStopWatchController.waitWatch.stop();
        return elementFound;
    }

    /**
     * Returns true if the Element exists.
     * <p>Note that org.openqa.selenium.WebDriver.findElement(By by) is called in this which does poll/wait
     * using the implicit wait.
     * @param webElementBy - a By locator for the Element
     * @return true if the Element exists, false otherwise.
     */
    public static boolean doesElementExist(By webElementBy) {
    	L2IStopWatchController.waitWatch.start();

		List<WebElement> elements = Browser.driver.findElements(webElementBy);
        L2IStopWatchController.waitWatch.stop();
		return elements.size() > 0;
    }

    /**
     * Returns true if the Element specified by the provided XPath exists.
     * <p>Note that org.openqa.selenium.WebDriver.findElement(By by) is called in this which does poll/wait
     * using the implicit wait.
     * @param xpath - an Xpath route to the Element(s)
     * @return true if the Element exists, false otherwise.
     */
    public static boolean doesElementExist(String xpath) {
    	By webElementBy = By.xpath(xpath);
    	return doesElementExist(webElementBy);
    }

    /**
     * Returns true if the passed WebElement exists, false otherwise.
     * @param element the WebElement to be found
     * @return true if the element exists, false otherwise
     */
    public static boolean doesElementExist(WebElement element) {
    	boolean elementFound = false;
		try {
			element.getText();
			elementFound = true;
		}
		catch (@SuppressWarnings("unused") WebDriverException ex) {
			//no action required.
    	}
    	return elementFound;
    }

    /**
     * Waits for a maximum specified amount of time until the passed element is enabled.
     * <p>If the element already exists and is enabled, this returns immediately.
     * <p>Throws a <code>RuntimeException</code> if the maximum specified time is passed but the element is still disabled
     *
     * @param element the WebElement
     * @param timeInMilliseconds time to wait
     * @throws RuntimeException if the element is still NOT enabled after the wait time
     */
	public static void waitForElementToBeEnabled(WebElement element, int timeInMilliseconds) {
    	L2IStopWatchController.waitWatch.start();
    	try {
	        int timeElapsedInMilliseconds = 0;
	        while (timeElapsedInMilliseconds < timeInMilliseconds)
	        {
	            try {
	            	if (element.isEnabled()) return;
	            }
	            catch (@SuppressWarnings("unused") NoSuchElementException | StaleElementReferenceException ex) {
	                //keep looping as the element may appear or not be stale...
	            }
	            sleepMillis(250);
	            timeElapsedInMilliseconds += 250;
	        }
	        throw new RuntimeException("Element + " + element + " is still NOT enabled after waiting for " + timeElapsedInMilliseconds + " ms");
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Waits for a maximum specified amount of time until the passed element is NOT enabled.<br>
     * If the element is not found, then it is deemed not enabled, so the method returns normally<br>
     * Throws a <code>RuntimeException</code> if the maximum specified time is passed but the element is still enabled.
     *
     * @param element the WebElement
     * @param timeInMilliseconds time to wait
     * @throws RuntimeException if the element is still enabled after the wait time
     */
    public static void waitForElementToBeNotEnabled(WebElement element, int timeInMilliseconds) {
    	L2IStopWatchController.waitWatch.start();
    	try {
	        int timeElapsedInMilliseconds = 0;
	        while (timeElapsedInMilliseconds < timeInMilliseconds)
	        {
	            try {
	            	if (!element.isEnabled()) return;
	            }
	            catch (@SuppressWarnings("unused") NoSuchElementException|StaleElementReferenceException ex) {
	            	return;
	            }
	            sleepMillis(250);
	            timeElapsedInMilliseconds += 250;
	        }
	        throw new RuntimeException("Element + " + element + " is still enabled after waiting for " + timeElapsedInMilliseconds + " ms");
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

 	/**
	 * Convenience method to refresh until an element becomes visible and also contains one of the Strings
	 * in the passed List in its text content.
	 * <p>If the passed List is empty or null, this method will return once the element becomes visible.
	 * @param elemt the WebELement to wait for
	 * @param searchTexts a List of Strings which we expect to appear once the WebElement becomes visible
	 * @param numberOfRefreshes the maximum number of refreshes to perform
	 * @param millisBetweenRefreshes the time between refreshes
	 * @param elementDescription a description of what element we are looking at to make the Exception understandable
	 * @throws WebDriverException if the Element does not appear or it does and the text does not contain what is expected.
	 */
	public static void refreshUntilWebElementVisibleAndContainsText
		(final WebElement elemt, final List<String> searchTexts, final int numberOfRefreshes,
			long millisBetweenRefreshes, String elementDescription) {
    	L2IStopWatchController.waitWatch.start();
    	try {
			refreshUntilWebElementVisible(elemt, numberOfRefreshes, elementDescription);

			if (searchTexts == null || searchTexts.isEmpty()) return;
			for (int i = 0; i < numberOfRefreshes; i++) {
				for (String search : searchTexts) {
					if (elemt.getText().contains(search)) {
						return;
					}
				}
				sleepMillis(millisBetweenRefreshes);
				log.info("About to refresh page to wait for element to contain text : " + searchTexts.toString());
				Browser.refreshPage();
			}
			throw new RuntimeException(elementDescription + " not containing text '" + searchTexts + "' after " + numberOfRefreshes + " refreshes. Current element text : " + elemt.getText());
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
	}

	/**
	 * Convenience method to refresh until an element becomes visible
	 * @param elemt the WebELement to wait for
	 * @param numberOfRefreshes the maximum number of refreshes to perform
	 * @param elementDescription a description of what element we are looking at to make the Exception understandable
	 * @throws WebDriverException if the Element does not appear or it does and the text does not contain what is expected.
	 */
	@SuppressWarnings("null")
	public static void refreshUntilWebElementVisible(final WebElement elemt, final int numberOfRefreshes,
			String elementDescription) {
		boolean okToContinue = false; // i.e. assume we need to keep refreshing until something nice happens.
    	L2IStopWatchController.waitWatch.start();
    	try {
    		WebDriverException mostRecentException = null;
			for (int i = 0; i < numberOfRefreshes; i++) {
				try {
					log.info("About to refresh page to wait for element to become visible : " + elementDescription);
					Browser.refreshPage();
					Browser.driverWait15Seconds.until(visibilityOf(elemt));
					okToContinue = true;
					break;
				}
				catch (NoSuchElementException | TimeoutException e) {
					mostRecentException = e;
				}
			}
			if (!okToContinue)
				throw mostRecentException;
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
	}

	/**
	 * Convenience method to refresh until an element becomes visible with 15 sec wait
	 * @param locator the locator of the WebElement to wait for
	 * @param numberOfRefreshes the maximum number of refreshes to perform
	 * @param elementDescription a description of what element we are looking at to make the Exception understandable
	 * @throws WebDriverException if the Element does not appear or it does and the text does not contain what is expected.
	 */
	@SuppressWarnings("null")
	public static void refreshUntilWebElementVisible(By locator, final int numberOfRefreshes,
			String elementDescription) {
		boolean okToContinue = false; // i.e. assume we need to keep refreshing until something nice happens.
    	L2IStopWatchController.waitWatch.start();
    	try {
    		WebDriverException mostRecentException = null;
			for (int i = 0; i < numberOfRefreshes; i++) {
				try {
					log.info("About to refresh page to wait for element to become visible : " + elementDescription);
					Browser.refreshPage();
					Browser.driverWait15Seconds.until(visibilityOfElementLocated(locator));
					okToContinue = true;
					break;
				}
				catch (NoSuchElementException | TimeoutException e) {
					mostRecentException = e;
				}
			}
			if (!okToContinue)
				throw mostRecentException;
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
	}

	/**
	 * Convenience method to refresh until  the page title becomes correct.
	 * @param title the page title to wait for
	 * @param numberOfRefreshes the maximum number of refreshes to perform
	 * @param elementDescription a description of what element we are looking at to make the Exception understandable
	 * @throws TimeoutException if the page title ultimately does not contain what is expected.
	 */
	@SuppressWarnings("null")
	public static void refreshUntilPageTitleCorrect(String title, final int numberOfRefreshes, String elementDescription) {
		boolean okToContinue = false; // i.e. assume we need to keep refreshing until something nice happens.
    	L2IStopWatchController.waitWatch.start();
    	try {
    		TimeoutException mostRecentException = null;
			for (int i = 0; i < numberOfRefreshes; i++) {
				try {
					log.info("About to refresh page to wait for element to become visible : " + elementDescription);
					Browser.refreshPage();
					waitForPageTitle(title);
					okToContinue = true;
					break;
				}
				catch (TimeoutException e) {
					mostRecentException = e;
				}
			}
			if (!okToContinue)
				throw mostRecentException;
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
	}

	/**
	 * Convenience method to refresh until an element ceases to exist.
	 * <p>Note that this starts with a refresh regardless
	 * @param xpath the Xpath of the WebELement to wait for it not to exist
	 * @param numberOfRefreshes the maximum number of refreshes to perform
	 * @param elementDescription a description of what element we are looking at to make the Exception understandable
	 * @throws RuntimeException if the Element does not cease to exist after several refreshes
	 */
	public static void refreshUntilWebElementDoesNotExist(final String xpath, final int numberOfRefreshes, String elementDescription) {
    	L2IStopWatchController.waitWatch.start();
    	try {
			for (int i = 0; i < numberOfRefreshes; i++) {
				log.info("About to refresh page to wait for element to not exist. Xpath : " + xpath);
				Browser.refreshPage();
				sleep(4); //After refreshing a page, most things will not exist to start with!
				if (!doesElementExist(xpath)) {
					return;
				}
			}
			throw new RuntimeException(elementDescription + " still exists after " + numberOfRefreshes + " refreshes. Current element xpath : " + xpath);
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
	}

	/**
	 * Convenience method to refresh until an element exists.
	 * <p>Note that this starts with a refresh regardless
	 * @param xpath the Xpath of the WebELement to wait for it not to exist
	 * @param numberOfRefreshes the maximum number of refreshes to perform
	 * @param elementDescription a description of what element we are looking at to make the Exception understandable
	 * @throws RuntimeException if the Element does not exist after several refreshes
	 */
	public static void refreshUntilWebElementExists(final String xpath, final int numberOfRefreshes, String elementDescription) {
    	L2IStopWatchController.waitWatch.start();
    	try {
			for (int i = 0; i < numberOfRefreshes; i++) {
				log.info("About to refresh page to wait for element to exist. Xpath : " + xpath);
				Browser.refreshPage();
				sleep(4); //After refreshing a page, most things will not exist to start with!
				if (doesElementExist(xpath)) {
					return;
				}
			}
			throw new RuntimeException(elementDescription + " does not exist after " + numberOfRefreshes + " refreshes. Current element xpath : " + xpath);
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
	}

    /**
	 * Monitors the text value each second of an Element over 'seconds' seconds. If
	 * it changes, the method returns once the change is detected. Otherwise it will
	 * return after the time limit has elapsed. It will also simply return in the
	 * event of any Exception.
	 *
	 * @param element the WebElement whose text value should change
	 * @param seconds the maximum number of seconds to wait
	 */
	public static void returnIfTextValueChangesOrAfterSpecificTime(WebElement element, int seconds) {
    	L2IStopWatchController.waitWatch.start();
    	try {
			String initialValue = element.getText();
			log.debug(new Date() + "INITIAL VALUE = " + initialValue);
			for (int i = 0; i < seconds; i++) {
				sleep(1);
				String currentValue = element.getText();
				log.debug("CURRENT VALUE = " + currentValue);
				if (!initialValue.equals(currentValue))
					return;
			}
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

	/**
	 * Monitors the text value of an Element over 'seconds' seconds until it changes from a specified value to a new value.
	 * If it changes within the time interval, the method returns the new value once the change is detected.
	 * Otherwise it will throw a RuntimeException.
	 *
	 * @param element the WebElement under scrutiny
	 * @param valueToChangeFrom the text value which the WebElement should change from.
	 * @param seconds the number of seconds to retry for.
	 * @return the new value the element's text value has changed to.
	 */
	public static String returnNewTextValueOnceChangedFromValue(WebElement element, String valueToChangeFrom, int seconds) {
		final int loopSleepMillis = 250;
    	L2IStopWatchController.waitWatch.start();
    	try {
			for (int i = 0; i < 1000/loopSleepMillis * seconds; i++) {
				try {
					String currentValue = element.getText();
					log.debug("CURRENT VALUE = " + currentValue);
					if (!valueToChangeFrom.equals(currentValue))
						return currentValue;
					sleepMillis(loopSleepMillis);
				}
				catch (StaleElementReferenceException e) {
					log.warn("StaleElementReferenceException caught, will try again... " + e);
				}
			}
			throw new RuntimeException(element + " has not changed value from '" + valueToChangeFrom + "' after " + seconds + " seconds.");
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
	}

    /**
     * Waits for up to 'seconds' a WebElement to not be displayed. <br>
     * Useful for e.g. Address search element
     * @param element the WebElement
     * @param seconds the number of seconds to wait for
     */
    public static void waitToVanish(WebElement element, int seconds) {
    	L2IStopWatchController.waitWatch.start();
    	try {
	    	if (!doesElementExist(element)) {
	    		//Just in case we're too early!
	    		sleep(1);
	    	}
	    	for (int i = 0; i < seconds; i++) {
				if (!element.isDisplayed()) {
					return;
				}
				sleep(1);
			}
			throw new RuntimeException(element + " not disappearing after " + seconds + " seconds. Current element text : " + element.getText());
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * Checks to see if an alert is already present, then accepts it irrespective of the text.
     */
    public static void acceptAlertIfOneExists() {
        try {
            Alert alert = Browser.driver.switchTo().alert();
            log.info("Alert appeared with text : " + alert.getText());
            alert.accept();
        } catch (@SuppressWarnings("unused") Exception e) {
        	// no action
        }
    }

    /**Instead of sleeping to element be visible that method waits and checks the element if it is visible
     * @param element the element we wait to be visible
     * @param waitLimitSeconds the max limit of waiting
     */
    public static void waitForElementWithSleep(WebElement element, int waitLimitSeconds) {
    	L2IStopWatchController.waitWatch.start();
    	try {
	        int seconds = 0;
	        while (seconds < waitLimitSeconds)
	        {
	        	if(doesElementExist(element))
	        		return;
	        	sleepMillis(1000);
	        	seconds++;
	        }
	        throw new RuntimeException("Element + " + element + " is still not exist after waiting for " +waitLimitSeconds+ " second(s)");
    	}
    	finally {
    		L2IStopWatchController.waitWatch.stop();
    	}
    }

    /**
     * that method waits and trying to click until a time limit specified
     * @param element the element we are waiting and trying to click
     * @param waitTimeLimitInSeconds the time limit of waiting
     */
    public static void waitUntilClick(WebElement element, int waitTimeLimitInSeconds) {
    	L2IStopWatchController.waitWatch.start();
    	 int count = 0;
         boolean continueOn = false;
         while (!continueOn) {
        	 try{
        		 MouseHoverUtilities.hover(element);
        		 waitForWebElementToBeClickable(element, waitTimeLimitInSeconds);
        		 element.click();
        		 continueOn = true;
        	 }catch (Exception e){
        		 count++;
        		 if (count == 40)
        			 ExceptionHandler.handleException("Issue clicking to WebElement: "+element, e, WaitUtilities.class);

        		 sleepMillis(1000);
        	 }

         }

         L2IStopWatchController.waitWatch.stop();
    }

    /**
     * that method waits and trying to click until a time limit specified
     * @param webElementBy the By Locator of the element we are waiting and trying to click
     * @param waitTimeLimitInSeconds the time limit of waiting
     */
    public static void waitUntilClick(By webElementBy, int waitTimeLimitInSeconds) {
    	L2IStopWatchController.waitWatch.start();
    	 int count = 0;
         boolean continueOn = false;
         while (!continueOn) {
        	 try{
        		 WebElement element=Browser.driver.findElement(webElementBy);
        		 MouseHoverUtilities.hover(element);
        		 waitForWebElementToBeClickable(element, waitTimeLimitInSeconds);
        		 element.click();
        		 continueOn = true;
        	 } catch (Exception e){
        		 count++;
        		 if (count == 20)
        			 ExceptionHandler.handleException("Issue clicking to WebElement: "+webElementBy, e, WaitUtilities.class);

        		 sleepMillis(500);
        	 }

         }

         L2IStopWatchController.waitWatch.stop();
    }



}
