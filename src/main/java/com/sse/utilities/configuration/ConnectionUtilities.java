package com.sse.utilities.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sse.utilities.credentials.SecretHandler;
import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.GetUserInfoResult;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.enterprise.fault.ExceptionCode;
import com.sforce.soap.enterprise.fault.UnexpectedErrorFault;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * A class used to hold general Enterprise SOAP API and Metadata API utilities (e.g.
 * error-handling, getting the connection etc.)
 * 
 * @author mitchella3
 *
 */
public final class ConnectionUtilities {
	private static Logger log = LogManager.getLogger(ConnectionUtilities.class);
	
	//A HashMap of roles with associated connections.
	private static Map<String,EnterpriseConnection> enterpriseConnectionMap = new HashMap<>();

	private ConnectionUtilities() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	/**
	 * Logout from the Enterprise connection for ALL roles which have been cached
	 * and then clear the cache.
	 * <p>
	 * Any ConnectionExceptions will not be thrown, as we will assume that the
	 * session(s) will eventually be invalidated by Salesforce itself.
	 */
	public static void logoutAllUsers() {
		Set<String> roles = enterpriseConnectionMap.keySet();
		for (String role : roles) {
			logout(role);
		}
		enterpriseConnectionMap.clear();
	}

	/**
	 * Logout from the Enterprise connection for a specific role.
	 * <p>Any ConnectionExceptions will not be thrown, as we will assume that the session will eventually be invalidated by Salesforce itself.
	 * @param role a String indicating the 'role' to be used for the API connection.
	 */
	private static void logout(String role) {
		EnterpriseConnection enterpriseConnection = enterpriseConnectionMap.get(role);
		if (enterpriseConnection != null) {
			try {
				enterpriseConnection.logout();
				log.info("Successfully logged out of Salesforce API for role : " + role);
			} catch (ConnectionException ce) {
				log.error("Could not log out of Salesforce API for role : " + role, ce); //no further action, just assume that the session timeout will eventually clear the session.
			}
		}
	}

	/**
	 * Returns the 'stored' Enterprise connection from this class for the passed role if it appears valid (by attempting a 'dummy' getUserInfo call on it).
	 * <p>If the stored connection is null or the getUserInfo call fails with an invalid session id, the connection is recreated from scratch.
	 * @param role a String indicating the 'role' to be used for the API connection.
	 * @return a valid Enterprise connection
	 * @throws ConnectionException if there is some unresolvable issue getting the
	 *                             valid connection
	 */
	public static EnterpriseConnection getConnection(String role) throws ConnectionException {
		EnterpriseConnection enterpriseConnection = enterpriseConnectionMap.get(role);
		if (enterpriseConnection == null) {
			enterpriseConnection = loginAndCreateConnection(role);
			enterpriseConnectionMap.put(role, enterpriseConnection);
		} else {
			try {
				enterpriseConnection.getUserInfo();
			} catch (UnexpectedErrorFault uef) {
				log.warn("UnexpectedErrorFault generated");
				if (uef.getExceptionCode() == ExceptionCode.INVALID_SESSION_ID) {
					log.warn("UnexpectedErrorFault generated as session invalid. Reauthenticating...");
					// Re-authenticate the user
					enterpriseConnection = loginAndCreateConnection(role);
					enterpriseConnectionMap.put(role, enterpriseConnection);
				} else {
					log.error("UnexpectedErrorFault generated. Exception code : " + uef.getExceptionCode());
					throw uef;
				}
			}
		}
		return enterpriseConnection;
	}

	/**
	 * Log in to Salesforce API Enterprise service URL and return a Connection which
	 * can be used for further requests.
	 *
	 * @param userName the Salesforce username to log in as.
	 * @param password the password of the user - which needn't be appended with the
	 *                 security token provided the IP address is trusted.
	 * @param url      the SOAP API endpoint to connect to the Salesforce API
	 *                 Otherwise, the security token will need to be appended.
	 * @return an EnterpriseConnection to the Salesforce API.
	 * @throws ConnectionException in the event of connection issues.
	 */
	private static EnterpriseConnection login(String userName, String password, String url) throws ConnectionException {
		final ConnectorConfig config = new ConnectorConfig();
		config.setUsername(userName);
		config.setPassword(password);
		config.setAuthEndpoint(url);

		EnterpriseConnection connection = Connector.newConnection(config); // this sets the URL within it.
		log.info("Logging in with username : " + userName);
		log.debug("Auth EndPoint: " + config.getAuthEndpoint());
		log.debug("Service EndPoint: " + config.getServiceEndpoint());
		log.debug("Username: " + config.getUsername());
		log.debug("SessionId: " + config.getSessionId());

		return connection;
	}

	/**
	 * Login to the Salesforce enterprise service and create an Enterprise
	 * connection for the passed role
	 * <p>
	 * This uses the current environment properties to find the user from the role,
	 * and uses these credentials.
	 * <p>
	 * This class maintains a cached connection (which may need to be refreshed
	 * after timeouts) on a per-role basis.
	 * 
	 * @return EnterpriseConnection the connection for use by subsequent
	 *         queries/updates.
	 * @throws RuntimeException    if the credentials could not be obtained.
	 * @throws ConnectionException if there is an issue connecting to Salesforce
	 */
	private static EnterpriseConnection loginAndCreateConnection(String role) throws ConnectionException {
		Properties currentEnvironmentProperties = PropertiesHolder.environmentProperties;
		if (currentEnvironmentProperties == null)
			throw new RuntimeException("Environment properties not found.");

		String environment = currentEnvironmentProperties.getProperty("environment");
		log.info("Attempting to create connection in environment " + environment);
		String url = currentEnvironmentProperties.getProperty("SOAPURL." + environment);
		if (url == null)
			throw new RuntimeException("No SOAP URL property defined for environment " + environment);

		String roleSuffix = "_" + role.replace(" ", "") + ".";
		String userNameProperty = "Username" + roleSuffix + environment;
		String passwordProperty = "Password" + roleSuffix + environment;
		String userName = currentEnvironmentProperties.getProperty(userNameProperty);
		String password = SecretHandler.decrypt(currentEnvironmentProperties.getProperty(passwordProperty));
		EnterpriseConnection connection = login(userName, password, url);
		connection.setDuplicateRuleHeader(true, false, true);
		
		GetUserInfoResult userInfo = connection.getUserInfo();

		log.debug("UserID: " + userInfo.getUserId());
		log.info("User Full Name: " + userInfo.getUserFullName());
		log.debug("User Email: " + userInfo.getUserEmail());
		log.debug("Session seconds : " + userInfo.getSessionSecondsValid());
		log.debug("Locale : " + userInfo.getUserLocale());
		log.debug("All details : " + userInfo.toString());

		return connection;
	}
	
	
	
//	******************** Metadata API Related Part ********************
	
	
	/**
	 * Log in to Salesforce API Enterprise service URL and return LoginResult which
	 * can be used for further requests.
	 * 
	 * @param userName username of the user used to connect
	 * @param password password of the user used to connect
	 * @param url      SOAP url property defined to connect
	 * @return
	 * @throws ConnectionException if there is an issue in connecting
	 */
	private static LoginResult loginAndReturnLoginResult(String userName, String password, String url)
			throws ConnectionException {
		final ConnectorConfig config = new ConnectorConfig();
		config.setAuthEndpoint(url);
		config.setServiceEndpoint(url);
		config.setManualLogin(true);

		return Connector.newConnection(config).login(userName, password);
	}

	/**
	 * Returns MetadataConnection
	 * 
	 * @param loginResult
	 * @return
	 * @throws ConnectionException if there is an issue in connecting
	 */
	private static MetadataConnection getMetadataConnection(LoginResult loginResult) throws ConnectionException {
		final ConnectorConfig config = new ConnectorConfig();
		config.setServiceEndpoint(loginResult.getMetadataServerUrl());
		log.debug("Metadata Server URL: " + loginResult.getMetadataServerUrl());
		config.setSessionId(loginResult.getSessionId());
		return new MetadataConnection(config);
	}

	/**
	 * Login to the Salesforce enterprise service and create a Metadata API
	 * connection for the passed role.
	 * 
	 * @param role the Salesforce user to create Metadata connection with.
	 * 
	 * @return MetadataConnection the connection for use by subsequent
	 *         queries/updates.
	 * @throws RuntimeException    if the credentials could not be obtained.
	 * @throws ConnectionException if there is an issue in connecting to Metadata
	 *                             API.
	 */
	public static MetadataConnection loginAndCreateMetadataConnection(String role) throws ConnectionException {
		Properties currentEnvironmentProperties = PropertiesHolder.environmentProperties;
		if (currentEnvironmentProperties == null)
			throw new RuntimeException("Environment properties not found.");

		String environment = currentEnvironmentProperties.getProperty("environment");
		log.info("Attempting to create connection in environment " + environment);
		String url = currentEnvironmentProperties.getProperty("SOAPURL." + environment);
		if (url == null)
			throw new RuntimeException("No SOAP URL property defined for environment " + environment);

		String roleSuffix = "_" + role.replace(" ", "") + ".";
		String userNameProperty = "Username" + roleSuffix + environment;
		String passwordProperty = "Password" + roleSuffix + environment;
		String userName = currentEnvironmentProperties.getProperty(userNameProperty);
		String password = SecretHandler.decrypt(currentEnvironmentProperties.getProperty(passwordProperty));
		LoginResult loginResult = loginAndReturnLoginResult(userName, password, url);
		MetadataConnection metadataConnection = getMetadataConnection(loginResult);

		GetUserInfoResult userInfo = loginResult.getUserInfo();

		log.debug("UserID: " + userInfo.getUserId());
		log.info("User Full Name: " + userInfo.getUserFullName());
		log.debug("User Email: " + userInfo.getUserEmail());
		log.debug("Session seconds : " + userInfo.getSessionSecondsValid());
		log.debug("All details : " + userInfo.toString());

		return metadataConnection;
	}
	
}
