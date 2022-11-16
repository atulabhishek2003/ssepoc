package com.sse.pages;

import com.sse.pages.HomePage;
import com.sse.pages.LoginPage;
import com.sse.pages.NavigationPanel;
import com.sse.salesforce.ScenarioData;
import com.sse.utilities.Browser;

import io.cucumber.java.Scenario;

/**
 * Class to hold instances of all Page objects
 *
 * @author 
 */
@SuppressWarnings("javadoc")
public final class Pages {
	public static ScenarioData scenarioData;
	/**
	 * Tabs and Search box - common to most standard  pages.
	 */
	public static NavigationPanel navigationPanel;
	/**
	 * The main Salesforce pages.
	 */
	public static LoginPage loginPage;
	public static HomePage sseHomePage;
	
	private Pages() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	     }	
		
	/**
	 * Construct instances of all Pages
	 * <p>
	 * IF the driver is null, then we are running a non-browser scenario e.g. Oracle
	 * validation. In that case, we only want to initialise the scenarioData 'page'.
	 *
	 * @param scenarioName the current Cucumber scenario.
	 */
	public static void initialise(Scenario scenario) {
		scenarioData = new ScenarioData(scenario);

		if (Browser.driver == null)
			return;

		loginPage = new LoginPage();
		sseHomePage = new HomePage();
		navigationPanel = new NavigationPanel();
		
	}
	
	}

