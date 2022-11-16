package com.sse.utilities.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sse.utilities.StringUtilities;

/**
 * A Class to hold any properties files used by the test suite.
 * @author Atul
 */
public final class PropertiesHolder {
	private static Logger log = LogManager.getLogger(PropertiesHolder.class);

	private static final String ENVIRONMENT_PROPERTIES_PATH = "environment.properties";
	@SuppressWarnings("javadoc")
	public static Properties environmentProperties;

	private static final String CONFIGURATION_PROPERTIES_PATH = "configuration.properties";
	@SuppressWarnings("javadoc")
	public static Properties configurationProperties;


	private static final String ORACLE_TRANSACTION_TABLE_COLUMNS_PATH = "oracletransactiontablecolumns.properties";
	@SuppressWarnings("javadoc")
	public static Properties oracleTransactionTableColumnsProperties;

	private static final String ORACLE_CUSTOMER_TABLE_COLUMNS_PATH = "oraclecustomertablecolumns.properties";
	@SuppressWarnings("javadoc")
	public static Properties oracleCustomerTableColumnsProperties;

	private PropertiesHolder() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}	
	
	/**
	 * Read the "environment" properties file(s)<br>
	 * environment.properties just contains the target environment and potentially anything 'global' across environments.
	 * <p>After reading the environment, a second properties file of the form <i>environment></i>_environment.properties
	 * (e.g. QA_environment.properties) which typically contgains credentials and URLs etc.
	 * is also read and combined into the environmentProperties Properties object.
	 * <p>If forceEnvironment is not null or empty, the value of forceEnvironment is used as the target environment
	 * irrespective of the contents of environment.properties.
	 * @param forceEnvironment the environment to use irrespective of the value in environment.properties (if specified)
	 * @throws IOException if an IO exception occurs
	 */
	public static void populateEnvironmentProperties(String forceEnvironment) throws IOException {
		String environment;
		if (StringUtilities.isNullEmptyOrWhiteSpace(forceEnvironment)) {
			environmentProperties = populateProperties(ENVIRONMENT_PROPERTIES_PATH);

			//Read environment, and then bootstrap the associated environment properties into the same Properties object.....
			environment = PropertiesHolder.environmentProperties.getProperty("environment");
			if (environment == null) throw new RuntimeException("NO ENVIRONMENT SPECIFIED IN ENVIRONMENT.PROPERTIES");
		}
		else {
			environment = forceEnvironment;
			environmentProperties = new Properties();
			environmentProperties.setProperty("environment", forceEnvironment);
		}
		environmentProperties.putAll(populateProperties(environment + "_" + ENVIRONMENT_PROPERTIES_PATH));
	}

	/**
	 * Read the "test configuration" properties file and loads into a publicly accessible Properties object.<br>
	 * This contains which browser is being used
	 * @throws IOException if an IO exception occurs
	 */
	public static void populateConfigurationProperties() throws IOException {
		configurationProperties = populateProperties(CONFIGURATION_PROPERTIES_PATH);
	}

	/**
	 * Read the "oracleTransactionTableColumnsProperties" properties file
	 * and loads into a publicly accessible Properties object.<br>
	 * This contains the names of every Oracle transaction staging table column.
	 * Many may not be required for this suite, however this provides a generic base for any transaction table testing.
	 * @throws IOException if an IO exception occurs
	 */
	public static void populateOracleTransactionTableColumnsProperties() throws IOException {
		oracleTransactionTableColumnsProperties = populateProperties(ORACLE_TRANSACTION_TABLE_COLUMNS_PATH);
	}

	/**
	 * Read the "oracleCustomerTableColumnsProperties" properties file
	 * and loads into a publicly accessible Properties object.<br>
	 * This contains the names of every Oracle Customer staging table column.
	 * Many may not be required for this suite, however this provides a generic base for any customer table testing.
	 * @throws IOException if an IO exception occurs
	 */
	public static void populateOracleCustomerTableColumnsProperties() throws IOException {
		oracleCustomerTableColumnsProperties = populateProperties(ORACLE_CUSTOMER_TABLE_COLUMNS_PATH);
	}

	/**
	 * @param path the relative classloader path to the properties resource.
	 * @return the populated Properties object
	 * @throws IOException if the properties file could not be found or read.
	 */
	public static Properties populateProperties(String path) throws IOException {

		Properties properties = new Properties();
		try (InputStream is = PropertiesHolder.class.getClassLoader().getResourceAsStream(path);) {
			properties.load(is);
			return properties;
		}
		catch (Exception e) {
			log.error("Error loading properties " + TestConfiguration.USER_DIR + "\\" + path, e);
			throw e;
		}
	}

}
