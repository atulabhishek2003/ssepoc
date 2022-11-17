package com.sse.utilities;
import static com.sse.utilities.ExceptionHandler.RUN_SUMMARY;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class can be used in place of genuine "assert" statements to log (and rethrow) any assertion errors.
 * <p>IF the RETHROW flag is set to false, then this can help speed up development allowing
 * assertion failures to not stop the scenario immediately but instead proceed to see if further failures ensue.
 * <p>Once development is completed, RETHROW should be set to true.
 * <p>The methods will "log.error" any assertion failure so that the developer can correct the issue as a background task.
 *
 * <p>Note that all methods are marked as Deprecated to indicate that they not for "real" use.
 * @author atul
 *
 */
public final class AssertLogger {
	private static Logger log = LogManager.getLogger(AssertLogger.class);

	/**
	 * Determines whether assertion failures are rethrown (which will stop the test suite).
	 * <p>SHOULD BE TRUE once all development is completed.
	 * <p>False can be used during testing to log issues without stopping tests.
	 */
	public static final boolean RETHROW = true;

	private AssertLogger() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}	
	
	/**
	 * Captures the Throwable from an assertEquals, and log.errors it.
	 * <p>If RETHROW is true, then the Throwable is rethrown to fail the corresponding test.
	 * @param descriptionOfAssertionMessage replica of assert message
	 * @param expected the expected object
	 * @param actual the actual object
	 */
	public static void assertEquals(String descriptionOfAssertionMessage, Object expected, Object actual) {
		try {
			org.junit.Assert.assertEquals(descriptionOfAssertionMessage, expected, actual);
			log.info(descriptionOfAssertionMessage + " ... passed! Value = " + expected);
		}
		catch (Throwable t) {
			logThrowable(descriptionOfAssertionMessage, t);
			if (RETHROW) throw t;
		}
	}

	/**
	 * Captures the Throwable from an assertEquals of ints, and log.errors it.
	 * <p>If RETHROW is true, then the Throwable is rethrown to fail the corresponding test.
	 * @param descriptionOfAssertionMessage replica of assert message
	 * @param expected the expected int value
	 * @param actual the actual int value
	 */
	public static void assertEqualsInt(String descriptionOfAssertionMessage, int expected, int actual) {
		try {
			org.junit.Assert.assertEquals(descriptionOfAssertionMessage, expected, actual);
			log.info(descriptionOfAssertionMessage + " ... passed! Value = " + expected);
		}
		catch (Throwable t) {
			logThrowable(descriptionOfAssertionMessage, t);
			if (RETHROW) throw t;
		}
	}

	/**
	 * Captures the Throwable from an assertEquals (on doubles with a delta), and log.errors it.
	 * <p>If RETHROW is true, then the Throwable is rethrown to fail the corresponding test.
	 * @param descriptionOfAssertionMessage replica of assert message
	 * @param expected the expected double
	 * @param actual the actual double
	 * @param delta the maximum delta between expected and actual for which both numbers are still considered equal.
	 */
	public static void assertEquals(String descriptionOfAssertionMessage, double expected, double actual, double delta) {
		try {
			org.junit.Assert.assertEquals(descriptionOfAssertionMessage, expected, actual, delta);
			log.info(descriptionOfAssertionMessage + " ... passed! Value = " + expected);
		}
		catch (Throwable t) {
			logThrowable(descriptionOfAssertionMessage, t);
			if (RETHROW) throw t;
		}
	}

	/**
	 * Captures the Throwable from an assertEquals (on doubles), and log.errors it.
	 * <p>If RETHROW is true, then the Throwable is rethrown to fail the corresponding test.
	 * <p>The "delta" value used to compare doubles is 0
	 * @param descriptionOfAssertionMessage replica of assert message
	 * @param expected the expected double
	 * @param actual the actual double
	 */
	public static void assertEquals(String descriptionOfAssertionMessage, double expected, double actual) {
		try {
			org.junit.Assert.assertEquals(descriptionOfAssertionMessage, expected, actual, 0);
			log.info(descriptionOfAssertionMessage + " ... passed! Value = " + expected);
		}
		catch (Throwable t) {
			logThrowable(descriptionOfAssertionMessage, t);
			if (RETHROW) throw t;
		}
	}

	/**
	 * Captures the Throwable from an assertTrue, and log.errors it.
	 * <p>If RETHROW is true, then the Throwable is rethrown to fail the corresponding test.
	 * @param descriptionOfAssertionMessage replica of assert message
	 * @param condition the condition to be checked to see if it is true
	 */
	public static void assertTrue(String descriptionOfAssertionMessage, boolean condition) {
		try {
			org.junit.Assert.assertTrue(descriptionOfAssertionMessage, condition);
			log.info(descriptionOfAssertionMessage + " ... passed!");
		}
		catch (Throwable t) {
			logThrowable(descriptionOfAssertionMessage, t);
			if (RETHROW) throw t;
		}
	}

	/**
	 * @param descriptionOfAssertionMessage replica of assert message
	 * @param t the Throwable to log
	 */
	private static void logThrowable(String descriptionOfAssertionMessage, Throwable t) {
		log.error(descriptionOfAssertionMessage, t);
		String essentialsFromThrowable = ExceptionHandler.getEssentialsFromThrowable(t);
		RUN_SUMMARY.error(descriptionOfAssertionMessage + essentialsFromThrowable);
		if (ExceptionHandler.displaySplash) ExceptionHandler.splash(essentialsFromThrowable, t);
	}


	/**
	 * Captures the Throwable from an assertFalse, and log.errors it.
	 * <p>If RETHROW is true, then the Throwable is rethrown to fail the corresponding test.
	 * @param descriptionOfAssertionMessage replica of assert message
	 * @param condition the condition to be checked to see if it is false
	 */
	public static void assertFalse(String descriptionOfAssertionMessage, boolean condition) {
		try {
			org.junit.Assert.assertFalse(descriptionOfAssertionMessage, condition);
			log.info(descriptionOfAssertionMessage + " ... passed!");
		}
		catch (Throwable t) {
			logThrowable(descriptionOfAssertionMessage, t);
			if (RETHROW) throw t;
		}
	}

	/**
	 * Captures the Throwable from an assertNull, and log.errors it.
	 * <p>If RETHROW is true, then the Throwable is rethrown to fail the corresponding test.
	 * @param descriptionOfAssertionMessage replica of assert message
	 * @param objectToBeNull the Object to be checked to see if it is null
	 */
	public static void assertNull(String descriptionOfAssertionMessage, Object objectToBeNull) {
		try {
			org.junit.Assert.assertNull(descriptionOfAssertionMessage, objectToBeNull);
			log.info(descriptionOfAssertionMessage + " ... passed!");
		}
		catch (Throwable t) {
			logThrowable(descriptionOfAssertionMessage, t);
			if (RETHROW) throw t;
		}
	}

}
