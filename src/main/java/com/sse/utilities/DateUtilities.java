package com.sse.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A collection of utilities associated with Date and Time
 * @author mitchella3
 */
public final class DateUtilities {

	private DateUtilities() {
	    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}	
	
    /**
     * Adds a number of years to a date (which has been provided as a String in a given date Format e.g. dd/MM/yyyy).<br>
     * The new date is returned in the same format.
     * @param previousDate the String representation of Date  to be added to
     * @param yearsToAdd number of years to add
     * @param dateFormat a pattern in the format used by SimpleDateFormat.
     * @return the new date in the same format.
     * @throws ParseException if the previousDate cannot be parse as a valid date.
     */
    public static String adjustDateByYears(String previousDate, int yearsToAdd, String dateFormat) throws ParseException {
 	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,Locale.getDefault());
 	    Calendar c = Calendar.getInstance();
 	    c.setTime(sdf.parse(previousDate));
 	    c.add(Calendar.YEAR, yearsToAdd);
 	    return sdf.format(c.getTime());
    }

    /**
     * Adds a specified number of days to the current date and returns a LocalDate object.
     * @param numberOfDays the number of days to add. This can be negative.
     * @return the LocalDate corresponding to today + numberOfDays
     */
    public static LocalDate addDaystoToday(int numberOfDays) {
		return LocalDate.now().plusDays(numberOfDays);
	}

    /**
     * Formats a LocalDate object in the specified format
     * @param date the LocalDate to be formatter.
     * @param formatPattern the DateTimeFormatter pattern  (e.g. dd/MM/yyyy)
     * @return the String representation of the LocalDate in the specified pattern.
     */
    public static String formatLocalDate(LocalDate date, String formatPattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
		return date.format(formatter);
    }

    /**
     * Parses a LocalDate from a String, using the specified format.<br>
     * This will throw a DateTimeParseException if the dateString cannot be parsed.
     * @param dateString the String representation of the LocalDate to be parsed.
     * @param formatPattern the DateTimeFormatter pattern  (e.g. dd/MM/yyyy)
     * @return the LocalDate obtained from parsing the String.
     */
    public static LocalDate parseDateFromString(String dateString, String formatPattern) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
    	return LocalDate.parse(dateString, formatter);
    }

    /**
     * Formats a LocalDateTime object in the specified format
     * @param dateTime the LocalDateTime to be formatted.
     * @param formatPattern the DateTimeFormatter pattern  (e.g. dd/MM/yyyy HH:mm)
     * @return the String representation of the LocalDate in the specified pattern.
     */
    public static String formatLocalDateTime(LocalDateTime dateTime, String formatPattern) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
    	return dateTime.format(formatter);
    }
    
    /**
     * Formats a Calendar's date object in the specified format.
     * <br>If the passed Calendar is null, an empty String is returned.
     * @param calendar the Calendar object to be formatted.
     * @param formatPattern the DateFormat pattern  (e.g. dd/MM/yyyy HH:mm)
     * @return the String representation of the Calendar's Date in the specified pattern.
     */
    public static String formatCalendar(Calendar calendar, String formatPattern) {
    	if (calendar == null) return "";
    	Date date = calendar.getTime();
    	DateFormat formatter = new SimpleDateFormat(formatPattern,Locale.getDefault());
    	return formatter.format(date);
    }

    /**
     * Parses a LocalDateTime from a String, using the specified format.<br>
     * This will throw a DateTimeParseException if the dateString cannot be parsed.
     * @param dateTimeString the String representation of the LocalDate to be parsed.
     * @param formatPattern the DateTimeFormatter pattern  (e.g. dd/MM/yyyy HH:mm)
     * @return the LocalDateTime obtained from parsing the String.
     */
    public static LocalDateTime parseDateTimeFromString(String dateTimeString, String formatPattern) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
    	return LocalDateTime.parse(dateTimeString, formatter);
    }

    /**
     * Receives a String representation of a date value in one format, and reformats to a String in another format.
     * <p>This is a convenience method which internally invokes parseDateFromString followed by formatLocalDate
     * @param dateString the String to be parsed and reformatted
     * @param fromPattern the date format pattern in which the received String is formatted.
     * @param toPattern the target format of the output String
     * @return the reformatted String
     * @see #parseDateFromString(String, String)
     * @see #formatLocalDate(LocalDate, String)
     */
    public static String reformatDate(String dateString, String fromPattern, String toPattern) {
    	return formatLocalDate(parseDateFromString(dateString,fromPattern),toPattern);
    }

    /**
     * Receives a String representation of a dateTime value in one format, and reformats to a String in another format.
     * <p>This is a convenience method which internally invokes parseDateTimeFromString followed by formatLocalDateTime
     * @param dateTimeString the String to be parsed and reformatted
     * @param fromPattern the date format pattern in which the received String is formatted.
     * @param toPattern the target format of the output String
     * @return the reformatted String
     * @see #parseDateTimeFromString(String, String)
     * @see #formatLocalDateTime(LocalDateTime, String)
     */
    public static String reformatDateTime(String dateTimeString, String fromPattern, String toPattern) {
    	return formatLocalDateTime(parseDateTimeFromString(dateTimeString,fromPattern),toPattern);
    }

    /**
     * Given a beginning date, return the next date <b>strictly</b> after the beginning date which has a certain day of the month.
     * <br><b>This currently ONLY caters for the day value to be in the range 1 to 28.</b>
     * <p>As an example, for a beginDate of 18/01/2000 and a 'nextDayOfMonth' argument of 12, this would return a LocalDate corresponding
     * to 12/02/2000.
     * <br>For a beginDate of 18/01/2000 and a 'nextDayOfMonth' argument of 18, this would return a LocalDate corresponding
     * to 18/02/2000.
     * @param beginDate the beginning date to start from to find the subsequent date
     * @param nextDayOfMonth the day of the month we want to find the next instance of (from 1 to 28 are the only supported values at present)
     * @return the subsequent date with the required day component.
     */
    public static LocalDate getNextDateWithSpecificDay(LocalDate beginDate, int nextDayOfMonth) {
    	LocalDate nextDate = beginDate.withDayOfMonth(nextDayOfMonth);
    	if (nextDate.isAfter(beginDate)) return nextDate;
    	return nextDate.plusMonths(1);
    }

}
