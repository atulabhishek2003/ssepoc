package com.sse.utilities.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sse.utilities.Browser;
import com.sse.utilities.security.SSLContextGenerator;

/**
 * Contains details of the overall configuration.
 * @author Atul
 */
public final class TestConfiguration {
	private static Logger log = LogManager.getLogger(TestConfiguration.class);
	/**
	 * The current 'user' directory.
	 * When running from Eclipse this will be the project root - e.g.
	 * C:\Users\mitchella3\git\AdSalesAutoTest
	 */
	public static final String USER_DIR  = System.getProperty("user.dir");
	/**
	 * User name. Will be the account under which this is running - e.g. "mitchella3".
	 */
	public static final String USER_NAME = System.getProperty("user.name");
	/**
	 * Top-level directory location for storing serialised scenarioData objects.
	 * <br>Excludes trailing separator ("\\" on Windows)
	 */
	public static String scenarioDataStoreDirectory;

	private TestConfiguration() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}		
	
	/**
	 * Initialises details of the test configuration, such as which browser, which environment etc.
	 * @param initialiseBrowser set to false if you don't wish to initialise the Browser object (e.g.
	 * if you are merely running deserialisation or Oracle validation features.....) Usually this will be true.
	 */
	public static void initialise(boolean initialiseBrowser) {
		initialise(initialiseBrowser, null);
	}
	
	/**
	 * Similar to initialise(boolean initialiseBrowser) except this takes a second parameter which forces
	 * the initialisation to IGNORE the value in environment.properties and replace it with the value passed.
	 * <br>e.g. if environment.properties has environment=STAGING   but you pass in SIT, then the SIT properties will be used.
	 * @param initialiseBrowser set to false if you don't wish to initialise the Browser object (e.g.
	 * if you are merely running deserialisation features.....) Usually this will be true.
	 * @param forceEnvironment the environment to use irrespective of the value in environment.properties
	 */
	public static void initialise(boolean initialiseBrowser, String forceEnvironment) {
		try {
			PropertiesHolder.populateEnvironmentProperties(forceEnvironment);
			PropertiesHolder.populateConfigurationProperties();
			PropertiesHolder.populateOracleTransactionTableColumnsProperties();
			PropertiesHolder.populateOracleCustomerTableColumnsProperties();
		}
		catch (IOException e) {
			log.error("Issue populating properties",e);
			throw new Error("Suite abandoned as properties could not be loaded.",e);
		}

		String environment = PropertiesHolder.environmentProperties.getProperty("environment");
		scenarioDataStoreDirectory =
				USER_DIR + File.separator + "serialised" + File.separator + environment;

		File directory = new File(scenarioDataStoreDirectory);
		if (!directory.exists()) directory.mkdirs();
		configureSSLKeystores();

		if (initialiseBrowser) Browser.initialise();

	}

	private static void configureSSLKeystores() {
		String filename = "src/main/resources/ViperDemoAPI_JKS.jks".replace('/', File.separatorChar);
		log.info("About to configure KeyStore at : " + filename);
		try (FileInputStream is = new FileInputStream(filename);) {
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			String password = "changeit";
			keystore.load(is, password.toCharArray());
			SSLContext.setDefault(SSLContextGenerator.provideSSLContext(keystore, password.toCharArray()));
		} catch (Exception e) {
			log.error("Issue populating SSL Keystores. Any external https call is likely to fail",e);
		}
	}

}
