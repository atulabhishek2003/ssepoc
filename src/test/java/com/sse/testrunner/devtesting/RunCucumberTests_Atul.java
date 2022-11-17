package com.sse.testrunner.devtesting;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.sse.sseGeneral.enterprise.ProfileChanger;
import com.sse.testrunner.AbstractRunCucumber;
import com.sse.utilities.ExceptionHandler;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;


/**
 * This is the main JUnit/Cucumber runner class.
 *
 * @author Atul
 */
@RunWith(Cucumber.class)
@CucumberOptions (
		glue = {"com.sse.stepdefinitions"},
		dryRun = false,
		monochrome=true,	
		stepNotifications = true,
		plugin = {
					"pretty", 
					"summary", 
					"pretty:reports/cucumber-pretty.txt",
					"json:target/cucumber.json", 
					"html:target/cucumber.html",
					"junit:target/cucumber.xml",
					"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
				},
		snippets = SnippetType.CAMELCASE,

		features = {
				"src/test/resources/developmentTests/",
				
				},
		tags = (
   				 "@TEST_ONLY"
				)

)

public class RunCucumberTests_Atul extends AbstractRunCucumber {


	@SuppressWarnings("javadoc")
	@BeforeClass
	public static void before() {
		ExceptionHandler.displaySplash=true;		
		ProfileChanger.prepareForAutoRun();

	}

}
