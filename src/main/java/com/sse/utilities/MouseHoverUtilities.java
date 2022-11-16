package com.sse.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * A class to handle some hover-related activities on WebElements
 *
 */
public final class MouseHoverUtilities {

	private static Logger log = LogManager.getLogger(MouseHoverUtilities.class);

	private MouseHoverUtilities() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	/**
	 * Hover over an element and then click it.
	 *
	 * @param elementToHover the element to hover
	 * @param elementToClick the element to click
	 */
	public static void hoverAndClick(WebElement elementToHover, WebElement elementToClick) {
		Actions action = new Actions(Browser.driver);
		action.moveToElement(elementToHover).click(elementToClick).build().perform();
	}

	/**
	 * Hover over an element
	 *
	 * @param element the element
	 */
	public static void hover(WebElement element) {
		Actions action = new Actions(Browser.driver);
		action.moveToElement(element).build().perform();

	}

	/**
	 * Hovers over the element (the one to hover over) after it is visible. And then
	 * clicks the element (the one to click) after making sure it is visible and
	 * clickable. In case a JavascriptException is thrown, then it finds the parent
	 * of element to click and clicks it.
	 * 
	 * @param elementToHover the WebElement to hover over
	 * @param elementToClick the WebElement to click
	 */
	public static void hoverAndSafeClick(WebElement elementToHover, WebElement elementToClick) {
		try {
			Actions action = new Actions(Browser.driver);
			WaitUtilities.waitForElementVisible(elementToHover);
			try {
				action.moveToElement(elementToHover).perform();
				WaitUtilities.waitForElementVisible(elementToClick);
				WaitUtilities.waitForWebElementToBeClickable(elementToClick, 6);  // changed  5 to 6
				action.click(elementToClick).perform();
			} catch (JavascriptException jse) {
				log.warn("JavascriptException caught, will try to click the parent element", jse);
				WebElement parent = elementToClick.findElement(By.xpath(".."));
				parent.click();
			}
		} catch (Exception e) {
			ExceptionHandler.handleException("Issue hover over and clicking", e, MouseHoverUtilities.class);
		}
	}

	/**
	 * That method waits until element is visible and clickable Uses Object click
	 * 
	 * @param elementToHover the WebElement to hover over
	 * @param elementToClick the WebElement to click
	 */
	public static void hoverAndSafeObjectClick(WebElement elementToHover, WebElement elementToClick) {
		try {
			Actions action = new Actions(Browser.driver);
			action.moveToElement(elementToHover).build().perform();
			WaitUtilities.waitForElementVisible(elementToClick);
			WaitUtilities.waitForWebElementToBeClickable(elementToClick, 5);
			action.click(elementToClick).build().perform();
			try {
				WebElementUtilities.objectClick(elementToClick);
			} catch (Exception e) {
				log.warn("Couldnt perform Object click!! " + e);
			}
		} catch (Exception e) {
			ExceptionHandler.handleException("Issue hover over and clicking", e, MouseHoverUtilities.class);
		}
	}

	/**
	 * Right click to element then clicks to option based on param
	 * 
	 * @param rightClickTo right clicks to that element
	 * @param clickTo      clicks to that element after right click
	 */
	public static void rightClick(WebElement rightClickTo, WebElement clickTo) {
		try {
			Actions action = new Actions(Browser.driver);
			action.contextClick(rightClickTo).perform();
			WaitUtilities.waitForElementVisible(clickTo);
			WaitUtilities.waitForWebElementToBeClickable(clickTo, 10);
			WebElementUtilities.objectClick(clickTo);
		} catch (Exception e) {
			ExceptionHandler.handleException("Issue at right click and click to element", e, MouseHoverUtilities.class);
		}
	}

	/**
	 * Right click to element then clicks to option based on param
	 * 
	 * @param rightClickTo right clicks to that element
	 * @param clickTo      find out the WebElement of that param then clicks to it
	 */
	public static void rightClick(WebElement rightClickTo, String clickTo) {
		try {
			String clickToXpath = "//span[text()='" + clickTo + "']";
			Actions action = new Actions(Browser.driver);
			action.contextClick(rightClickTo).perform();
			WaitUtilities.waitForElementVisibleLocated(By.xpath(clickToXpath));
			WebElement clickToElement = Browser.driver.findElement(By.xpath(clickToXpath));
			WaitUtilities.waitForElementVisible(clickToElement);
			WaitUtilities.waitForWebElementToBeClickable(clickToElement, 10);
			WebElementUtilities.objectClick(clickToElement);
		} catch (Exception e) {
			ExceptionHandler.handleException("Issue at right click and click to element", e, MouseHoverUtilities.class);
		}
	}

	/**
	 * The Method first scroll up(-9600) then scroll down 3 times with 1 sec waits
	 * 
	 * @param scrollAmountByStringNumber scroll down amount for each time
	 */
	public static void scrollDown3TimesWithWait(String scrollAmountByStringNumber) {
		JavascriptExecutor js = (JavascriptExecutor) Browser.driver;
		js.executeScript("window.scrollBy(0, -9600)");
		for (int i = 0; i < 3; i++) {
			js.executeScript("window.scrollBy(0, " + scrollAmountByStringNumber + ")");
			WaitUtilities.sleep(1);
		}
	}

}
