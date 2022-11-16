package com.sse.utilities.development;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.sse.utilities.Browser;

/**
 * A class to help <b>during development only</b> to test XPaths, IDs etc....
 * @author mitchella3
 */
public final class TestElementFinding {

	private TestElementFinding() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}	
	
	/**
	 * Method for testing the results of XPaths.<br>
	 * If an Exception occurs (e.g. invalid Xpath expressions),
	 * this is caught and a stacktrace output to the console.<br>
	 * If the Xpath is valid, a count of the number of elements found is output.<br>
	 * If at least one is found, for each, the element is dumped to the console
	 * using {@link WebElementDebug#dumpWebElementToConsole}.
	 * @param xpath the XPath String to test.
	 */
	public static void testXpath(String xpath) {
		displayResultOfSearchAttempt(xpath, "xpath");
	}

	/**
	 * As textXPath, but using ID String to find element(s).
	 * @param id the ID String to test.
	 */
	public static void testId(String id) {
		displayResultOfSearchAttempt(id, "id");
	}

	/**
	 * As textXPath, but using CSS String to find element(s).  e.g. #ngProgressBar
	 * @param css the CSS String to test.
	 */
	public static void testCSS(String css) {
		displayResultOfSearchAttempt(css, "css");
	}

	/**
	 * Method for just displaying the count of found elements using the passed XPath.<br>
	 * If an Exception occurs (e.g. invalid Xpath expressions),
	 * this is caught and a stacktrace output to the console.<br>
	 * If the Xpath is valid, a count of the number of elements found is output.<br>
	 * @param xpath the XPath String to test.
	 * @return the count of the number of elements
	 */
	public static int testXpathCount(String xpath) {
		List<WebElement> elements = Browser.driver.findElements(By.xpath(xpath));
		System.out.println((elements.size()) + " elements found using xpath " + xpath);
		return elements.size();
	}

	/**
	 * As textXPathCount, but using ID String to find element(s).
	 * @param id the ID String to test.
	 * @return the count of the number of elements
	 */
	public static int testIdCount(String id) {
		List<WebElement> elements = Browser.driver.findElements(By.id(id));
		System.out.println((elements.size()) + " elements found using id " + id);
		return elements.size();
	}

	/**
	 * As textXPathCount, but using CSS String to find element(s).  e.g. #ngProgressBar
	 * @param css the CSS String to test.
	 * @return the count of the number of elements
	 */
	public static int testCSSCount(String css) {
		List<WebElement> elements = Browser.driver.findElements(By.cssSelector(css));
		System.out.println((elements.size()) + " elements found using css " + css);
		return elements.size();
	}

	/**
	 * @param searchString the Xpath or Id (or other) String mechanism of trying to find element(s).
	 * @param method - the approach to be tested, e.g. "xpath" or "id".
	 */
	private static void displayResultOfSearchAttempt(String searchString, String method) {
		List<WebElement> elements = null;
		try {
			switch (method) {
			case "xpath":
				elements = Browser.driver.findElements(By.xpath(searchString));
				break;
			case "id":
				elements = Browser.driver.findElements(By.id(searchString));
				break;
			case "css":
				elements = Browser.driver.findElements(By.cssSelector(searchString));
				break;
			default :
				System.out.println("METHOD " + method + " NOT YET IMPLEMENTED");
				return;
			}
			int size;
			System.out.println((size=elements.size()) + " elements found using " + method + " " + searchString);
			int i=1;
			for (WebElement element : elements) {
				WebElementDebug.dumpWebElementToConsole(element, searchString + "    (instance " + i + " of " + size + ")");
				WebElementDebug.outputXpathByTagNumbers(element, searchString + "    (instance " + i + " of " + size + ")");
				WebElementDebug.outputXpathWithAttributes(element, searchString + "    (instance " + i + " of " + size + ")");
				i++;
			}
		}
		catch (Exception e) {
			System.out.println("Exception thrown using " + method + " " + searchString + "\n" + e);
			e.printStackTrace();
		}
	}

	/**
	 * Method for testing the results of XPaths. This is designed for a developer to pick and choose
	 * relevant items/methods to display depending on their needs, some from this class and optionally, some
	 * from WebElementDebug.<br>
	 * If an Exception occurs (e.g. invalid Xpath expressions),
	 * this is caught and a stacktrace output to the console.<br>
	 * @param xpath the XPath String to test.
	 */
	public static void testXpathCustom(String xpath) {
		List<WebElement> elements;
		try {
			elements = Browser.driver.findElements(By.xpath(xpath));
			int size = elements.size();
			System.out.println(size + " elements found using Xpath: " + xpath);
			int i=1;
			for (WebElement element : elements) {
				WebElementDebug.outputXpathByTagNumbers(element, xpath + "    (instance " + i + " of " + size + ")");

				
				System.out.println("Value of CSS width = " + element.getCssValue("width"));
				System.out.println("Value of width = " + element.getSize().getWidth());
				System.out.println("Value of getText = " + element.getText());
				System.out.println("Value of isDisplayed = " + element.isDisplayed());
				System.out.println("Value of isEnabled = " + element.isEnabled());
				i++;
			}
		}
		catch (Exception x) {
			x.printStackTrace();
		}
	}

	/**
	 * This can be used by any developer to put random code in to aid testing or development in the area of exploring how to find elements etc....
	 * <br>A good reason might be to test several Xpaths in several places within the code, and this method avoids
	 * copy/pasting/changing multiple sets of code in multiple places.....
	 * 
	 */
	public static void testThings() {
		try {
//			TestElementFinding.testCSSCount("#ngProgress");
//			TestElementFinding.testXpathCustom("loading-indicator//div[@id='ngProgress']");
//			TestElementFinding.testXpathCustom("cart-grid//div[@id='ngProgress']");
			
			int left = TestElementFinding.testXpathCount("//progress-bar/div/p[@id='progress-bar-left']");
			int right = TestElementFinding.testXpathCount("//progress-bar/div/p[@id='progress-bar-right']");
			if (left > 0) {
				WebElement w = Browser.driver.findElement(By.xpath("//progress-bar/div/p[@id='progress-bar-left']"));
				System.out.println("Value of progress-bar-left text = " + w.getText());
			}
			if (right > 0) {
				WebElement w = Browser.driver.findElement(By.xpath("//progress-bar/div/p[@id='progress-bar-right']"));
				System.out.println("Value of progress-bar-left right = " + w.getText());
			}
			TestElementFinding.testXpathCustom("//progress-bar/div[@id='ngProgress-container']/div[@id='ngProgress']");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
