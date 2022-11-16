package com.sse.sseGeneral.enterprise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.sse.utilities.configuration.PropertiesHolder;


/**
 * A class used to invoke the Salesforce Enterprise API and disable/enable Single Sign-On for automation profiles.
 * This also sets all relevant profiles to "password never expires" to allow them to login with an expired password.
 * @author mitchella3
 *
 */
public final class ProfileChanger {
	private static Logger log = LogManager.getLogger(ProfileChanger.class);
	final static List<String> AUTOMATION_USERS_SSO_LIST = new ArrayList<>(Arrays.asList(
			"QA"
		));

	private ProfileChanger() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}		
	
	/**
	 * Disable/enable Single Sign-On for all profiles using the Salesforce API logging in as the sys admin user.
	 * <p>If the setSsoEnabled flag is set to true with the intention of enabling SSO, firstly, this checks the value of the SSOEnable property in the environment-specific properties file.
	 * SSO will ONLY be enabled IF BOTH ssoEnabled is set to true AND the SSOEnable property is set to "true".
	 * <p>Also, any action in this method will only be performed on the profiles whose names correspond to those in the variable <code>automationUsersSSOList</code>
	 * @param ssoEnabled true to enable SSO, false otherwise.
	 */
	public static void setSSO(boolean ssoEnabled) {
		amendProfiles(ssoEnabled, false);
	}

	/**
	 * Disable Single Sign-On and set password to Never Expires for all profiles using the Salesforce API logging in as the sys admin user.
	 * <p>Any action will only be performed on the profiles whose names correspond to those in the variable <code>automationUsersSSOList</code>
	 */
	public static void prepareForAutoRun() {
		amendProfiles(false, true);
	}

	/**
	 * Disable/enable Single Sign-On and/or set the password policy to "Never Expires" for all profiles using the Salesforce API logging in as the sys admin user.
	 * <p>If the setSsoEnabled flag is set to true with the intention of enabling SSO, firstly, this checks the value of the SSOEnable property in the environment-specific properties file.
	 * SSO will ONLY be enabled IF BOTH ssoEnabled is set to true AND the SSOEnable property is set to "true".
	 * <p>Also, any action in this method will only be performed on the profiles whose names correspond to those in the variable <code>automationUsersSSOList</code>
	 * @param setSsoEnabled true to enable SSO if applicable in this environment, false to disable SSO.
	 * @param setPasswordNeverExpires true to set the password policy to "Never Expires", false will bypass any changes to the password policy
	 */
	private static void amendProfiles(boolean setSsoEnabled, boolean setPasswordNeverExpires) {
		boolean envSSOEnable = false; //Does the properties file say that we can update SSO to enabled.
		if (setSsoEnabled) {
			if (PropertiesHolder.environmentProperties == null)
				throw new RuntimeException("Environment properties not found.");
			envSSOEnable = Boolean.parseBoolean(PropertiesHolder.environmentProperties.getProperty("SSOEnable"));
			if (!envSSOEnable) {
				log.info("SSO enabling requested but properties file setting disallows this. No SSO re-enablement will occur.");
				log.info("This is valid for most automation test environments.");
			}
		}
		
		
	}
}

