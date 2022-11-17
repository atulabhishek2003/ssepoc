package com.sse.utilities;
import static org.junit.Assume.assumeNoException;

import java.awt.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sse.utilities.development.ErrorSplash;

/**
 * A class for dealing with Exceptions.
 * @author atul
 */
public final class ExceptionHandler {
	/**
	 * Whether to display a JFrame containing the error message.
	 * <p>Only useful when developing locally to allow exceptions to be very obvious whilst working
	 * on other things.....
	 */
	public static boolean displaySplash = false;

	/**
	 * Just in case we really do want to <b>fail</b> tests owing to technical errors, set this to false to generate
	 * Assertion errors for (e.g.) timeouts etc.....
	 * <b>This will change any "blue" to "red" on output reports AND IS NOT RECOMMENDED
	 */
	public static boolean invokeAssumeNoException = true;
	private static Logger log = LogManager.getLogger(ExceptionHandler.class);

	/**
	 * Special log4j2 logger which can be used anywhere to log to a scenario summary file.
	 * <p>See log4j2.xml for details of the configuration.
	 */
	public static final Logger RUN_SUMMARY = LogManager.getLogger("RUN_SUMMARY");

	private ExceptionHandler() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}		
	
	/**
	 * This method treats any Exception as a 'technical error' which should not<br>
	 * be reported as a test failure. Examples might include timeout Exceptions.<br>
	 * Exceptions are fed back to JUnit via the <code>assumeNoException</code> method<br>
	 * which reports the exception, skips subsequent steps, but does not fail the test.
	 * @param message a description of the exception which has occurred.
	 * @param e the Exception to be handled
	 * @param callingObject the object in which the Exception occurred.
	 */
	public static void handleException(String message, Exception e, Object callingObject) {
		log.error("Exception thrown in " + callingObject.getClass().getSimpleName() + " : " + message,e);
		RUN_SUMMARY.error(callingObject.getClass().getName() + " : " + message + getEssentialsFromThrowable(e));
		if (displaySplash) splash(message, e);
	    if (invokeAssumeNoException)
	    	assumeNoException(e);
	    else org.junit.Assert.fail(callingObject.getClass().getSimpleName() + " : " + message);
	}

	/**
	 * Similar to handleException(String message, Exception e, Object callingObject)
	 * except it receives a Class type as the calling object, so that this can be invoked from static methods.
	 *
	 * This method treats any Exception as a 'technical error' which should not<br>
	 * be reported as a test failure. Examples might include timeout Exceptions.<br>
	 * Exceptions are fed back to JUnit via the <code>assumeNoException</code> method<br>
	 * which reports the exception, skips subsequent steps, but does not fail the test.
	 * @param message a description of the exception which has occurred.
	 * @param e the Exception to be handled
	 * @param callingClass the class in which the Exception occurred.
	 */
	public static void handleException(String message, Exception e, Class<?> callingClass) {
		log.error("Exception thrown in " + callingClass.getSimpleName() + " : " + message,e);
		RUN_SUMMARY.error(callingClass.getName() + " : " + message + getEssentialsFromThrowable(e));
		if (displaySplash)
			splash(message, e);
	    if (invokeAssumeNoException)
	    	assumeNoException(e);
	    else org.junit.Assert.fail(callingClass.getSimpleName() + " : " + message);
	}

	@SuppressWarnings({ "unused", "javadoc" })
	public static void splash(String message, Throwable e) {
		if (e == null) new ErrorSplash(6000, message, Color.GREEN);
		else new ErrorSplash(6000, message + " : " + e, Color.RED);
		WaitUtilities.sleep(15);
	}

	/**
	 * Attempts to get "interesting" and essential information from a Throwable's stack trace
	 * to be used in the summary reporting log output.
	 * <p>If a null Throwable is passed, this method returns null
	 * @param t the Throwable to interrogate
	 * @return a String of the main points of interest
	 */
	public static String getEssentialsFromThrowable(Throwable t) {
		if (t == null) return "";
		String padding = "                                    ";
		StringBuffer essentials = new StringBuffer("\n");
		essentials.append(padding + StringUtilities.before(t.toString(),"\n"));
		StackTraceElement[] elements = t.getStackTrace();
		for (StackTraceElement element : elements) {
			String elementString = element.toString();
			if (elementString.contains("com.lnrs") &&
				!elementString.contains("com.lnrs.utilities.AssertLogger")
			)
			essentials.append("\n" + padding + element);
		}
		return essentials.toString();
	}

}
