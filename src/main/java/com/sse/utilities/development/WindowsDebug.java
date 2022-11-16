package com.sse.utilities.development;

import java.util.Set;

import com.sse.utilities.Browser;

/**
 * A class to output information on the current Window handles - for debugging only.
 * @author mitchella3
 */
public class WindowsDebug {

	/**
	 * Outputs details of all current Windows.
	 * @param extraText some descriptive text to add to the start of the dump, e.g. where it's being called from.
	 */
	public static void dumpWindowDetails(String extraText) {
		System.out.println("\n\n*** DUMP OF WINDOW HANDLES ***");
		System.out.println(extraText);

		String currentWindow = Browser.driver.getWindowHandle();
		System.out.println("Current window handle : " + currentWindow);

		try {
			Set<String> handles = Browser.driver.getWindowHandles();
			int i=0;
			for (String handle : handles) {
				System.out.println("Window handle : '" + i + "' = " +  handle);
				Browser.driver.switchTo().window(handle);
				System.out.println("Window title : '" + i + "' = " +  Browser.driver.getTitle());
				System.out.println("Window URL : '" + i + "' = " +  Browser.driver.getCurrentUrl());
				i++;
			}
		}
		/* org.openqa.selenium.NoSuchWindowException: no such window
		 * may occur in e.g. the Lookup windows. If the parent gets focus, the search/lookup window closes!!!
		 */
		catch (Exception e) {
			System.out.println("Exception dumping window handles...." + e);
			e.printStackTrace();
		}
		finally {
			Browser.driver.switchTo().window(currentWindow);
			System.out.println("*** END OF DUMP OF WINDOW HANDLES ***\n\n");
		}

	}

}
