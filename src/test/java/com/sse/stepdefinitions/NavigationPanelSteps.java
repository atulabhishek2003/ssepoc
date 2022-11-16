package com.sse.stepdefinitions;


import com.sse.pages.NavigationPanel;
import com.sse.pages.Pages;
import com.sse.utilities.WaitUtilities;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
/**
 * Step definitions related to the navigation tabs and search entry at the top of most Salesforce pages.
 * @author mitchella3
 */
public final class NavigationPanelSteps {

	/**
	 * Click the waffle grid.
	 * <p>This generates a list of 'pages' which can be accessed.
	 */
	@When("I click the waffle grid")
	public void iClickWaffleGrid() {
		WaitUtilities.sleep(5);
		Pages.navigationPanel.clickWaffleGrid();
	}

	/**
	 * Navigates using the waffle grid to another page.
	 * @param page the name of the page to navigate to.
	 */
	@Then("I navigate to the {string} page using waffle grid")
	public void iNavigateToThePageUsingWaffle(String page) {
		WaitUtilities.sleep(5);
		Pages.navigationPanel.clickWaffleAndNavigate(page);
	}
	/**
	 * To click on the notification tab and then click on the notification received
	 */
	@Then("I navigate to notification tab and click on the notification received")
	public void iNavigateToNotificationsTab()
	{
		Pages.navigationPanel.iNavigateToNotifications();
	}

	/**
	 * Navigates using the navigation tabs to another page.
	 * The page name must be declared in the constructor of {@link NavigationPanel}.
	 * @see NavigationPanel#NavigationPanel()
	 * @param page the name of the tab to navigate to.
	 */
	@Then("I navigate to the {string} page")
	public void iNavigateToThePage(String page) {
		Pages.navigationPanel.clickTab(page);
	}

	/**
	 * Logs out the current user
	 */
	@And("I logout")
	public void iLogout() {
		Pages.navigationPanel.logout();
		Pages.loginPage.checkArrivalOnPage();
	}

}

