package com.sse.utilities.development;

import java.util.Map;
import java.util.Properties;
/**
 * A class to allow the display (to the console) of useful environment-related information.
 * @author mitchella3
 *
 */
public final class EnvironmentDisplay {

	private EnvironmentDisplay() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}		
	
	/**
	 * Prints some of the more important/useful environment information to the console
	 */
	public static void displayEssentialEnvironmentToConsole() {

		print("Class path");
		print("----------");
		String classPath = System.getProperty("java.class.path");
		String[] parts = classPath.split(";", 0);
		for (String part : parts) {
			print("" + part);
		}

		displayOtherInfo();

	}

	/**
	 * Prints as much as possible environment information to the console
	 */
	public static void displayFullEnvironmentToConsole() {
		Properties sysProps = System.getProperties();
		for (Object key : sysProps.keySet()) {
			print("Property key : " + key);
			print("Property value : " + sysProps.getProperty((String) key));
		}
		Map<String,String> sysEnv = System.getenv();
		for (String key : sysEnv.keySet()) {
			print("Environment key : " + key);
			print("Environment value : " + sysEnv.get(key));
		}
		displayOtherInfo();
	}

	/**
	 * Displays assorted other useful information
	 */
	private static void displayOtherInfo() {
		print("");
		print("User/working directory");
		print("-----------------------");
		print(System.getProperty("user.dir"));


		print("");
		print("Current thread");
		print("-----------------------");
		print(Thread.currentThread().toString());


		print("");
		print("Total memory");
		print("-----------------------");
		print(Runtime.getRuntime().totalMemory()+"");
	}

	private static void print(String s) {
		System.out.println(s);
	}
}

