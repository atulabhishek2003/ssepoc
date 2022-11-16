package com.sse.utilities;


import java.io.File;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sse.utilities.configuration.PropertiesHolder;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * A class representing the 'browser' which holds the web driver instance and details.
 * It also includes 'browser' activities, e.g. page refreshes etc.
 * @author mitchella3
 */
public final class Browser {

	/**
	 * The globally-accessible web driver instance
	 */
	public static WebDriver driver;
	/**
	 * The global default driver wait.
	 */
	public static WebDriverWait defaultDriverWait;
    private static final long DEFAULT_WAIT_TIMEOUT_SECS = 61;
    private static final long DEFAULT_SLEEP_TIMEOUT_MILLIS = 200;

	/**
	 * A driver wait to be used for very long wait times.
	 */
	public static WebDriverWait longDriverWait;
	private static final long LONG_WAIT_TIMEOUT_SECS = 360;
	private static final long LONG_SLEEP_TIMEOUT_MILLIS = 200;
	/**
	 * A driver wait to be used for short wait times.
	 */
	public static WebDriverWait shortDriverWait;
	private static final long SHORT_WAIT_TIMEOUT_SECS = 7;
	private static final long SHORT_SLEEP_TIMEOUT_MILLIS = 200;

	/**
	 * Driver with 15 second wait time
	 */
	public static WebDriverWait driverWait15Seconds;
	/**
	 * Driver with 2 second wait time
	 */
	public static WebDriverWait driverWait2Seconds;

	private static Logger log = LogManager.getLogger(Browser.class);



	private enum WindowAttribute {
		URL,TITLE;
	}
	
	private Browser() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}	
	
	/**
	 * Initialise the required browser from the configuration file.
	 */
	public static void initialise() {

		switch (PropertiesHolder.configurationProperties.getProperty("browser")) {
		case "CHROME" :
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.addArguments("--disable-infobars","--start-maximized","--disable-notifications");
			WebDriverManager.chromedriver().setup();
			//System.setProperty(CHROME_DRIVER_SYSTEM_PROPERTY, CHROME_DRIVER_LOCATION);
			driver = new ChromeDriver(chromeOptions);
			break;
		case "CHROME_HEADLESS" :
			ChromeOptions chromeOptionsHeadless = new ChromeOptions();
			chromeOptionsHeadless.addArguments("--disable-infobars","--start-maximized","--disable-gpu","--headless");
			WebDriverManager.chromedriver().setup();
			//System.setProperty(CHROME_DRIVER_SYSTEM_PROPERTY, CHROME_DRIVER_LOCATION);
			driver = new ChromeDriver(chromeOptionsHeadless);
			break;
		case "FIREFOX" :
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			WebDriverManager.firefoxdriver().setup();
			//System.setProperty(FIREFOX_DRIVER_SYSTEM_PROPERTY, FIREFOX_DRIVER_LOCATION);
			driver = new FirefoxDriver(firefoxOptions);
			break;
		case "IE" :
			throw new Error("IE not implemented.");
		default:
			throw new Error("Not implemented.");
		}
		defaultDriverWait  = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT_SECS, DEFAULT_SLEEP_TIMEOUT_MILLIS);
		longDriverWait  = new WebDriverWait(driver, LONG_WAIT_TIMEOUT_SECS, LONG_SLEEP_TIMEOUT_MILLIS);
		shortDriverWait  = new WebDriverWait(driver, SHORT_WAIT_TIMEOUT_SECS, SHORT_SLEEP_TIMEOUT_MILLIS);
		driverWait15Seconds = new WebDriverWait(driver, 15, 200);
		driverWait2Seconds = new WebDriverWait(driver, 2, 200);
	}

	/**
	 * Navigate to the given URL.
	 * @param url the URL
	 */
	public static void goTo(String url) {
		driver.get(url);
	}

	/**
	 * @return the current browser page title.
	 */
	public static String title() {
		return driver.getTitle();
	}

	/**
	 * @return the current browser URL
	 */
	public static String url() {
		return driver.getCurrentUrl();
	}

	/**
	 * Close the current browser windows/instances (but only if the driver was created).
	 */
	public static void close() {
		if (driver != null) {
			Set<String> handles = driver.getWindowHandles();
			for (String handle : handles) {
				driver.switchTo().window(handle);
				driver.close();
			}
		}
	}

	/**
	 * Close the current browser window only provided there are at least two windows/tabs open
	 * If there are remaining windows, navigate to the first one found
	 */
	public static void closeCurrentWindow() {
		Set<String> handles = driver.getWindowHandles();
		if (handles.size()>=2) {
			driver.close();
		}

		for (String handle : handles) {
			driver.switchTo().window(handle);
			break;
		}
	}

    /**
     * Refresh the current page.
     */
    public static void refreshPage(){
    	driver.navigate().refresh();
    }

    /**
     * Close an alert.
     */
    public static void closePop() {
    	Alert alert = driver.switchTo().alert();
    	alert.accept();
	}

    /**
     * Take a screenshot and store in a file.
     * @param filePath the path to the storage location
     */
    public static void screenShot(String filePath) {
    	try {
    		TakesScreenshot ts = (TakesScreenshot)(driver);
    		File src = ts.getScreenshotAs(OutputType.FILE);
    		FileUtils.copyFile(src, new File(filePath),false);
    	}
    	catch (Exception e) {
    		log.warn("Screenshot exception storing in filePath " + filePath,e);
		}
    }

    /**
     * Iterates through all windows under the control of this Browser's WebDriver
     * and returns the Window Handle of the first window found whose title contains the specified String.
     * <p>BEWARE of windows which close in the event of their "parent" gaining focus - e.g. search/lookup windows
     * @param partialTitle the String to search for in the title of any current windows.
     * @return the Window Handle if found, otherwise null.
     */
    public static String getWindowHandleByPartialTitle(String partialTitle) {
    	return getWindowHandleBySomeAttributeText(partialTitle,WindowAttribute.TITLE);
    }

    /**
     * Iterates through all windows under the control of this Browser's WebDriver
     * and returns the Window Handle of the first window found whose URL contains the specified String.
     * <p>BEWARE of windows which close in the event of their "parent" gaining focus - e.g. search/lookup windows
     * @param partialURL the String to search for in the URL of any current windows.
     * @return the Window Handle if found, otherwise null.
     */
    public static String getWindowHandleByPartialURL(String partialURL) {
    	return getWindowHandleBySomeAttributeText(partialURL,WindowAttribute.URL);
    }

    private static String getWindowHandleBySomeAttributeText(String text, WindowAttribute attribute) {
		String currentWindow = driver.getWindowHandle();

		Set<String> handles = driver.getWindowHandles();
		String handleToReturn = null;

		outerLoop:
		for (String handle : handles) {
			driver.switchTo().window(handle);
			switch (attribute) {
			case TITLE:
				if (driver.getTitle().contains(text)) {
					handleToReturn = handle;
					break outerLoop;
				}
				break;
			case URL:
				if (driver.getCurrentUrl().contains(text)) {
					handleToReturn = handle;
					break outerLoop;
				}
				break;
			default:
			}
		}

		driver.switchTo().window(currentWindow);
		return handleToReturn;
    }

    /**
     * Loops through all windows associated with this Browser Webdriver instance and closes
     * any which contain the String partialTitle.
     * <p>Note, if a feature file is launched using Run As Cucumber Feature, then the driver will be
     * null, so in that case, this method simply returns.
     * @param partialTitle the String which a Window title may contain.
     */
    public static void closeWindowsWithTitle(String partialTitle) {
    	if (driver==null) return;
		String currentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			driver.switchTo().window(handle);
			if (driver.getTitle().contains(partialTitle)) {
				driver.close();
			}
		}

		driver.switchTo().window(currentWindow);
    }

    /**
     * Checks if page exist
     * @return true or false based on page title
     */
    public static boolean doesPageExist() {
    	try {
    		WaitUtilities.waitForPageTitle("Unable to load");
    	} catch(@SuppressWarnings("unused") Exception e) {
    		log.warn("Page title is: " + Browser.driver.getTitle());
    	}
    	return !Browser.driver.getTitle().contains("Unable to load");
    }

}
