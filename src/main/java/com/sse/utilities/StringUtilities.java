package com.sse.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.InvalidArgumentException;

/**
 * A class to hold static general String-related utilities.
 * 
 * @author mitchella3
 */
public final class StringUtilities {
	private final static Pattern SALESFORCE_LIGHTNING_ID_PATTERN = Pattern.compile("[a-zA-Z0-9]{18}");
	private static Logger log = LogManager.getLogger(StringUtilities.class);

	private StringUtilities() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	/**
	 * Create and return a new <code>String</code> consisting of a number of
	 * occurrences of a given character.
	 * 
	 * @param chr The character the <code>String</code> is to consist of.
	 * @param len Length of required <code>String</code>. If less than or equal to
	 *            zero an empty <code>String</code> is returned.
	 * @return The created string.
	 */
	public static String repeat(char chr, int len) {
		if (len <= 0)
			return "";
		char[] charArray = new char[len];
		return new String(charArray).replace(charArray[0], chr);
	}

	/**
	 * Create and return a new <code>String</code> by padding the end of a given
	 * <code>String</code> to a specified length with blanks.
	 * 
	 * @param str <code>String</code> to be padded.
	 * @param len Length to which str is to be padded. If the length of str is
	 *            greater than or equal to len, str is returned unaltered.
	 * @return The padded <code>String</code>.
	 */
	public static String rightPad(String str, int len) {
		return rightPad(str, len, ' ');
	}

	/**
	 * Create and return a new <code>String</code> by padding the end of a given
	 * <code>String</code> to a specified length with the specified character.
	 * 
	 * @param str <code>String</code> to be padded.
	 * @param len Length to which str is to be padded. If the length of str is
	 *            greater than or equal to len, str is returned unaltered.
	 * @param pad The padding character.
	 * @return The padded <code>String</code>.
	 */
	public static String rightPad(String str, int len, char pad) {
		int padLength = len - str.length();
		// Note: if padLength is negative an empty string is returned by the repeat
		// method.
		String padString = repeat(pad, padLength);
		return str + padString;
	}

	/**
	 * Compare the first 'n' characters of two Strings (padding to the right with
	 * spaces if required).
	 * 
	 * @param str1 the first String to be compared.
	 * @param str2 the second String to be compared.
	 * @param len  Length to which str is to be padded. If the length of str is
	 *             greater than or equal to len, str is returned unaltered.
	 * @return true if the first 'n' characters match, otherwise false.
	 */
	public static boolean compareFirstNCharacters(String str1, String str2, int len) {
		if (str1 == null || str2 == null)
			return false;
		String comp1 = rightPad(str1, len).substring(0, len);
		String comp2 = rightPad(str2, len).substring(0, len);
		return comp1.equals(comp2);
	}

	/**
	 * Splits a URL into constituent parts, using '/' as the delimiter.<br>
	 * Assuming it's a well-formed URL the resultant String[] will have the
	 * following elements:<br>
	 * <ul>
	 * <li>element 0 : protocol e.g. "https"
	 * <li>element 1 : empty String
	 * <li>element 2 : domain e.g. "cs88.salesforce.com"
	 * <li>element 3 : the next section of the URL e.g. 0019E00000aXZoF (Classic
	 * Salesforce example)
	 * <li>element 4+ : remaining bits of the URL
	 * </ul>
	 * For Lightning, element 5 or 6 will usually contain the 18-character object
	 * id.
	 * <p>
	 * This will throw a NullPointerException if the url is null.
	 * 
	 * @param url String containing the current URL
	 * @return a String array containing the sections of the URL.
	 */
	public static String[] splitURL(String url) {
		return url.split("/", 0);
	}

	/**
	 * From a URL which represents a Salesforce Object IN CLASSIC SALESFORCE, this
	 * will return the 15-character object id. <br>
	 * For instance, given a URL : https://cs88.salesforce.com/0019E00000aXZoF <br>
	 * or
	 * https://cs88.salesforce.com/0019E00000aXZoF#0019E00000aXZoF_RelatedOpportunityList_target
	 * , this method will return 0019E00000aXZoF An ArrayIndexOutOfBoundsException
	 * will be thrown if a URL is passed which has nothing after the domain.<br>
	 * This method will throw a NullPointerException if the url is null<br>
	 * 
	 * @param url the Salesforce URL of a current object
	 * @return the 15-character object id
	 */
	public static String getSalesforceObjectIDFromURL(String url) {
		return splitURL(url)[3].substring(0, 15);
	}

	/**
	 * From a URL which represents a Salesforce Object IN LIGHTNING SALESFORCE, this
	 * will return the 18-character object id. <br>
	 * For instance, given a URL :
	 * https://rbi--unitysit.lightning.force.com/lightning/r/Account/0019E00000rrJBNQA2/view
	 * this method will return 0019E00000rrJBNQA2
	 * <p>
	 * In Lightning, the object id appears to be in the 5th or 6th section of the
	 * URL. An ArrayIndexOutOfBoundsException or a RuntimeException will be thrown
	 * if a URL is passed which doesn't contain something resembling an object id in
	 * the right place(s). <br>
	 * This method will throw a NullPointerException if the url is null<br>
	 * 
	 * @param url the Salesforce URL of a current object
	 * @return the 18-character object id
	 */
	public static String getSalesforceObjectIDFromLightningURL(String url) {
		String[] urlBits = splitURL(url);
		if (looksLikeObjectId(urlBits[5]))
			return urlBits[5];
		if (looksLikeObjectId(urlBits[6]))
			return urlBits[6];
		throw new RuntimeException("Could not obtain object id from URL " + url);
	}

	private static boolean looksLikeObjectId(String urlBit) {
		Matcher matcher = SALESFORCE_LIGHTNING_ID_PATTERN.matcher(urlBit);
		return matcher.matches();
	}

	/**
	 * Returns the portion of a source String before the first occurrence of a given
	 * search String.<br>
	 * If the search string is not found, the whole source String is returned. <br>
	 * If the source string is "Problem at 10:00" and the search string is " at ",
	 * this method returns "Problem".<br>
	 * If the source String is null, this method returns null.
	 * 
	 * @param source the source String
	 * @param search the search String
	 * @return the portion of the String before the search string, otherwise the
	 *         original String.
	 */
	public static String before(String source, String search) {
		if (source == null)
			return null;
		int index = source.indexOf(search);
		if (index == -1)
			return source;
		return source.substring(0, index);
	}

	/**
	 * Returns the portion of a source String after the first occurrence of a given
	 * search String.<br>
	 * If the search string is not found, the whole source String is returned. <br>
	 * If the source string is "Problem at 10:00" and the search string is " at ",
	 * this method returns "10:00".<br>
	 * If the source String is null, this method returns null.
	 * 
	 * @param source the source String
	 * @param search the search String
	 * @return the portion of the String after the search string, otherwise the
	 *         original String.
	 */
	public static String after(String source, String search) {
		if (source == null)
			return null;
		int index = source.indexOf(search);
		if (index == -1)
			return source;
		return source.substring(index + search.length());
	}

	/**
	 * Checks whether a String is null or empty of just contains whitespace, in
	 * which case it returns true.
	 * <p>
	 * Otherwise it returns false.
	 * 
	 * @param s the String to check for emptiness or null.
	 * @return true if the passed String is null or empty or whitespace.
	 */
	public static boolean isNullEmptyOrWhiteSpace(String s) {
		return s == null || s.trim().isEmpty();
	}

	/**
	 * This method takes a String containing comma-separated name/value pairs
	 * (specified by an equals symbol '=') and constructs a Map of name/value pairs.
	 * <p>
	 * As an example, the String "Quantity=1, Percent=10" will be parsed into a Map
	 * of 2 elements with keys <br>
	 * Quantity and Percent, and values "1" and "10" respectively.
	 * <p>
	 * If the passed String is empty or null, an empty Map is returned.
	 * <p>
	 * If any entry within commas does not correspond to a name/value pair (e.g.
	 * there is no "="), then that entry is logged and bypassed.
	 * <p>
	 * Any leading or trailing spaces are trimmed prior to insertion into the Map.
	 * 
	 * @param commaSeparatedNameValuePairs a String containing comma-separated
	 *                                     name/value pairs.
	 * @return the corresponding Map representation of the name/value pairs.
	 */
	public static Map<String, String> createMapFromCommaSeparatedNameValuePairs(String commaSeparatedNameValuePairs) {
		Map<String, String> map = new HashMap<>();
		if (isNullEmptyOrWhiteSpace(commaSeparatedNameValuePairs))
			return map;
		String[] split = commaSeparatedNameValuePairs.split(",");
		for (String attribute : split) {
			String[] nameValue = attribute.split("=", 2);
			if (nameValue.length < 2) {
				log.warn("Attribute <" + attribute + "> is not a valid name/value pair");
			} else {
				map.put(nameValue[0].trim(), nameValue[1].trim());
			}
		}
		return map;
	}

	/**
	 * This method takes a String containing comma-separated values and constructs a
	 * HashSet of trimmed values.
	 * <p>
	 * If the passed String is empty or null, an empty Set is returned.
	 * <p>
	 * Any leading or trailing spaces are trimmed prior to insertion into the Map.
	 * 
	 * @param commaSeparatedValues a String containing comma-separated name/value
	 *                             pairs.
	 * @return the corresponding Set representation of the values.
	 */
	public static Set<String> createSetFromCommaSeparatedValues(String commaSeparatedValues) {
		Set<String> set = new HashSet<>();
		if (isNullEmptyOrWhiteSpace(commaSeparatedValues))
			return set;
		String[] split = commaSeparatedValues.split(",");
		for (String attribute : split) {
			set.add(attribute.trim());
		}
		return set;
	}

	/**
	 * If a String contains a line break character (Windows or Unicode), this
	 * returns the chunk before the line break.
	 * <p>
	 * If the String is null, empty or doesn't contain a line break, this returns
	 * the original String.
	 * 
	 * @param input the String to interrogate.
	 * @return the String prior to any line breaks.
	 */
	public static String returnStringBeforeLineBreak(String input) {
		if (isNullEmptyOrWhiteSpace(input))
			return input;
		return input.split("\\R")[0];
	}

	/**
	 * Utility method to handle looking for text which contains single quotes.
	 * <p>
	 * If we have, for example, the need to create a dynamic XPath of the form :<br>
	 * "//span[text()=" + myName + "]"; <br>
	 * BUT myName is known to contain a single quote e.g. Her Majesty's Treasury ,
	 * then we can instead use <br>
	 * * "//span[text()=" + createXPathFromStringWithQuoteMarks(myName) + "]"; <br>
	 * which will convert the string to something of the form : <br>
	 * concat('Her Majesty',"'",'s Treasury')
	 * 
	 * @param input a String potentially containing a single quote
	 * @return the concat form of the string if it contains a quote, otherwise the
	 *         original input
	 */
	public static String createXPathFromStringWithQuoteMarks(String input) {
		if (input.contains("'")) {
			String prefix = "";
			String[] elements = input.split("'");

			String output = "concat(";

			for (String s : elements) {
				output += prefix + "'" + s + "'";
				prefix = ",\"'\",";
			}

			if (output.endsWith(","))
				output = output.substring(0, output.length() - 2);

			output += ")";

			return output;
		}
		return input;
	}

	/**
	 * A method which looks in a Map<String, String> for the value associated with a
	 * passed key, and returns the corresponding value if it exists (and is not
	 * blank), otherwise it returns a provided default value.
	 * 
	 * @param dataMap      the map which may contain a value for the provided key.
	 * @param key          the key to look for in the map.
	 * @param defaultValue a value to return in the event that the key is not found,
	 *                     or the value is blank.
	 * @return the value from the map or the default value
	 */
	public static String getFromMap(Map<String, String> dataMap, String key, String defaultValue) {
		String mapValue = dataMap.get(key);
		if (isNullEmptyOrWhiteSpace(mapValue))
			return defaultValue;
		return mapValue;
	}

	/**
	 * A method which validates that every key in a Map<String, String> matches one
	 * of the items in a String array of expected keys.
	 * <p>
	 * If there is a key in the Map which is not expected, an
	 * InvalidArgumentException is thrown.
	 * 
	 * @param dataMap   the map to validate.
	 * @param validKeys an array of valid keys against which the map is to be
	 *                  validated
	 */
	public static void validateMapAgainstArray(Map<String, String> dataMap, String... validKeys) {
		if (dataMap != null && !dataMap.isEmpty()) {
			List<String> validKeysList = new ArrayList<>(Arrays.asList(validKeys));
			for (String mapKey : dataMap.keySet()) {
				if (!validKeysList.contains(mapKey)) {
					String message = "The data map contains an unexpected value <" + mapKey
							+ "> Please check the spelling and case of the value and whether the value is supported in the code."
							+ "\nThe valid values are : " + validKeysList;
					log.error(message);
					throw new InvalidArgumentException(message);
				}
			}
		}
	}
	
	/**
	 * Returns a readable String from a Calendar object (i.e. the date/time) or "null" if the Calendar is null.
	 * <br>Mainly used in logging.
	 * @param calendar a calendar object from which to derive a friendly String representation.
	 * @return a readable String representation of the Calendar/time or "null" if it's null.
	 */
	public static String getStringFromCalendar(Calendar calendar) {
		if (calendar == null) return "null";
		return calendar.getTime()+"";
	}

}
