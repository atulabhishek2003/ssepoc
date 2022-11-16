package com.sse.utilities;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
/**
 * Various general utilities for 'general' WebElements.
 * @author mitchella3
 */
public final class WebElementUtilities {
	
	private WebElementUtilities() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}	
		
	
    /**
     * Tries element.click() for up to 10 times.<br>
     * NOTE that this does not appear to work well for checkboxes or some 'unusual' elements<br>
     * If you have issues, try the objectClick() method instead.
     * @param elementToClick the WebElement to click
     * @throws WebDriverException thrown if we cannot successfully 'click' the element after 10 retries.
     */
    public static void elementClickSafe(WebElement elementToClick) {

        final int maxTries = 10;
        for (int i = 0; i < maxTries; i++) {
            try {
                elementToClick.click();
                break;
            }
            catch (WebDriverException e) {
            	if (i == maxTries-1)
            		throw e;
            }
            WaitUtilities.sleepMillis(250);
        }
    }



    /**
     * Clicks using a JavascriptExecutor kludge! <br>
     * The 'normal' element.click() can fail under many circumstances with unexpected errors. <br>
     * This method seems to be the most useful for the following scenarios : <br>
     * <ul>
     * <li> Clicking checkboxes
     * <li> Clicking elements which are not visible on the page (even if Enabled etc...)
     * </ul>
     * @param element the WebElement to be clicked.
     */
    public static void objectClick(WebElement element) {
    	JavascriptExecutor executor = (JavascriptExecutor)(Browser.driver);
		executor.executeScript("arguments[0].click();", element);
    }

    /**
     * Attempts to use Javascript to scroll the passed element into view (by default positioned at the top of the parent element)
     * <p>It is possible that this will simply not do anything if the element is not visible/displayed or non-existent.
     * @param element the WebElement to try to scroll into view.
     */
    public static void scrollIntoViewUsingJavascript(WebElement element) {
    	JavascriptExecutor executor = (JavascriptExecutor)(Browser.driver);
    	executor.executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    /**
     * Read the ::after content value of a checkbox and return "true" (ticked) or "false" (unticked). See ATEH-92. 
     * <p>In Lightning since 2019, the checkbox ticked is implemented as a Pseudo-CSS element, so we have
     * to use Javascript to determine the value
     * @param element the WebElement of the checkbox element to which the ::after element is applied
     * @return "true" if the checkbox is ticked, "false" otherwise.
     */
    public static String determineCheckboxState(WebElement element) {
    	JavascriptExecutor executor = (JavascriptExecutor)(Browser.driver);
    	String script = "return window.getComputedStyle(arguments[0],'::after').getPropertyValue('content')";
    	String content = (String) executor.executeScript(script, element);
    	return "\"\"".equals(content)+"";
    }

    /**
     * Move to a WebElement and click.
     * @param elemt the WebElement to move to and click
     */
    public static void moveToAndClickViaActions(WebElement elemt){
    	Actions actions = new Actions(Browser.driver);
    	actions.moveToElement(elemt).click().build().perform();
    }

    /**
     * Move to a WebElement and attempt two single clicks using Actions.
     * <p>This can be used if the Actions doubleClick() method does not work.
     * <p>Firstly, note that all Exceptions are passed up to the calling method.
     * <p>Secondly, the method may silently do nothing. This usually occurs when the Element
     * to double-click is not the correct one to respond to the action.
     * @param element the WebElement to move to and double-click
     */
    public static void singleClickTwice(WebElement element){
		Actions actions = new Actions(Browser.driver);
		actions.moveToElement(element);
		actions.click(element).click(element).perform();
    }

    /**
     * Move to a WebElement specified by the passed Xpath and attempt two single clicks using Actions.
     * <p>This can be used if the Actions doubleClick() method does not work.
     * <p>Firstly, note that all Exceptions are passed up to the calling method.
     * <p>Secondly, the method may silently do nothing. This usually occurs when the Element
     * to double-click is not the correct one to respond to the action.
     * @param elementXpath the XPath of a WebElement to move to and double-click
     */
    public static void singleClickTwice(String elementXpath) {
    	WebElement element = Browser.driver.findElement(By.xpath(elementXpath));
    	singleClickTwice(element);
    }

    /**
     * Move to a WebElement and attempt a double-click using Actions.
     * <p>Firstly, note that all Exceptions are passed up to the calling method.
     * <p>Secondly, the method may silently do nothing. This usually occurs when the Element
     * to double-click is not the correct one to respond to the action.
     * @param element the WebElement to move to and double-click
     */
    public static void doubleClick(WebElement element){
    	Actions actions = new Actions(Browser.driver);
    	actions.moveToElement(element);
    	actions.click(element).click(element).perform();
    }

    /**
     * Move to a WebElement specified by the passed Xpath and attempt a double-click using Actions.
     * <p>Firstly, note that all Exceptions are passed up to the calling method.
     * <p>Secondly, the method may silently do nothing. This usually occurs when the Element
     * to double-click is not the correct one to respond to the action.
     * @param elementXpath the XPath of a WebElement to move to and double-click
     */
    public static void doubleClick(String elementXpath) {
    	WebElement element = Browser.driver.findElement(By.xpath(elementXpath));
    	singleClickTwice(element);
    }

	/**
	 * Some WebElements misbehave when trying to use sendKeys(), and instead<br>
	 * generate an error similar to <p>
	 * <code>org.openqa.selenium.WebDriverException: unknown error: cannot focus element</code><br>
	 *
	 * In these circumstances, using Actions may work better.
	 *
	 * <p>NOTE that sending Ctrl+a to "select all" has issues for chromedriver.exe since 2021
	 * Instead, create the Actions outside this code, and call addSelectAllToActions
	 *
	 * @param element the WebElement to sendkeys to.
	 * @param keysToSend the keys to send.
	 */
	public static void moveToAndSendKeysUsingActions(WebElement element, String keysToSend) {
		Actions actions = new Actions(Browser.driver);
		actions.moveToElement(element);
		actions.click();
		actions.sendKeys(keysToSend);
		actions.build().perform();
	}

	/**
	 * Adds the Actions step to emulate a Ctrl+a to "select all".
	 * <p>Since the version of chromedriver.exe dating from mid-2020,
	 * there are issues trying to use actions to
	 * simulate Ctrl+a by using Keys.chord(Keys.CONTROL,"a").
	 * <br>This approach can be used instead.
	 * @param actions the Actions object to have a 'select all' added to the list of commands
	 * @return the original Actions object (with the added 'select all' commands)
	 */
	public static Actions addSelectAllToActions(Actions actions) {
		actions.keyDown(Keys.CONTROL);
		actions.sendKeys("a");
		actions.keyUp(Keys.CONTROL);
		return actions;
	}

    /**
     * Emulates hitting the Escape key.
     */
    public static void hitEscapeKey() {
    	Actions action = new Actions(Browser.driver);
    	action.sendKeys(Keys.ESCAPE);
		action.build().perform();
    }

    /**
     * Emulates hitting the Enter key.
     */
    public static void hitEnterKey() {
    	Actions action = new Actions(Browser.driver);
    	action.sendKeys(Keys.ENTER);
    	action.build().perform();
    }

    /**
     * Attempts for a number of times to send keys to an Element (such as a Text box).
     * <p>NOTE this also sends a Keys.TAB at the end of the String.
     * Beware that this may not always be desirable!
     * @param element the WebElement to receive the keys
     * @param stringToSend the String to send to the WebElement.
     * @throws InvalidElementStateException if the element cannot be sent keys after the max wait time.
     */
    public static void sendKeysSafe(WebElement element, String stringToSend) {
    	if (stringToSend == null || stringToSend.isEmpty()) return;
        final int maxTries = 8;
        for (int count = 0; count < maxTries; count++){
            try {
                WaitUtilities.waitForWebElementToBeClickable(element, 8);
                element.sendKeys(stringToSend);
                element.sendKeys(Keys.TAB);
                break;
            }
            catch (InvalidElementStateException e) {
            	if (count == maxTries-1)
            		throw e;
            }

            WaitUtilities.sleepMillis(250);
        }
    }

    /**
     * Interrogates an array of WebElements to determine the first which has text, and return that text.
     * <p>This can be used when a similar field is being accessed, but using different roles - in which case we might not know the correct version of XPath
     * to obtain the desired value.
     * @param elements and array of WebElements to be interrogated.
     * @return the text from the first element which has some.
     */
    public static String getTextFromAvailableElements(WebElement... elements) {
    	if (elements == null || elements.length == 0) return "";
    	String text = null;
    	for (WebElement element : elements) {
    		try {
				text = element.getText();
				return text;
			} catch (@SuppressWarnings("unused") Exception e) {
				continue;
			}
    	}
    	if (text == null) throw new RuntimeException("No passed elements were available to retrive the text from");
    	return null;
    }


    /**
     * Interrogates an array of WebElements to determine the first which is visible, and return that element.
     * <p>Tha calling code must deal with a null WebElement returned.
     * @param elements List of Webelements to be interrogated.
     * @return the first one which is visible, or null otherwise.
     */
    public static WebElement getFirstVisibleElement(List<WebElement> elements) {
    	if (elements == null || elements.isEmpty()) return null;
    	for (WebElement element : elements) {
    		try {
				if (element.isDisplayed())
					return element;
			} catch (@SuppressWarnings("unused") Exception e) {
				continue;
			}
    	}
    	return null;
    }

    /**Wait to be clickable then clicks by locator
     * @param locator the By Locator of the element to click
     */
    public static void clickByLocator(By locator) {
    	WaitUtilities.waitForWebElementToBeClickableByLocator(locator, 15);
    	WaitUtilities.waitUntilClick(locator, 5);
    }

}
