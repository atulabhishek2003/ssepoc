package com.sse.stepdefinitions.general;

import com.sse.utilities.Browser;

import io.cucumber.java.en.Then;

/**
 * Step definitions which can be generally used independently of the current page.
 * @author mitchella3
 */
public class GeneralPageSteps {

	/**
	 * Refresh the current page, wherever you are.
	 */
	@Then("I refresh the page")
	public void iRefreshThePage() {
		Browser.refreshPage();
	}

}
