package com.sse.utilities.development;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.sse.utilities.Browser;
import com.sse.utilities.WaitUtilities;

/**
 * A class to output information on WebElement objects - for debugging only.
 *
 * @author atul
 */
public class WebElementDebug {

	/**
	 * Switch to determine whether an attempt is made to output all style
	 * information. Generates a lot of extra output - potentially!
	 */
	public static boolean CSS_OUTPUT = false;

	/**
	 * General switch to determine whether output should be generated at all. If
	 * this is false, then all output is bypassed (even if CSS_OUTPUT is true). This
	 * prevents clogging of the console and speeds up execution. Set to true when
	 * debugging specific elements during development.
	 */
	public static boolean OUTPUT = true;

	/**
	 * Calls dumpWebElementToConsole a number of time with a sleep in between each call.
	 * <p>This is very useful if an element has some kind of unknown state change after a while and you wish to try to investigate what it is.
	 * <p>An example might be a button which is visible but for which the value of isEnabled changes from false to true after a while.
	 * @param numberOfTimes how many times to call dumpWebElementToConsole
	 * @param milliSecondsBetween the number of milliseconds to wait in between each invocation
	 * @param element the WebElement to display information about
	 * @param friendlyDescription the description to appear in the output
	 * @see #dumpWebElementToConsole
	 */
	public static void dumpWebElementToConsoleAFewTimes(int numberOfTimes, long milliSecondsBetween,
			WebElement element, String friendlyDescription) {
		for (int i=0; i < numberOfTimes; i++) {
			dumpWebElementToConsole(element, friendlyDescription + " (#" + i + ")");
			WaitUtilities.sleepMillis(milliSecondsBetween);
		}
	}

	/**
	 * Outputs details of a WebElement to the console with a friendly description to
	 * describe what element is being interrogated. This method will not generate
	 * output if OUTPUT is set to false.
	 * <p>This will catch and output any exceptions thrown during the interrogation.
	 *
	 * @param element
	 *            the WebElement to display information about
	 * @param friendlyDescription
	 *            the description to appear in the output
	 */
	public static void dumpWebElementToConsole(WebElement element, String friendlyDescription) {
		if (!OUTPUT)
			return;
		outputToConsole("\n DUMP OF WEBELEMENT " + friendlyDescription);
		String subTitle;

		/////////////////////////////////////////
		subTitle = "element.getLocation";
		try {
			outputToConsole("Value of " + subTitle + " = " + element.getLocation());
		} catch (Exception e) {
			outputException(subTitle, e);
		}

		/////////////////////////////////////////
		subTitle = "element.getRect";
		try {
			outputToConsole("Value of element.getRect.height = " + element.getRect().getHeight());
			outputToConsole("Value of element.getRect.width = " + element.getRect().getWidth());
			outputToConsole("Value of element.getRect.x = " + element.getRect().getX());
			outputToConsole("Value of element.getRect.y = " + element.getRect().getY());
		} catch (Exception e) {
			outputException(subTitle, e);
		}
		/////////////////////////////////////////
		subTitle = "element.getSize";
		try {
			outputToConsole("Value of " + subTitle + " = " + element.getSize());
		} catch (Exception e) {
			outputException(subTitle, e);
		}




		/////////////////////////////////////////
		subTitle = "element.getText";
		try {
			outputToConsole("Value of " + subTitle + " = " + element.getText());
		} catch (Exception e) {
			outputException(subTitle, e);
		}

		/////////////////////////////////////////
		subTitle = "element.getTagName";
		try {
			outputToConsole("Value of " + subTitle + " = " + element.getTagName());
		} catch (Exception e) {
			outputException(subTitle, e);
		}

		/////////////////////////////////////////
		subTitle = "element.isDisplayed";
		try {
			outputToConsole("Value of " + subTitle + " = " + element.isDisplayed());
		} catch (Exception e) {
			outputException(subTitle, e);
		}

		/////////////////////////////////////////
		subTitle = "element.isEnabled";
		try {
			outputToConsole("Value of " + subTitle + " = " + element.isEnabled());
		} catch (Exception e) {
			outputException(subTitle, e);
		}

		/////////////////////////////////////////
		subTitle = "element.isSelected";
		try {
			outputToConsole("Value of " + subTitle + " = " + element.isSelected());
		} catch (Exception e) {
			outputException(subTitle, e);
		}

		/////////////////////////////////////////
		subTitle = "ExpectedConditions.elementToBeClickable (If Element is output, then this is true)";
		try {
			outputToConsole("Value of " + subTitle + " = " + ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			outputException(subTitle, e);
		}

		/////////////////////////////////////////
		subTitle = "ExpectedConditions.visibilityOf (If Element is output, then this is true)";
		try {
			outputToConsole("Value of " + subTitle + " = " + ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			outputException(subTitle, e);
		}

		/////////////////////////////////////////
		subTitle = "Element Attributes";
		try {
			JavascriptExecutor executor = (JavascriptExecutor) Browser.driver;
			Object aa = executor.executeScript(
					"var items = {}; " + "for (index = 0; index < arguments[0].attributes.length; ++index) { " +
					// "items[arguments[0].attributes[index].name] =
					// arguments[0].attributes[index].value };" +
							"items[arguments[0].attributes[index].name] = ''};" + " return items;",
					element);
			String[] attributes = aa.toString().split("\\W"); // Split at any non word.
			for (String attribute : attributes) {
				if (!attribute.isEmpty()) {
					outputToConsole("Value of attribute " + attribute + " = " + element.getAttribute(attribute));
				}
			}
		} catch (Exception e) {
			outputException(subTitle, e);
		}

		/////////////////////////////////////////
		subTitle = "element.getAttribute('value') ----- not always visible from all attributes....";
		try {
			outputToConsole("Value of " + subTitle + " = " + element.getAttribute("value"));
		} catch (Exception e) {
			outputException(subTitle, e);
		}

		/////////////////////////////////////////
		if (CSS_OUTPUT) {
			subTitle = "Element Style components";
			try {
				JavascriptExecutor executor = (JavascriptExecutor) Browser.driver;
				Object aa = executor.executeScript("var s = '';" + "var o = getComputedStyle(arguments[0]);"
						+ "for(var i = 0; i < o.length; i++){" +
						// "s+=o[i] + ':' + o.getPropertyValue(o[i])+';';}" +
						"s+=o[i] + '¦';}" + "return s;", element);
				// System.out.println("s = " + aa);
				String[] attributes = aa.toString().split("¦", 0); // Split at any non word.
				for (String attribute : attributes) {
					if (!attribute.isEmpty()) {
						outputToConsole("Value of css value " + attribute + " = " + element.getCssValue(attribute));
					}
				}
			} catch (Exception e) {
				outputException(subTitle, e);
			}
		}

		outputToConsole("END OF DUMP OF WEBELEMENT " + friendlyDescription + "\n");
	}

	/**
	 * Outputs details of a WebElement's "text" to the console with a friendly
	 * description to describe what element is being interrogated. <br>
	 * This method will not generate output if OUTPUT is set to false.
	 *
	 * @param element
	 *            the WebElement to display information about
	 * @param friendlyDescription
	 *            the description to appear in the output
	 */
	public static void dumpWebElementTextToConsole(WebElement element, String friendlyDescription) {
		if (!OUTPUT)
			return;
		try {
			outputToConsole(friendlyDescription + " text = <" + element.getText() + ">");
		} catch (Exception e) {
			outputException(friendlyDescription + " getText", e);
		}
	}

	/**
	 * Given a WebElement, outputs to the console the full Xpath using "numeric" tags.
	 * <p>e.g. /html/body[1]/div[5]/div[1]/section[1]/header[1]/div[2]/div[1]/div[1]
	 * @param element the WebElement to output the Xpath of.
	 * @param friendlyDescription
	 *            the description to appear in the output
	 */
	public static void outputXpathByTagNumbers(WebElement element, String friendlyDescription) {
		if (!OUTPUT)
			return;
		outputToConsole("Xpath (numeric) of WebElement " + friendlyDescription);
		if (element == null) {
			outputToConsole("WebElement is null");
			return;
		}
		outputToConsole(generateXPath(element, ""));
	}

	private static String generateXPath(WebElement childElement, String current) {
		String childTag = childElement.getTagName();
		if (childTag.equals("html")) {
			return "/html"+current;
		}
		WebElement parentElement = childElement.findElement(By.xpath(".."));
		List<WebElement> siblingElements = parentElement.findElements(By.xpath("*"));
		int count = 0;
		for(int i=0; i<siblingElements.size(); i++) {
			WebElement childrenElement = siblingElements.get(i);
			String childrenElementTag = childrenElement.getTagName();
			if (childTag.equals(childrenElementTag)) {
				count++;
			}
			if (childElement.equals(childrenElement)) {
				return generateXPath(parentElement, "/" + childTag + "[" + count + "]" + current);
			}
		}
		return null;
	}

	/**
	 * Loops upwards through the Xpath hierarchy and outputs relevant attributes.
	 * <p>Handy for distinguishing between elements when multiple have been returned by a "Find" in order to pin down the 'correct' one.
	 * @param element the WebElement to investigate.
	 * @param friendlyDescription
	 *            the description to appear in the output
	 */
	public static void outputXpathWithAttributes(WebElement element, String friendlyDescription) {
		if (!OUTPUT)
			return;
		outputToConsole("Xpath (attributes) of WebElement " + friendlyDescription);
		if (element == null) {
			outputToConsole("WebElement is null");
			return;
		}
		WebElement currentElement = element;
		while (true) {
			outputToConsole(constructBasicElementDetails(currentElement));
			if (currentElement.getTagName().equals("html")) break;
			currentElement = currentElement.findElement(By.xpath(".."));
		}
	}

	private static String constructBasicElementDetails(WebElement element) {
		String s = "";
		s += "tagName=" + element.getTagName() + ",";
		s += "id=" + element.getAttribute("id") + ",";
		s += "name=" + element.getAttribute("name") + ",";
		s += "title=" + element.getAttribute("title") + ",";
		s += "class=" + element.getAttribute("class") + ",";
		String text = element.getText().replaceAll("\\R", ","); //remove carriage returns, line feeds etc.
		int maxTextLength = 40;
		if (text.length() > maxTextLength) text = text.substring(0, maxTextLength) + "...";
		s += "text=" + text + ",";
		return s;
	}


	private static void outputToConsole(Object o) {
		System.out.println(new Date() + " : " + o + "");
	}

	private static void outputException(String subTitle, Exception e) {
		outputToConsole(new Date() + " Exception obtaining " + subTitle + " : " + e.getMessage());
	}
}
