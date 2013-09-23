package com.medisanaspace.library;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Helper class providing functions to generate and parse Dates/Strings.
 * 
 * @author Clemens Lode (c) Medisana Space Technologies GmbH, 2012
 *         clemens.lode@medisanaspace.com
 * 
 * @version $Revision: 1.0 $
 */
public final class DateHelper {

	private DateHelper() {
	}

	/**
	 * Method generateCSVDateString.
	 * @param date Date
	 * @return String
	 */
	public static String generateCSVDateString(final Date date) {
		DateFormat formatter;
		formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		return formatter.format(date);
	}

	/**
	 * Method generateEarlierDate.
	 * @param earlierTime long
	 * @return Date
	 */
	public static Date generateEarlierDate(final long earlierTime) {
		long timeInMillis = System.currentTimeMillis() - earlierTime;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);
		return cal.getTime();
	}

	/**
	 * Method generateCurrentDate.
	 * @return Date
	 */
	public static Date generateCurrentDate() {
		long timeInMillis = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);
		return cal.getTime();
	}

	/**
	 * Method isYoungerThan.
	 * @param date Date
	 * @param oldestDateInMillis long
	 * @return boolean
	 */
	public static boolean isYoungerThan(final Date date,
			final long oldestDateInMillis) {

		if (date == null) {
			return false;
		}

		long dateToCheckInMillis = date.getTime();
		return dateToCheckInMillis > oldestDateInMillis;
	}

	/**
	 * Method isYoungerBy.
	 * @param date Date
	 * @param maxAgeInMillis long
	 * @return boolean
	 */
	public static boolean isYoungerBy(final Date date, final long maxAgeInMillis) {
		if (date == null) {
			return false;
		}
		long dateToCheckInMillis = date.getTime();
		return dateToCheckInMillis > new Date().getTime() - maxAgeInMillis;
	}

	/**
	 * Method getDate.
	 * @param date String
	 * @return Date
	 * @throws Exception
	 */
	public static Date getDate(final String date) throws Exception {
		try {
			Long time = Long.parseLong(date);
			return new Date(time);
		} catch (NumberFormatException n) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"EEE MMM dd HH:mm:ss z yyyy");
				return dateFormat.parse(date);
			} catch (ParseException d) {
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					return dateFormat.parse(date);
				} catch (ParseException e) {
					try {
						SimpleDateFormat dateFormat = new SimpleDateFormat(
								"yyyy-MM-dd");
						return dateFormat.parse(date);
					} catch (ParseException f) {

						Logger.getLogger(DateHelper.class.getName()).log(
								Level.WARNING,
								"Invalid date format (" + e.toString() + ").");
						throw new Exception("Invalid date format ("
								+ e.toString() + ")."); // TODO
					}
				}
			}
		}
	}
}
