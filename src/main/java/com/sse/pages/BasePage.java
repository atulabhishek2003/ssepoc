package com.sse.pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.sse.utilities.AssertLogger;
import com.sse.utilities.Browser;
import com.sse.utilities.ExceptionHandler;
import com.sse.utilities.StringUtilities;
import com.sse.utilities.WaitUtilities;

/**
 * This is the superclass of all Page objects and contains common initialisation
 * code.
 *
 * @author atul
 */
public abstract class BasePage {
	private static Logger log = LogManager.getLogger(BasePage.class);

	protected static final String BEGIN = "//div[@class='windowViewMode-normal oneRecordActionWrapper isModal active lafPageHost']//label[text()='";
	protected static final String END = "']/..//input";

	/**
	 * In Lightning, the start of the XPath to find the value of a standard text
	 * field in a read-only page, given its "label".
	 */
	protected static final String TEXT_VALUE_BEGIN = "//div/div/div/span[text()='";
	/**
	 * In Lightning, the end of the XPath to find the value of a text-format field
	 * in a read-only page, given its "label".
	 */
	protected static final String TEXT_VALUE_END = "']/../../div[2]/span//*[@data-output-element-id='output-field']";

	/**
	 * In Lightning, the end of the XPath to find the value of a number-format field
	 * in a read-only page, given its "label".
	 */
	protected static final String TEXT_VALUE_END_NUMBER = "']/../../div[2]/span//lightning-formatted-number";

	/**
	 * In Lightning, the end of the XPath to find the value of a name-format field
	 * in a read-only page, given its "label".
	 */
	protected static final String TEXT_VALUE_END_NAME = "']/../../div[2]/span//lightning-formatted-name";
	/**
	 * In Lightning, the end of the XPath to find the value of text field like phone
	 * number which looks like link
	 */
	protected static final String TEXT_VALUE_WITH_LINK_END = "']/../../div[2]/span//a";

	/**
	 * In Lightning, the start of the XPath to find the value of a standard text
	 * field in a read-only page, given its "label".
	 */
	protected static final String TEXT_VALUE_BEGIN_MODAL = "//div[@class='modal-container slds-modal__container']//span[text()='";
	/**
	 * In Lightning, the end of the XPath to find the value of a standard text field
	 * in a read-only page, given its "label".
	 */
	protected static final String TEXT_VALUE_END_MODAL = "']/../following-sibling::div";
	/**
	 * In Lightning, the start of the XPath to find the value of a text field which
	 * has a 'Change button' in a read-only page, given its "label".
	 * <p>
	 * An example is the Record Type field in an Invoice object.
	 */
	protected static final String CHANGEABLE_TEXT_VALUE_BEGIN = "//div/div/div/span[text()='";
	/**
	 * In Lightning, the end of the XPath to find the value of a text field which
	 * has a 'Change button' in a read-only page, given its "label".
	 */
	protected static final String CHANGEABLE_TEXT_VALUE_END = "']/../../div[2]/span//div/span";
	/**
	 * In Lightning, the start of the XPath to find the value of a text field with a
	 * link in a read-only page, given its "label".
	 */
	protected static final String LINK_VALUE_BEGIN = "//div/div/div/span[text()='";
	/**
	 * In Lightning, the end of the XPath to find the value of a text field with a
	 * link in a read-only page, given its "label".
	 */
	protected static final String LINK_VALUE_END = "']/../../div[2]//div//a";
	/**
	 * In Lightning, the start of the XPath to find the value of a text field with a
	 * link in a read-only page, given its "label".
	 */
	protected static final String LINK_VALUE_WITH_IMAGE_BEGIN = "//div/div/div/span[text()='";
	/**
	 * link in a read-only page, given its "label". In Lightning, the end of the
	 * XPath to find the value of a text field with a
	 */
	protected static final String LINK_VALUE_WITH_IMAGE_END = "']/../../div[2]//div//a";
	/**
	 * In Lightning, the start of the XPath to find the value of a check box field
	 * in a read-only page, given its "label".
	 */
	protected static final String CHECKBOX_VALUE_BEGIN = "//span[@class='slds-form-element__label slds-assistive-text'][contains(text(),'";
	/**
	 * In Lightning, the end of the XPath to find the value of a check box field in
	 * a read-only page, given its "label".
	 */
	protected static final String CHECKBOX_VALUE_END = "')]/../..//label/span[@class='slds-checkbox_faux']";

	/**
	 * In Lightning, the start of the XPath to find the "title" breadcrumb of the
	 * current page.
	 */
	private static final String TITLE_BEGIN = "//div[@class='windowViewMode-normal oneContent active lafPageHost']//h1/div[text()='";
	/**
	 * In Lightning, the end of the XPath to find the "title" breadcrumb of the
	 * current page.
	 */
	private static final String TITLE_END = "']";

	/**
	 * In Lightning, the start of the XPath to find the "title" breadcrumb of the
	 * current List page (e.g. Cases).
	 */
	private static final String LIST_TITLE_BEGIN = "//lst-breadcrumbs//div//span[text()='";
	/**
	 * In Lightning, the end of the XPath to find the "title" breadcrumb of the
	 * current List page (e.g. Cases).
	 */
	private static final String LIST_TITLE_END = "']";

	/**
	 * In Lightning, the start of the XPath to find the input text field in an
	 * editable page, given its "label".
	 */
	protected static final String TEXT_EDIT_BEGIN = "//label[@data-aura-class='uiLabel']//span[text()='";
	/**
	 * Salesforce release change, Xpaths changed on Lead, Opty and contacts edit
	 * page layouts
	 */
	protected static final String TEXT_BEGIN = "//label[@class='slds-form-element__label slds-no-flex'][contains(text(),'";
	/**
	 * In Lightning, the end of the XPath to find the input text field in an
	 * editable page, given its "label".
	 */
	protected static final String TEXT_EDIT_END = "']/../..//input";
	/**
	 * Salesforce release change, Xpaths changed on Lead, Opty and contacts edit
	 * page layouts
	 */
	protected static final String TEXT_END = "')]/../..//input";

	/**
	 * In Lightning, the start of the XPath to find the input text field in an
	 * editable page, given its "label".
	 */
	protected static final String DATE_EDIT_BEGIN = "//label[@data-aura-class='uiLabel']//span[text()='";
	/**
	 * In Lightning, the end of the XPath to find the input text field in an
	 * editable page, given its "label".
	 */
	protected static final String DATE_EDIT_END = "']/../..//input";
	/**
	 * Salesforce release change, Xpaths changed on Lead, Opty and contacts edit
	 * page layouts
	 */
	protected static final String DATE_BEGIN = "//label[@class='slds-form-element__label'][text()='";
	/**
	 * Salesforce release change, Xpaths changed on Lead, Opty and contacts edit
	 * page layouts
	 */
	protected static final String DATE_END = "']/..//input";
	/**
	 * In Lightning, the start of the XPath to find the input text field in an
	 * editable page, given its "label".
	 */
	protected static final String TEXT_AREA_EDIT_BEGIN = "//label[@data-aura-class='uiLabel']//span[text()='";
	/**
	 * Salesforce release change, Xpaths changed on Lead, Opty and contacts edit
	 * page layouts
	 */
	protected static final String TEXT_AREA_BEGIN = "//*[@class='slds-form-element__label'][contains(text(),'";

	/**
	 * In Lightning, the end of the XPath to find the input text field in an
	 * editable page, given its "label".
	 */
	protected static final String TEXT_AREA_EDIT_END = "']/../..//textarea";
	/**
	 * Salesforce release change, Xpaths changed on Lead, Opty and contacts edit
	 * page layouts
	 */
	protected static final String TEXT_AREA_END = "')]/../..//textarea";

	/**
	 * In Lightning, the start of the XPath to find the input text field in an
	 * editable page, given its "label".
	 */
	// protected static final String LOOKUP_EDIT_BEGIN =
	// "//label[@data-aura-class='uiLabel']//span[text()='";
	protected static final String LOOKUP_EDIT_BEGIN = "//label[contains(text(),'";
	/**
	 * In Lightning, the end of the XPath to find the input text field in an
	 * editable page, given its "label".
	 */
//	protected static final String LOOKUP_EDIT_END = "']/../..//input";
	protected static final String LOOKUP_EDIT_END = "')]/../..//input";
	/**
	 * In Lightning, the start of the XPath to find the input check box field in an
	 * editable page, given its "label".
	 */
	protected static final String CHECKBOX_EDIT_BEGIN = "//label[@class='slds-checkbox__label']//span[text()='"; // Updated during implementing changes came with Apttus Upgrade (Feb, 2022) 
	/**
	 * In Lightning, the end of the XPath to find the input check box field in an
	 * editable page, given its "label".
	 */
	protected static final String CHECKBOX_EDIT_END = "']/../..//input";

	/**
	 * In Lightning, the start of the XPath to find the input check box field in an
	 * editable page, given its "label".
	 */
	protected static final String RADIO_EDIT_BEGIN = "//label[@class='slds-radio']//span[text()='";
	/**
	 * In Lightning, the end of the XPath to find the input check box field in an
	 * editable page, given its "label".
	 */
	protected static final String RADIO_EDIT_END = "']/../../div/input";
	/**
	 * In Lightning, the start of the XPath to find the drop down field in an
	 * editable page, given its "label".
	 */
	protected static final String DROPDOWN_EDIT_BEGIN = "//span[@data-aura-class='uiPicklistLabel']//span[text()='";
	/**
	 * Salesforce release change, Xpaths changed on Lead, Opty and contacts edit
	 * page layouts
	 */

	protected static final String DROPDOWN_BEGIN = "//*[@class='slds-form-element__label'][contains(text(),'";

	/**
	 * In Lightning, the end of the XPath to find the drop down field field in an
	 * editable page, given its "label".
	 */
	protected static final String DROPDOWN_EDIT_END = "']/../..//a";
	/**
	 * Salesforce release change, Xpaths changed on Lead, Opty and contacts edit
	 * page layouts
	 */
	protected static final String DROPDOWN_END = "')]";  //deleted input (Jan2022 fix)


	/**
	 * In Lightning, the XPath for the "New" button in 'list' pages, e.g.
	 * Leads.page, OpportunitiesPage etc.
	 */

	protected static final String NEW_BUTTON_IN_LIST_PAGES_XPATH = "//div[@class='windowViewMode-maximized oneContent active lafPageHost' or @class='windowViewMode-normal oneContent active lafPageHost']//div[@title='New']";
	@FindBy(xpath = NEW_BUTTON_IN_LIST_PAGES_XPATH)
	protected WebElement newButtonInListPage;

	protected static final String MODAL_WINDOW_EDIT_BUTTON_XPATH = "//div[@class='windowViewMode-maximized oneContent active lafPageHost' or @class='windowViewMode-normal oneContent active lafPageHost']//button[text()='Edit']";
	@FindBy(xpath = MODAL_WINDOW_EDIT_BUTTON_XPATH)
	protected WebElement editButtonModalWindow;
	/**
	 * In Lightning, the XPath for the "List View Controls" button in 'list' pages,
	 * e.g. Leads.page, OpportunitiesPage etc.
	 * <p>
	 * Currently only used for confirming arrival on the list pages as this should
	 * always be there (unlike the New button which is user-dependant).
	 */

	protected static final String LIST_VIEW_CONTROL_BUTTON_IN_LIST_PAGES_XPATH = "//div[@class='windowViewMode-maximized oneContent active lafPageHost' or @class='windowViewMode-normal oneContent active lafPageHost']//button[@title='List View Controls']";

	// @FindBy(xpath = LIST_VIEW_CONTROL_BUTTON_IN_LIST_PAGES_XPATH)
//	protected WebElement listViewControlButtonInListPage;

	/*
	 * Fields for handling 'Related tab' items.
	 */
	/**
	 * Standard Lightning Salesforce pages have three tabs - Details, Related, and
	 * Chatter.
	 * <p>
	 * This Xpath points to the "Related" tab allowing functionality to click it
	 * when required.
	 * <p>NOTE, after the Related tab has been clicked, the Xpath below will not work since the 'li' class has extra attributes (i.e. 'class=slds-tabs_default__item slds-is-active slds-has-focus')
	 * so the XPath only relates to a clickable related tab when it is not already active.
	 */
	protected static final String RELATED_TAB_XPATH = 
			"//div[@class='windowViewMode-normal oneContent active lafPageHost']//lightning-tab-bar/ul/li[@class='slds-tabs_default__item']/a[text()='Related']";
	@FindBy(xpath = RELATED_TAB_XPATH)
	protected WebElement relatedTab;

	protected static final String DROPDOWN_ARROW_XPATH = "//div[@class='windowViewMode-normal oneContent active lafPageHost']//lightning-button-menu[contains(@class,'menu')]//button";
	@FindBy(xpath = DROPDOWN_ARROW_XPATH)
	protected WebElement dropDownArrow;

	/**
	 * Standard Lightning Salesforce pages have three tabs - Details, Related, and
	 * Chatter.
	 * <p>
	 * This Xpath points to the "Details" tab allowing functionality to click it
	 * when required.
	 */
	protected static final String DETAILS_TAB_XPATH = 
			"//a[normalize-space()='Details']";	
//			"//div[@class='windowViewMode-normal oneContent active lafPageHost']//lightning-tab-bar/ul/li[@class='slds-tabs_default__item']/a[text()='Details']";
//			"//div[contains(@class,'slds-col slds-size_1-of-1 row')]//a[text()='Details']"; 
	@FindBy(xpath = DETAILS_TAB_XPATH)
	protected WebElement detailsTab;

	/**
	 * In Lightning, the start of the XPath to find the header label of a related
	 * list in the Related Tab of a Salesforce object.
	 * <p>
	 * Public visibility as it is used by utilities.
	 */
	// public static final String RELATED_TAB_HEADING_BEGIN =
	// "//div[@class='windowViewMode-normal oneContent active
	// lafPageHost']//div/article/div/header/div[@class='slds-media__body']/h2/a/span[@title='";
	// public static final String RELATED_TAB_HEADING_BEGIN =
	// "//*[@class='slds-truncate slds-m-right--xx-small'][@title='";
	public static final String RELATED_TAB_HEADING_BEGIN = "//div[@class='windowViewMode-normal oneContent active lafPageHost']//h2/a/span[@title='";

	/**
	 * In Lightning, the start of the XPath to find the header label of a related
	 * list in the Related Tab of a Salesforce object.
	 * <p>
	 * Public visibility as it is used by utilities. *
	 */
	public static final String RELATED_TAB_HEADING_END = "']";
	/**
	 * In Lightning, the end of the XPath to find the count of elements in a related
	 * list in the Related Tab of a Salesforce object.
	 * <p>
	 * Public visibility as it is used by utilities. *
	 */
	public static final String RELATED_TAB_HEADING_COUNT = "']/../span[2]";
	/**
	 * In Lightning, the start of the XPath to find the value(s) of links to related
	 * items in the Related Tab of a Salesforce object.
	 * <p>
	 * Public visibility as it is used by utilities.
	 */
	public static final String RELATED_TAB_VALUE_BEGIN = "//div//article//h2/a/span[@title='";
	/**
	 * In Lightning, the end of the XPath to find the value(s) of links to related
	 * items in the Related Tab of a Salesforce object.
	 * <p>
	 * Public visibility as it is used by utilities.
	 */
	// public static final String RELATED_TAB_VALUE_END =
	// "']/../../../../../..//tbody//th//a[1]";
	public static final String RELATED_TAB_VALUE_END = "']/../../../../../../../../..//tbody//th//a[1]";
	/**
	 * In some pages, there is a 'Classic-style' iframe - e.g. Generate Order Form
	 * page from a Quote/Proposal page. This allows the code to switch to that
	 * iframe.
	 */
	public static final String CLASSIC_IFRAME_XPATH = "//div[@class='windowViewMode-normal oneContent active lafPageHost']//iframe";

	/**
	 * Toast Message
	 */
	public static final String TOASTMESSAGE = "//span[contains(@class,'toastMessage')]";

	/**
	 * Error Message when there is no access
	 */
	public static final String ERRORMESSAGE = "(//flexipage-error//p)[1]";

	/**
	 * Constructor. Initialises the WebElements in the current Page.
	 */
	public BasePage() {
		PageFactory.initElements(Browser.driver, this);
	}

	/*
	 * Only needs implementing for 'real' pages, i.e. not for the navigation and
	 * search pane (which is already 'navigated to' as it's on most pages anyway)
	 */

	/**
	 * Confirm that we have arrived at the desired page.<br>
	 */
	public abstract void checkArrivalOnPage();

	/**
	 * Extract the Salesforce object id from the URL.<br>
	 * For "Lightning" Salesforce pages, the URL will look like (e.g.)
	 * "https://rbi--unitysit.lightning.force.com/lightning/r/Account/0019E00000rrJBNQA2/view"
	 * and we can call a utility to extract it.<br>
	 *
	 * @return the Object ID of the current Salesforce object (from the URL).
	 */
	protected String getObjectID() {
		String url = Browser.url();
		log.debug("URL = " + url);
		return StringUtilities.getSalesforceObjectIDFromLightningURL(url);
	}

	/**
	 * Stores information visible from the current page into variables for use by
	 * later steps.
	 * <p>
	 * It retries up to three times just in case of StaleElementReferenceExceptions.
	 */
	public void storeDetailsWithRetries() {

		int retryCount = 0;
		int maxRetryCount = 3;
		try {
			while (retryCount < maxRetryCount) {
				try {
					log.info(getClass() + " About to store details : try " + (retryCount + 1) + " of " + maxRetryCount);
					WaitUtilities.waitForPageToLoad();
					storeDetails();
					log.info(getClass() + " Successfully stored details on try " + (retryCount + 1) + " of "
							+ maxRetryCount);
					return;
				} catch (StaleElementReferenceException sere) {
					retryCount++;
					log.error(getClass() + " StaleElementReferenceException - Failed to store details on try "
							+ (retryCount) + " of " + maxRetryCount, sere);
					WaitUtilities.sleep(2);
				} catch (NoSuchElementException nsee) {
					retryCount++;
					log.error(getClass() + " NoSuchElementException - Failed to store details on try " + (retryCount)
							+ " of " + maxRetryCount, nsee);
					WaitUtilities.sleep(2);
				}
			}
			throw new RuntimeException("Maximum retry count exceeded storing details");
		} catch (Exception e) {
			ExceptionHandler.handleException("Page not stored", e, this);
		}
	}

	/**
	 * Empty non-abstract implementation of storeDetails - which in subclasses will
	 * store page details.
	 */
	protected void storeDetails() {
		log.warn("*** This method should be overridden in your current page class *** " + this.getClass());
	}

	/**
	 * In Lightning, create the XPath to find the "title" breadcrumb of the current
	 * page.
	 * <p>
	 * An example is the title "Account" which is at the top of the record for an
	 * Account object.
	 * <p>
	 * This is mainly used to confirm arrival on the required page.
	 *
	 * @param title the title of the current record or page in Lightning
	 * @return the Xpath corresponding to that element on the page.
	 */
	protected static String deriveLightningTitleXpath(String title) {
		return TITLE_BEGIN + title + TITLE_END;
	}

	/**
	 * In Lightning, create the XPath to find the "title" breadcrumb of the current
	 * List page.
	 * <p>
	 * An example is the title "Cases" which is at the top of the list page for
	 * Cases.
	 * <p>
	 * This is mainly used to confirm arrival on the required list page.
	 *
	 * @param title the title of the current list page in Lightning
	 * @return the Xpath corresponding to that element on the page.
	 */
	protected static String deriveListPageLightningTitleXpath(String title) {
		return LIST_TITLE_BEGIN + title + LIST_TITLE_END;
	}

	/**
	 * In Lightning, create the XPath to find the value in a read-only page of a
	 * field, given its "label".
	 * <p>
	 * An example is to find the Xpath for the value of VAT Number for an Account
	 * (in the Account page), invoke this method with the label value "VAT Number".
	 * <p>
	 * This will not work for values which are 'links' or checkboxes etc. - only
	 * standard text values.
	 * <p>
	 * This cannot be used in a @FindBy annotation owing to the restriction about
	 * constants in annotations(!).
	 *
	 * @param title the label text of the desired field in a read-only page in
	 *              Lightning
	 * @return the Xpath corresponding to the text value of that field
	 */
	protected final static String deriveLightningTextFieldValue(String label) {
		return TEXT_VALUE_BEGIN + label + TEXT_VALUE_END;
	}

	/**
	 * Convenience method to attempt to return a single WebElement given an XPath.
	 * <p>
	 * Will throw any exceptions (e.g. org.openqa.selenium.NoSuchElementException)
	 * to the caller.
	 *
	 * @param xpath the xpath pointing to a single WebElement (hopefully!)
	 * @return the found WebElement.
	 */
	protected static WebElement getWebElementFromXpath(String xpath) {
		return Browser.driver.findElement(By.xpath(xpath));
	}

	/**
	 * Convenience method to attempt to return a list of WebElement given an XPath.
	 * <p>
	 * Will throw any exceptions (e.g. org.openqa.selenium.NoSuchElementException)
	 * to the caller.
	 *
	 * @param xpath the xpath pointing to zero, one or many WebElements
	 * @return the found WebElement.
	 */
	protected static List<WebElement> getWebElementsFromXpath(String xpath) {
		return Browser.driver.findElements(By.xpath(xpath));
	}

	/**
	 * That method can be used after creating or editing a page, to validate the
	 * message contains such as "was created", "was saved"
	 *
	 * @param message a message containing the result of saving new or edited pages.
	 */
	public void validateMessageContains(String message) {
		try {
			WaitUtilities.waitForElementVisibleLocated(By.xpath(TOASTMESSAGE));
			WebElement toastMessage = Browser.driver.findElement(By.xpath(TOASTMESSAGE));
			AssertLogger.assertTrue("ToastMessage contains >>> " + message, toastMessage.getText().contains(message));
		} catch (Exception e) {
			ExceptionHandler.handleException("Issue at Validating TOASTMESSAGE contains > " + message, e, this);
		}
	}

	/**
	 * That method can be used when there is no access to the page such as "Looks
	 * like there's a problem."
	 *
	 * @param message a message when the error occurs.
	 */
	public void validateErrorMessage(String message) {
		try {
			WaitUtilities.waitForElementVisibleLocated(By.xpath(ERRORMESSAGE));
			WebElement errorMessage = Browser.driver.findElement(By.xpath(ERRORMESSAGE));
			AssertLogger.assertTrue("Error Message contains >>> " + message, errorMessage.getText().contains(message));
		} catch (Exception e) {
			ExceptionHandler.handleException("Issue at Validating ERRORMESSAGE contains > " + message, e, this);
		}
	}

	/**
	 * Attempt to see if you can potentially edit the object. Actually editing the
	 * object will be left to subsequent steps.
	 * <p>
	 * If the value of can is false then the user should not be able to edit the
	 * object.
	 * <p>
	 * If the value of can is true then the user should be able to edit the object.
	 *
	 * @param can true or false
	 */
	public void editObjectValidation(boolean can) {
		try {
			if (!can) {
				AssertLogger.assertTrue("Edit button in page should not exist",
						!WaitUtilities.doesElementExist(editButtonModalWindow));
			} else {
				AssertLogger.assertTrue("Edit button in page should exist",
						WaitUtilities.doesElementExist(editButtonModalWindow));
			}
		} catch (Exception e) {
			ExceptionHandler.handleException("Issue checking existence of Edit button", e, this);
		}
	}

	/**
	 * Validates Dom contains
	 *
	 * @param message to be validated
	 */
	public void validateDOMContains(String message) {
		boolean condition = Browser.driver.findElement(By.tagName("body")).getText().contains(message);
		AssertLogger.assertTrue("DOM contains message >> " + message, condition);
	}

}
