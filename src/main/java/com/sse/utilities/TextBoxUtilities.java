package com.sse.utilities;

import org.openqa.selenium.WebElement;
/**
 * A collection of utilities associated with text boxes.
 * @author mitchella3
 */
public final class TextBoxUtilities {

	  private TextBoxUtilities() {
		    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	  }	
	
	/**
	 * Enter text into a text box.
	 * <p>NOTE that if this fails because the field is not user-editable, then consider
	 * WebElementUtilities.moveToAndSendKeysUsingActions instead.
	 * <p>ALSO note that sendKeysSafe also sends a Keys.TAB at the end of the String.
	 * Beware that this may not always be desirable!
	 * @param textboxElement the textbox WebElement
	 * @param textToSend the text to enter
	 */
	public static void enterTextIntoTextbox(WebElement textboxElement, String textToSend) {
    	WaitUtilities.waitForElementToBeClickableSafe(textboxElement,8);
        textboxElement.clear();
        WebElementUtilities.sendKeysSafe(textboxElement, textToSend);
    }

	/**
	 * Enter text into a text box (with no Keys.TAB to "consolidate" the input...)
	 * <p>NOTE that if this fails because the field is not user-editable, then consider
	 * WebElementUtilities.moveToAndSendKeysUsingActions instead.
	 * @param textboxElement the textbox WebElement
	 * @param textToSend the text to enter
	 */
	public static void enterTextIntoTextboxNoTab(WebElement textboxElement, String textToSend) {
		WaitUtilities.waitForElementToBeClickableSafe(textboxElement,8);
		textboxElement.clear();
        WaitUtilities.waitForWebElementToBeClickable(textboxElement, 8);
        if (textToSend != null) {
        	textboxElement.sendKeys(textToSend);
        }
	}

	/**
	 * Enter text into a text box ONE character at a time.
	 * <p>This can workaround the intermittent symptom of Salesforce just ignoring characters - notably email in a new Lead page.
	 * @param textboxElement the textbox WebElement
	 * @param textToSend the text to enter
	 */
	public static void enterTextIntoTextboxOneCharAtATime(WebElement textboxElement, String textToSend) {
    	WaitUtilities.waitForElementToBeClickableSafe(textboxElement,8);
    	textboxElement.clear();
    	if (textToSend != null) {
		    for (int i = 0; i < textToSend.length(); i++) {
		    	WaitUtilities.sleepMillis(100);
		    	textboxElement.sendKeys(Character.toString(textToSend.charAt(i)));
		    }
    	}
	}

}
